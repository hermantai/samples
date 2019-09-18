## A Tour of Go sample files

It's from https://tour.golang.org.

You can run the tour with:

    go get golang.org/x/tour
    tour

Notice that in Chrome OS, you need to start *tour* at a port that the crostini
container forward, e.g. 8000.

    tour -http 127.0.0.1:8000

See [reddit](https://www.reddit.com/r/Crostini/comments/99s3t9/wellknown_ports_are_now_autoforwarded_to_the/) for detail.

```
// TCP ports to statically forward to the container over SSH.
const uint16_t kStaticForwardPorts[] = {
  3000,  // Rails
  4200,  // Angular
  5000,  // Flask
  8000,  // Django
  8008,  // HTTP alternative port
  8080,  // HTTP alternative port
  8085,  // Cloud SDK
  8888,  // ipython/jupyter
  9005,  // Firebase login
};
```

Another option is to use a connection forwarder: https://github.com/kzahel/connection-forwarder

## Run files in the tour folder

Assuming github.com/hermantai... is in your go workspace, you can run hello
with:

    $ go run github.com/hermantai/samples/go/tour/hello

This is most convenient because the binary is not saved anywhere.

Or you can cd to the directory and run:

    github.com/hermantai/samples/go/tour$ go run hello/hello.go

You can also build before running:

    ~/go_workspace/src/github.com/hermantai/samples/go/tour/hello$ go build
    ~/go_workspace/src/github.com/hermantai/samples/go/tour/hello$ ls
    hello    hello.go

    ~/go_workspace/src/github.com/hermantai/samples/go/tour/hello$ ./hello
    Hello world

If you install it, the binary will be in the global bin directory

    ~/go_workspace/src/github.com/hermantai/samples/go/tour/hello$ go install
    ~/go_workspace/src/github.com/hermantai/samples/go/tour/hello$ ls ~/go_workspace/bin
    hello

## View docs

    godoc -http=:8000

Then you can view the docs for the hello command here: http://localhost:8000/pkg/github.com/hermantai/samples/go/tour/hello/

The docs are read from the source code ("the src directory").

