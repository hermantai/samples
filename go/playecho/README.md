# Overview
A package that play with the echo server framework:
https://echo.labstack.com/guide/

# Quickstart

Download echo

    $ go get github.com/labstack/echo/v4

Run the server

    $ go run server.go

Try the following pages:
* http://localhost:1323
* http://localhost:1323/users/Joe
* http://localhost:1323/show?team=x-men&member=wolverine

Run the following commands:
* $ curl -F "name=Joe Smith" -F "email=joe@labstack.com" http://localhost:1323/save
* $ curl -F "name=Joe Smith" -F "avatar=@/path/to/your/avatar.png" http://localhost:1323/saveWithFiles
* curl -F "name=peter" -F "email=abc@gmail.com" http://localhost:1323/users
