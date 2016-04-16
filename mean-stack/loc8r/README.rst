Loc8r
=====
A sample Node.js application using the mean stack - Mongo, Express, Angular,
Node. It is the sample application created by following the book "Getting MEAN
with Mongo, Express, Angular, and Node" by Simon Holmes.

Setup mongo database
---------------------
Before you run the application locally, make sure
you have a mongod running in your local host.
Connect to it and create a database Loc8r::

    $ mongo
    > use Loc8r;

Before you deploy the application to Heroku, you
should set up a mongo database in the cloud as well.
See the instruction in "Deploy the application"
section.

Run the application
-------------------
In this directory, run::

    $ npm start

Or if you have nodemon installed, simply run::

    $ nodemon

You can test if it works for Heroku by::

    $ heroku local

Deploy the application to Heroku
--------------------------------
You can deploy this to your heroku account as well.
Create an application in Heroku
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
First, run::

    $ heroku create [optionally put your app name here, or one will be generated]

That output something like::

     https://your-app-name.herokuapp.com/ | git@heroku.com:your-app-name.git
     Git remote heroku added

You can log in to your Heroku account to check the application. To let this
git repo to know your heroku account, run this::

    $ heroku git:remote -a your-app-name

Setup a mongo database
^^^^^^^^^^^^^^^^^^^^^^
Set up a mongo database. You can use mlab for it. After a mlab account is created,
just do the following::

    $ heroku config:set MONGODB_URI=your_db_uri

The format of the uri should be like
"mongodb://<user-name>:<password>@<host>.mlab.com:11261/<database>.

If instead, you create the mongo database through add-on in Heroku, you can run the following::

    $ heroku addons:add mongolab

Then you can confirm the existence of the mongo db by::

    $ heroku addons:open mongolab

Either way, you should be able to get the URI that is configured by::

    $ heroku config:get MONGODB_URI

I had problems with finding out the password for my mongodb
on mlab and I solved it by adding a new user with a new
password. Take note of the various components of the uri:
host, port, database, user and password.

Install mongo in your local machine if you don't have it.
Then you should be able to use the client to connect to the
mongo database in Heroku by::

    $ mongo <your_db_uri>

Add initial locations collection in it::

    $ mongorestore -h <host>:<port> -d <database> -u <user> -p <password> db_setup/Loc8r

In the mongo client, you can query the collections::

    > db.locations.find().pretty()

(Optional) Add more fake data to your mongo db
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
Add fake data to your mongo db with (you may want to change the content first)::

    $ mongo Loc8r db_setup/add_db_data.js

Push the application to Heroku
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
If this application is in the root directory of the git repository, you can
simply run::

    $ git push heroku master

Otherwise, like this application inside the http://github.com/hermantai/samples
repo, you have to run the following in the root directory::

    $ git subtree push --prefix mean-stack/loc8r heroku master

Protip: you can tail the heroku application log by::

    $ heroku logs -t

About the fake data used in the application
-------------------------------------------
The fake data are straightly from the book "Getting
MEAN with Mongo, Express, Angular, and Node".

You can get your current latitude and longitude from this website: http://www.where-am-i.net/.

You can geocode an address from the website http://www.latlong.net/convert-address-to-lat-long.html. or http://mygeoposition.com

Get your current coordinates in http://whatismylatlng.com
