// A demo of a web server. The server class is `webserver`. The main function
// at the end of this file simply calls webserver.Start() to start an echo
// web server. Instead the Start method, there are mappings between
// different paths to methods of webserver to handle the requests from those
// paths.
//
// Paths do not begin with API_ROOT can simply be accessed from a web browser.
// Paths begin with API_ROOT can also be accessed from a web browser but they
// are usually accessed by a web app (e.g. javascript inside an html file) to
// do backend work.
package main

import (
	"flag"
	"fmt"
	"html"
	"net/http"
	"strings"
	"sync"

	"github.com/go-playground/validator"
	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
)

const (
	API_ROOT = "/api"
)

var port = flag.Int("port", 1323, "the port numbe to serve http requests")

// User represents a user.
// It supports binding to the following requests (through the tags: json, xml, form, query):
//
// * application/json
// * application/xml
// * application/x-www-form-urlencoded
// * URL Query parameter, e.g. /users?id=<userID>
//
// See https://echo.labstack.com/guide/binding/ for details
//
// The "validate" tag is for validating data. See
// https://echo.labstack.com/guide/request/
type User struct {
	Name   string `json:"name" xml:"name" form:"name" query:"name" validate:"required"`
	Email  string `json:"email" xml:"email" form:"email" query:"email" validate:"required,email"`
	UserId string `json:"user_id" xml:"user_id" form:"user_id" query:"user_id"`
}

type CustomValidator struct {
	validator *validator.Validate
}

func (cv *CustomValidator) Validate(i interface{}) error {
	if err := cv.validator.Struct(i); err != nil {
		// Optionally, you could return the error to give each route more control over the status code
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	return nil
}

func newCustomValidator() *CustomValidator {
	return &CustomValidator{validator: validator.New()}
}

type OperationResult struct {
	Msg string `json: msg`
}

func newOperationResult(msg string) *OperationResult {
	return &OperationResult{
		Msg: msg,
	}
}

type webserver struct {
	// Key: id; Value: User
	users map[string]*User
	// To protect write access of users
	usersLock sync.Mutex

	// The port number to serve http requests.
	port int
}

func (w *webserver) homePage(c echo.Context) error {
	s := `
<html>
	<head><title>Simple User Server</title></head>
	<body>
		<h1>Hello, world!</h1>
		<h2>Users</h2>
		<table border="1">
			<tr><th>Name</th><th>Email</th><th>ID</th></tr>
			%s
		</table>

		<p>Go to javascript page: <a href="static">link</a></p>
	</body>
</html>
`
	userRows := []string{}
	for _, user := range w.users {
		userRows = append(
			userRows,
			fmt.Sprintf("<tr><td>%s</td><td>%s</td><td>%s</td></tr>",
				html.EscapeString(user.Name), html.EscapeString(user.Email),
				user.UserId))

	}
	s = fmt.Sprintf(s, strings.Join(userRows, "\n"))
	return c.HTML(http.StatusOK, s)
}

func (w *webserver) getUser(c echo.Context) error {
	id := c.Param("id")
	return c.JSON(http.StatusOK, w.users[id])
}

func (w *webserver) getUsers(c echo.Context) error {
	users := []*User{}
	for _, user := range w.users {
		users = append(users, user)
	}
	return c.JSON(http.StatusOK, users)
}

// show simply echos what is being sent in the request
func (w *webserver) show(c echo.Context) error {
	// Get team and member from the query string
	team := c.QueryParam("team")
	member := c.QueryParam("member")
	s := "team:" + team + ", member:" + member + "\n"
	for key, values := range c.Request().Header {
		s += fmt.Sprintf("\n%s:\n", key)
		for _, value := range values {
			s += fmt.Sprintf("> %s\n", value)
		}
	}
	return c.String(http.StatusOK, s)
}

func (w *webserver) deleteUser(c echo.Context) error {
	id := c.Param("id")
	if _, ok := w.users[id]; ok {
		return c.JSON(http.StatusOK, newOperationResult(""))
	}
	return c.JSON(http.StatusNotFound, newOperationResult(""))
}

// Save a user to the server
func (w *webserver) saveUser(c echo.Context) error {
	u := new(User)
	if err := c.Bind(u); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, err.Error())
	}
	if err := c.Validate(u); err != nil {
		return err
	}

	userId := uuid.NewString()
	u.UserId = userId

	defer func() {
		w.usersLock.Unlock()
	}()
	w.usersLock.Lock()

	w.users[userId] = u
	return c.JSON(http.StatusCreated, u)
	// or
	// return c.XML(http.StatusCreated, u)
}

func newWebServer(port int) *webserver {
	users := make(map[string]*User)
	// users["1"] = &User{
	// 	Name:   "Initial User",
	// 	Email:  "abc@hotmail.com",
	// 	UserId: "1",
	// }
	return &webserver{
		users: users,
		port:  port,
	}
}

func (w *webserver) Start() {
	e := echo.New()
	e.Validator = newCustomValidator()

	// Paths for used in a browser
	e.GET("/", w.homePage)
	e.GET("/show", w.show)

	//
	// API methods (usually accessed by a client app or a curl command)
	//

	e.GET(fmt.Sprintf("%s/users/:id", API_ROOT), w.getUser)
	e.GET(fmt.Sprintf("%s/users", API_ROOT), w.getUsers)
	// e.PUT("/users/:id", updateUser)
	e.DELETE(fmt.Sprintf("%s/users/:id", API_ROOT), w.deleteUser)

	e.POST(fmt.Sprintf("%s/users", API_ROOT), w.saveUser)

	// Serve any file from static directory for path /static/*.
	e.Static("/static", "static")

	e.Logger.Fatal(e.Start(fmt.Sprintf(":%d", w.port)))
}

func main() {
	flag.Parse()

	newWebServer(*port).Start()
}
