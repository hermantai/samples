Samples
#######

Templates and code snippets for various projects, so creating a new project
does not have to start from scratch

Go
**
For Go files with the `main` package, you can run the following to install it
in your $GOPATH::

  go get github.com/hermantai/samples/go/<directory-name-containing-the-file>

For example, if you want to try the "hello" script::

  go get github.com/hermantai/samples/go/hello

Then you would see the binary "hello" in the $GOPATH/bin directory.

Content of Go samples
=====================

base
  base is a basic demo of a Go program. This can be served as a template for
  other Go files.

hello
  hello is a demo of printing a string with a function defined in another
  package stringutil. It's from the tutorial "How to Write Go Code" in
  https://golang.org/doc/code.html

stringutil
  Package stringutil demonstrates how to define a package besides main.  It's
  from the tutorial "How to Write Go Code" in https://golang.org/doc/code.html

chainofios
  chainofios (Chain of IOs) demos how to start a web server, an rpc server,
  and a general purpose tcp server. It also demo some file read and write
  (that's why it's a chain of IOs...) including the use of template. A lot of
  code snippets are from the book https://www.golang-book.com/books/intro

eatingrace
  eatingrace is a demo of concurrency in Go using math/rand, sync.Once,
  time.Tick, time.After, goroutines and channels.

playgomodules
  Try out go modules described in https://blog.golang.org/using-go-modules.

webserver
  webserver demos how to start a simple HTTP server.

html
****

html5_base.html
  A barebone html5 file

jquery_mobile_base
  A skeleton of jquery mobile web application. You can decide to use the
  development version or the production version of the jquery libraries.

Python
******

cherrypy_web_server.py
  A simple web server using CherryPy. It's good for prototyping REST API.

func_with_test.py
  A template for creating functions or classes with unit tests. It's mainly
  used for rapid protyping several functions or classes.

playground_script.py
  A template to let you quickly insert code and try them out, with ipython at
  the end to explore stuff.

script_template.py
  A template for a python script. It has loggers and a parser boilerplate
  ready.

script_with_subparsers_template.py
  A template for a python script. It has loggers, a parser and subparsers
  boilerplate ready.

simple_no_logging_script_template.py
  A template for a python script without using the logging module. It has a
  parser boilerplate.

sqlalchemy_project
  A starter project for sqlalchemy.

web_client.py
  A simple command line web client. I know curl is the best, but doing it in
  python has much more fun!

Java
*****
app-configuration
  A way to solve loading of application properties at runtime by Johannes
  Brodwall. Source:
  http://www.javacodegeeks.com/2014/10/dead-simple-configuration.html
  
Javascript
**********
nodejs
======
simple-http-server
------------------
A sample of simple http server using httpdispatcher

Misc
****
This README follows the convention mentioned in
http://thomas-cokelaer.info/tutorials/sphinx/rest_syntax.html#headings and is
copied verbatim below::

  Normally, there are no heading levels assigned to certain characters as the
  structure is determined from the succession of headings. However, it is better
  to stick to the same convention throughout a project. For instance:

  # with overline, for parts
  * with overline, for chapters
  =, for sections
  -, for subsections
  ^, for subsubsections
  ", for paragraphs

Instead of differentiate betwee parts or sections, tho, I would just treat
them as h1, h2, h3..., so just use the symbols in that order.
