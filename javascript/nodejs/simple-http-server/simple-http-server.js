/*
 * from http://blog.modulus.io/build-your-first-http-server-in-nodejs
 * The blog post is missing the calling of setStaticDirname
 */

var http = require('http');
var dispatcher = require('httpdispatcher');

const PORT=9090;

function handleRequest(request, response){
  try {
    //log the request on console
    console.log("Request: " + request.url);
    //Disptach
    dispatcher.dispatch(request, response);
  } catch(err) {
    console.log(err);
  }
}

dispatcher.setStaticDirname('.');
dispatcher.setStatic('css');

dispatcher.onGet("/", function(req, res) {
  res.writeHead(200, {'Content-Type': 'text/html'});
  require('fs').readFile('index.html', function (err, html) {
    if (err) {
      throw err;
    }
    res.end(html);
  });
});

dispatcher.onGet("/page1", function(req, res) {
  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end('Page One');
});

dispatcher.onPost("/post1", function(req, res) {
  res.writeHead(200, {'Content-Type': 'text/plain'});
  res.end('Got Post Data');
});

var server = http.createServer(handleRequest);

server.listen(PORT, function(){
  // Callback triggered when server is successfully listening
  console.log("Server listening on: http://localhost:%s", PORT);
});
