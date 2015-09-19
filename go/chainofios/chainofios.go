// Copyright 2015 Herman Tai
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// chainofios (Chain of IOs) demos how to start a web server, an rpc server,
// and a general purpose tcp server. It also demo some file read and write
// (that's why it's a chain of IOs...) including the use of template. A lot of
// code snippets are from the book https://www.golang-book.com/books/intro
package main

import (
	"bufio"
	"encoding/json"
	"flag"
	"fmt"
	"html/template"
	"io"
	"io/ioutil"
	"net"
	"net/http"
	"net/rpc"
	"net/rpc/jsonrpc"
	"os"
	"path/filepath"
	"runtime"
	"strconv"
	"time"
)

type WebPage struct {
	Content string
}

const HTML_TEMPLATE = `<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chain of IOs</title>
</head>
<body>
以下是記錄:
<p>
	{{.Content}}
</p>
</body>
</html>
`

var TMP_FOLDER = "/tmp/chainofios-" + strconv.FormatInt(
	time.Now().UTC().Unix(),
	10,
)

var TCP_SERVER_PORT int  //= 8001
var RPC_SERVER_PORT int  //= 8002
var HTTP_SERVER_PORT int //= 8003

// startTCPServer starts a pure TCP server with no other protocols
func startTCPServer() {
	listener, err := net.Listen("tcp", ":"+strconv.Itoa(TCP_SERVER_PORT))

	if err != nil {
		fmt.Println(err)
		return
	}

	for {
		conn, err := listener.Accept()
		if err != nil {
			fmt.Println(err)
			continue
		}
		go handleTCPServerConnection(conn)
	}
}

// handleTCPServerConnection is used by the TCP server to handle TCP
// connections from clients. This function should usually be called as a
// goroutine
func handleTCPServerConnection(conn net.Conn) {
	defer conn.Close()

	msg := "tcp server gets it"
	err := json.NewEncoder(conn).Encode(&msg)
	if err != nil {
		fmt.Printf("TCP server json encode error: %s\n", err)
	}
}

// getResponseFromTCPServer simulates a client connecting to the TCP server
// started in this demo
func getResponseFromTCPServer() (string, error) {
	// for some reason, using localhost causes mac to show a prompt for me to
	// say ok, but using 127.0.0.1 does not
	//// conn, err := net.Dial("tcp", "localhost:"+strconv.Itoa(TCP_SERVER_PORT))
	conn, err := net.Dial("tcp", "127.0.0.1:"+strconv.Itoa(TCP_SERVER_PORT))
	if err != nil {
		fmt.Printf("getResponseFromTCPServer error: %s\n", err)
		return "", err
	}

	defer conn.Close()

	var msg string
	err = json.NewDecoder(conn).Decode(&msg)
	if err != nil {
		fmt.Printf("TCP client json decode error: %s\n", err)
		return "", err
	}

	return msg, nil
}

// RPCServer is a type that is exported to the RPC server started by this
// demo, so the client can call its methods.
type RPCServer struct{}

// GetMessage is the only method exported by RPCServer for the RPC server
// started by this demo. What unusual is that the method only returns an
// error. The actual output (the happy path) is done by modifying the address
// that the second argument (a pointer) that points to. Notice that both
// RPCServer and the method it has are exportable.
func (server *RPCServer) GetMessage(req GetMessageRequest, resp *GetMessageResponse) error {
	msg, err := getResponseFromTCPServer()
	if err != nil {
		return err
	}

	resp.Msg = msg + ", rpc server gets it"
	return nil
}

// GetMessageRequest is the "input" of the GetMessage method for RPCServer.
// Since RPC calls can only take one argument, it's a usual practice to have a
// struct for it.
type GetMessageRequest struct{}

// GetMessageResponse is the "output" of the GetMessage method for RPCServer.
// Since RPC calls can only return one argument, it's a usual practice to have a
// struct for it, so the RPC call can return "multiple" values if it wants.
type GetMessageResponse struct {
	// It's very important that the field is exportable, otherwise you will
	// get an empty response if you use jsonrpc.Dial, or it will hang if you
	// use rpc.Dial (the server uses rpc.ServeConn instead of
	// jsonrpc.ServeConn)
	Msg string
}

// startRPCServer starts the json RPC server for this demo
func startRPCServer() {
	rpc.Register(new(RPCServer))
	listener, err := net.Listen("tcp", ":"+strconv.Itoa(RPC_SERVER_PORT))
	if err != nil {
		fmt.Println(err)
		return
	}
	for {
		conn, err := listener.Accept()
		if err != nil {
			continue
		}
		go jsonrpc.ServeConn(conn)
	}
}

// getResponse simulates a client connecting to the json RPC server started in this
// demo
func getResponseFromRPCServer() (string, error) {
	conn, err := jsonrpc.Dial("tcp", "127.0.0.1:"+strconv.Itoa(RPC_SERVER_PORT))
	if err != nil {
		fmt.Printf("getResponseFromRPCServer error: %s\n", err)
		return "", nil
	}

	defer conn.Close()

	var result GetMessageResponse
	err = conn.Call(
		"RPCServer.GetMessage",
		GetMessageRequest{},
		&result,
	)

	if err != nil {
		fmt.Printf("RPC client error: %s\n", err)
		return "", err
	}
	return result.Msg, nil
}

