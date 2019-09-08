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

// webserver demos how to start a simple HTTP server.
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

	fmt.Println("Server is starting at localhost:4000")
	err := http.ListenAndServe("localhost:4000", nil)

	if err != nil {
		log.Fatal(err)
	}
}
