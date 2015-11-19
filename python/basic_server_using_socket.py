# Basic server using socket

import socket

import six

def eval_server(address):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR ,1)
    sock.bind(address)
    sock.listen(5)
    while True:
        client, addr = sock.accept()
        print("Connection", addr)
        handler(client)

def handler(client):
    while True:
        try:
            client.send("Type a python expression:\n")
            req = client.recv(1000)
            if not req:
                break
            client.send("Result:\n")
            res = eval(req)
            if not isinstance(res, six.string_types):
                res = str(res)
            client.send(res)
            client.send("\n")
        except Exception as ex:
            print(ex)
            client.send(str(ex))
            client.send("\n")
    print("closed")

eval_server(('', 25000))
