Loc8r
=====
A sample Node.js application using the mean stack - Mongo, Express, Angular,
Node. It is the sample application created by following the book "Getting MEAN
with Mongo, Express, Angular, and Node" by Simon Holmes.

Run the application
-------------------
In this directory, run::

    $ npm start

Or if you have nodemon installed, simply run::

    $ nodemon

You can test if it works for Heroku by::

    $ heroku local

Deply the application
---------------------
You can deploy this to your heroku account as well. First, run::

    $ heroku create [optionally put your app name here, or one will be generated]

That output something like::

     https://your-app-name.herokuapp.com/ | git@heroku.com:your-app-name.git
     Git remote heroku added

You can log in to your Heroku account to check the application. To let this
git repo to know your heroku account, run this::

    $ heroku git:remote -a your-app-name

If this application is in the root directory of the git repository, you can
simply run::

    $ git push heroku master

Otherwise, like this application inside the http://github.com/hermantai/samples
repo, you have to run the following in the root directory::

    $ git subtree push --prefix mean-stack/loc8r heroku master

About the fake data used in the application
-------------------------------------------
They are straightly from the book "Getting MEAN with Mongo, Express, Angular,
and Node".

You can get your current latitude and longitude from this website: http://www.where-am-i.net/.
You can geocode an address from the website http://www.latlong.net/convert-address-to-lat-long.html.
