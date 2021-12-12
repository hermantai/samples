#############################################
github.com/hermantai/samples/go/playgomodules
#############################################

Try out go modules described in https://blog.golang.org/using-go-modules.

************
Important commands
************

init a module
::

  $ go mod init example.com/hello

List all dependencies
::

  $ go list -m all

Use a tagged version instead of a tagged one. This command would update go.mod.
::

  $ go get golang.org/x/text

Get a specific version
::

  $ go get rsc.io/sampler@v1.3.1

List versions of a module
::

  $ go list -m -versions rsc.io/sampler

Clean up dependencies
::

  $ go mod tidy

Tag a new version
::

  $ git tag v1.0.0
  $ git push origin v1.0.0

************
Run the code
************

Run tests
::

  $ go test

Run the sample script
::

  $ go run main/main.go

Outside of the repo, we can run commands with
::

  # download and install the package
  $ go run go get github.com/hermantai/samples/go/playgomodules/main

  # run it
  $ main
