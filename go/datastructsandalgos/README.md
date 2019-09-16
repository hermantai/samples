## Samples from the book: Learn Data Structures and Algorithms with Golang: Level up your Go programming skills to develop faster and more efficient code


## Run files in the datastructsandalgos folder

Assuming github.com/hermantai... is in your go workspace, you can run datastructs
with:

    $ go run github.com/hermantai/samples/go/datastructsandalgos/datastructs

This is most convenient because the binary is not saved anywhere.

Or you can cd to the directory and run:

    github.com/hermantai/samples/go/datastructsandalgos$ go run datastructs/datastructs.go

You can also build before running:

    ~/go_workspace/src/github.com/hermantai/samples/go/datastructsandalgos/datastructs$ go build
    ~/go_workspace/src/github.com/hermantai/samples/go/datastructsandalgos/datastructs$ ls
    datastructs    datastructs.go

    ~/go_workspace/src/github.com/hermantai/samples/go/datastructsandalgos/datastructs$ ./datastructs
    Hello world

If you install it, the binary will be in the global bin directory

    ~/go_workspace/src/github.com/hermantai/samples/go/datastructsandalgos/datastructs$ go install
    ~/go_workspace/src/github.com/hermantai/samples/go/datastructsandalgos/datastructs$ ls ~/go_workspace/bin
    hello

## View docs

    godoc -http=:8000

Then you can view the docs for the datastructs command here: http://localhost:8000/pkg/github.com/hermantai/samples/go/datastructsandalgos/datastructs/

The docs are read from the source code ("the src directory").

