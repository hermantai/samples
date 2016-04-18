var mongoose = require('mongoose');
require('./locations');

var dbURI = "mongodb://localhost/Loc8r";
if (process.env.NODE_ENV === 'production') {
  // Heroku should have environment variable
  // MONGODB_URI defined if you go through the
  // add-on mongolab. Otherwise, you can set it
  // through "heroku config:set MONGODB_URI". See
  // "README.rst" for detail.
  //
  // If you don't use Heroku, just set this
  // environment variable somehow in your
  // production environment...
  if (!process.env.MONGODB_URI) {
    throw "Env variable MONGODB_URI not defined";
  }
  dbURI = process.env.MONGODB_URI;
}

var options = {
  server: { socketOptions: { keepAlive: 300000, connectTimeoutMS: 30000 } },
  replset: { socketOptions: { keepAlive: 300000, connectTimeoutMS : 30000 } }
};
mongoose.connect(dbURI, options);

mongoose.connection.on(
  'error',
  console.error.bind(console, 'connection error:')
);

mongoose.connection.on('connected', function() {
  console.log("Mongoose connected to " + dbURI);
});

mongoose.connection.on('error', function(err) {
  console.log("Mongoose connection error: " + err);
});

mongoose.connection.on('disconnected', function() {
  console.log("Mongoose disconnected");
});

var gracefulShutdown = function(msg, callback) {
  mongoose.connection.close(function() {
    console.log("Mongoose disconnected through " + msg);
    callback();
  });
};

// Capture it once only, so the second SIGUSR2, emited in process.kill in the
// callback, can be captured by nodemon to restart the app.
process.once("SIGUSR2", function() {
  gracefulShutdown("nodemon restart", function() {
    process.kill(process.pid, "SIGUSR2");
  });
});

process.on("SIGINT", function() {
  gracefulShutdown("app termination", function() {
    process.exit(0);
  });
});

process.on("SIGTERM", function() {
  gracefulShutdown("heroku app shutdown", function() {
    process.exit(0);
  });
});
