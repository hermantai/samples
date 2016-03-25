A demo of `Google Cloud SQL`_ on `Google App Engine`_ with `SQLAlchemy
pooling`_ to solve the limit of `12 concurrent connections`_ per Google App
Engine instance. You should always close your database connections as soon as
you are finished with your query. This pooling solution is to convert a fatal
error comes from going over the limit of 12 concurrent Cloud SQL connections
into a delay of your application.

To deploy this demo:

1. Requirements:

   a. Have a `Google App Engine project`_
   b. Have a `Google Cloud SQL first generation instance`_. Create a database
      called "db1" in that instance.
   c. Have `Google App Engine SDK for Python`_ installed (which has the
   appcfg.py) command line tool.
   d. Have `pip`_ installed.

2. In this directory, create a subdirectory "lib"::

    mkdir lib

3. Install a vendor library SQLAlchemy::

    pip install -t lib sqlalchemy

4. Replace the placeholder in this line in the file ``helloworld.py`` with your
   Google Cloud SQL instance name, e.g. "my-org:instance1"::

    CLOUD_SQL_INSTANCE_NAME = "FULL_CLOUD_SQL_INSTANCE_NAME"

5. Run the following command in this directory to deploy this demo to your
   Google App Engine project::

    appcfg.py -A YOUR_PROJECT_ID -V 1 update .

6. After that, you should be able to see the demo in
   http://YOUR_PROJECT_ID.appspot.com.

7. Click on "Cloud SQL (setup the table and data)" to setup the table and data.

8. You can now click on other links in the demo to see the difference between
   using pooling vs not using pooling.

.. _`Google Cloud SQL`: https://cloud.google.com/sql
.. _`Google App Engine`: https://cloud.google.com/appengine/
.. _`SQLAlchemy pooling`: http://docs.sqlalchemy.org/en/latest/core/pooling.html
.. _`12 concurrent connections`: https://cloud.google.com/sql/faq
.. _`Google App Engine SDK for Python`: https://cloud.google.com/appengine/downloads#Google_App_Engine_SDK_for_Python
.. _`Google Cloud SQL first generation instance`: https://cloud.google.com/sql/docs/getting-started#create
.. _`Google App Engine project`: https://console.cloud.google.com/
.. _`pip`: https://pypi.python.org/pypi/pip
