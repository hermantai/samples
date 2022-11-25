# Overview
A sample Go echo server that manages users.

# Quickstart

Download all packages

    $ go get ./..

Run the server

    $ go run server.go

Or if you don't have go installed, you can simply use the release binaries, e.g.:

    $ ./releases/2022-11-24/simpleuserserver

Try the following pages:
* http://localhost:1323
* http://localhost:1323/show?team=x-men&member=wolverine

Save a user:

	$ curl -X POST -F "name=Joe Smith" -F "email=joe@labstack.com" http://localhost:1323/api/users
	{"name":"Joe Smith","email":"joe@labstack.com","user_id":"8c09061b-e451-4317-828f-614d31ed019e"}

Save another user:

	$ curl -X POST -F "name=Peter Pan" -F "email=peterp@gmail.com" http://localhost:1323/api/users
	{"name":"Peter Pan","email":"peterp@gmail.com","user_id":"432022da-9d9c-4e95-9720-a7edd903ca0e"}

Gets all users:

	$ curl http://localhost:1323/api/users
	[{"name":"Joe Smith","email":"joe@labstack.com","user_id":"8c09061b-e451-4317-828f-614d31ed019e"},{"name":"Peter Pan","email":"peterp@gmail.com","user_id":"432022da-9d9c-4e95-9720-a7edd903ca0e"}]

You can visit from a brower to look at all users: http://localhost:1323

You can either use curl or a browser to look up a single user (use the ID
returned by the command above that gets all users):
http://localhost:1323/api/users/8c09061b-e451-4317-828f-614d31ed019e

This is a page that generates the page from a javascript locally instead of
from a server: http://localhost:1323/static/jstesting.html

# Helpful commands

Get a package and upload go.mod:

    $ go get github.com/google/uuid
    $ go get github.com/labstack/echo/v4