// startHTTPServer starts an HTTP Server for this demo
func startHTTPServer() {
	var h WebServerHandler

	http.Handle("/", h)
	err := http.ListenAndServe(
		"127.0.0.1:"+strconv.Itoa(HTTP_SERVER_PORT),
		nil,
	)

	if err != nil {
		fmt.Println(err)
	}
}

type WebServerHandler struct{}

func (WebServerHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	resp, err := getResponseFromRPCServer()
	if err != nil {
		w.WriteHeader(http.StatusInternalServerError)
		fmt.Println("Error getting response from rpc server:", err)
		return
	} else {
		w.Header().Set(
			"Content-Type",
			"text/html",
		)
		pageTemplate, err := getHTMLPageTemplate()

		if err != nil {
			fmt.Println("Error getting html page template:", err)
			return
		}

		tmpl, err := template.New("webpage").Parse(pageTemplate)
		if err != nil {
			fmt.Println("Error parsing template:", err)
			return
		}

		// If not using template, you would probably do
		//   io.WriteString(w, ...)
		// or
		//   fmt.Fprintf(w, ...)
		err = tmpl.Execute(w, WebPage{resp + ", <http server gets it>"})
		if err != nil {
			fmt.Println("Error running tmpl.Execute:", err)
			return
		}
	}
}

// getHTMLPageTemplate gets the HTML template for the HTTP server served in
// this demo. It intentionally does a bunch of writing to and reading from
// files to illustrate how to use the io and ioutil libraries.
func getHTMLPageTemplate() (string, error) {
	// First, create a folder to bury the HTML template in a file...
	os.Mkdir(TMP_FOLDER, 0755)
	templateFilepath := filepath.Join(TMP_FOLDER, "template.html")
	templateFile, err := os.Create(templateFilepath)
	if err != nil {
		fmt.Println("error creating file:", err)
		return "", err
	}
	_, err = templateFile.WriteString(HTML_TEMPLATE)

	if err != nil {
		return "", err
	}

	templateFile.Close()

	// Three ways to read it back, os.Open, ioutil or Scanner
	openedByOSOpen, err := os.Open(templateFilepath)
	if err != nil {
		return "", err
	}

	stat, err := openedByOSOpen.Stat()
	if err != nil {
		return "", err
	}
	fmt.Println("File has size:", stat.Size())

	// read the file 10 characters at a time
	content1 := ""
	bs := make([]byte, 10)
	for {
		n, err := openedByOSOpen.Read(bs)
		if err == io.EOF {
			break
		}
		if err != nil {
			return "", err
		}

		content1 += string(bs[:n])
	}
	openedByOSOpen.Close()

	if HTML_TEMPLATE != content1 {
		panic(
			fmt.Sprintf("content1 issue: %#v != %#v", HTML_TEMPLATE, content1),
		)
	}

	// read by ioutil.ReadFile

	bs, err = ioutil.ReadFile(templateFilepath)
	if err != nil {
		return "", err
	}
	content2 := string(bs)
	if HTML_TEMPLATE != content2 {
		panic(
			fmt.Sprintf("content2 issue: %#v != %#v", HTML_TEMPLATE, content2),
		)
	}

	// use Scanner to read it

	f, err := os.Open(templateFilepath)
	if err != nil {
		return "", err
	}
	defer f.Close()

	// by default it scans by lines
	scanner := bufio.NewScanner(f)
	content3 := ""
	for scanner.Scan() {
		content3 += scanner.Text() + "\n"
	}

	if err = scanner.Err(); err != nil {
		return "", err
	}

	if content1 != content3 {
		panic(
			fmt.Sprintf("content3 issue: %#v != %#v", HTML_TEMPLATE, content3),
		)
	}

	return content1, nil
}

func main() {
	basePortNumber := flag.Int(
		"base-port-number",
		8000,
		"The base port number for the servers started by this demo",
	)
	flag.Parse()
	fmt.Println("Unparsed command line args: ", flag.Args())

	TCP_SERVER_PORT = *basePortNumber + 1
	RPC_SERVER_PORT = *basePortNumber + 2
	HTTP_SERVER_PORT = *basePortNumber + 3

	go startTCPServer()
	go startRPCServer()
	go startHTTPServer()
	// let the server to start up before connecting it by the client below
	runtime.Gosched()

	url := "http://127.0.0.1:" + strconv.Itoa(HTTP_SERVER_PORT)
	resp, err := http.Get(url)
	if err != nil {
		fmt.Println(err)
		return
	}
	body, err := ioutil.ReadAll(resp.Body)
	resp.Body.Close()

	fmt.Println("Body is:")
	fmt.Println(string(body))

	var input string
	fmt.Println("You can point at your browser to the url to try out:", url)
	fmt.Println("Press enter to exit")
	fmt.Scanln(&input)
}
