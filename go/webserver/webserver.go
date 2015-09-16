// webserver demos how to start a simple HTTP server
package main

import (
	"fmt"
	"log"
	"net/http"
)

type MyHandler struct{}

func (MyHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "User agent is: %s\n", r.UserAgent())
	fmt.Fprintf(w, "Referer is: %s\n", r.Referer())
}

func main() {
	var h MyHandler

	http.Handle("/", h)
	err := http.ListenAndServe("localhost:4000", nil)

	if err != nil {
		log.Fatal(err)
	}
}
