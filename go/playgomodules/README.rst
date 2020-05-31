#############################################
github.com/hermantai/samples/go/playgomodules
#############################################

Try out go modules described in https://blog.golang.org/using-go-modules.

************
Run the code
************

Run tests
::

  $ go test

Run the sample script
::

  $ go run main/main.go

List all dependencies
::

  $ go list -m all

Tag a new version
::

  $ git tag v1.0.0
  $ git push origin v1.0.0
