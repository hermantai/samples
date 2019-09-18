# hello world gRPC sample

From https://www.grpc.io/docs/quickstart/go/.

## Run the server

    $ cd github.com/hermantai/samples/go/grpcsamples/helloworld
    $ go run greeter_server/main.go

or 

    $ go run github.com/hermantai/samples/go/grpcsamples/helloworld/greeter_server

## Run the client

    $ go run greeter_client/main.go 
    2019/09/15 23:18:19 Greeting: Hello world

or

    $ go run github.com/hermantai/samples/go/grpcsamples/helloworld/greeter_client

## Development

From: http://google.github.io/proto-lens/installing-protoc.html
Install protoc

    PROTOC_ZIP=protoc-3.7.1-linux-x86_64.zip
    curl -OL https://github.com/google/protobuf/releases/download/v3.7.1/$PROTOC_ZIP
    sudo unzip -o $PROTOC_ZIP -d /usr/local bin/protoc
    sudo unzip -o $PROTOC_ZIP -d /usr/local include/*
    rm -f $PROTOC_ZIP

Install the go plugin

    go get -u github.com/golang/protobuf/protoc-gen-go

Generate the proto again after changes

    samples/go/grpcsamples/helloworld$ protoc -I helloworld/ helloworld/helloworld.proto --go_out=plugins=grpc:helloworld
