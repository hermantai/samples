# Quickstart

The golang package management is a bit complicated
(https://golang.org/doc/code.html). Below is how to have a quickstart to
use sample go codes in github.com/hermantai/samples/go.

First, make sure you like your GOPATH:

    $ go env GOPATH

Then, install the hello program (it's hello.go inside a hello directory):

    $ go install github.com/hermantai/samples/go/hello

You can also omit the full package path if you are already inside the
samples/go/hello directory.

You can then execute the program with:

    $ $GOPATH/bin/hello

If you havn't clone the repository and put it in your GOPATH, you should use
"go get" instead:

    $ go get github.com/hermantai/samples/go/hello
    $ $GOPATH/bin/hello
