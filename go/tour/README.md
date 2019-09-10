## A Tour of Go sample files

It's from https://tour.golang.org.

You can run the tour with:

    go get golang.org/x/tour
    tour

## Run files in the tours folder

Assuming github.com/hermantai... is in your go workspace, you can run hello
with:

    $ go run github.com/hermantai/samples/go/tours/hello

This is most convenient because the binary is not saved anywhere.

Or you can cd to the directory and run:

    github.com/hermantai/samples/go/tours$ go run hello/hello.go

You can also build before running:

    ~/go_workspace/src/github.com/hermantai/samples/go/tours/hello$ go build
    ~/go_workspace/src/github.com/hermantai/samples/go/tours/hello$ ls
    hello    hello.go

    ~/go_workspace/src/github.com/hermantai/samples/go/tours/hello$ ./hello
    Hello world

If you install it, the binary will be in the global bin directory

    ~/go_workspace/src/github.com/hermantai/samples/go/tours/hello$ go install
    ~/go_workspace/src/github.com/hermantai/samples/go/tours/hello$ ls ~/go_workspace/bin
    hello

## View docs

    godoc -http=:8000

Then you can view the docs for the hello command here: http://localhost:8000/pkg/github.com/hermantai/samples/go/tour/hello/

The docs are read from the source code ("the src directory").

