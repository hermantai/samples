import cgi
import MySQLdb
import webapp2

from sqlalchemy import create_engine
from sqlalchemy import Column, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker


CLOUD_SQL_INSTANCE_NAME = "FULL_CLOUD_SQL_INSTANCE_NAME"
Base = declarative_base()
connect_string = "mysql+mysqldb://root@/db1?unix_socket=/cloudsql/%s" % (
    CLOUD_SQL_INSTANCE_NAME
)
engine_with_pool = create_engine(
    connect_string,
    pool_size=10,
    max_overflow=0,
    pool_timeout=5,
)
Base.metadata.bind = engine_with_pool


def setup_db():
    db = MySQLdb.connect(
        unix_socket="/cloudsql/%s" % CLOUD_SQL_INSTANCE_NAME,
        user="root",
        db="db1",
    )
    cursor = db.cursor()
    cursor.execute("create database if not exists db1 character set 'utf8'")
    cursor.execute("create table if not exists table1(name varchar(255))")
    cursor.execute("truncate table table1")
    cursor.execute("insert into table1 values('data1')")
    db.commit()
    return db


class Data(Base):
    __tablename__ = "table1"
    name = Column(String(250), primary_key=True)


class MainPage(webapp2.RequestHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/html'
        self.response.write("<html><head><title>Hello</title></head>")
        self.response.write("<body>")
        self.response.write('<p>Hello, World!</p>')
        self.response.write('<p><a href="/cloud-sql">Cloud SQL (list the tables)</a></p>')
        self.response.write('<p><a href="/cloud-sql-leak-connections?n=3">Cloud SQL (leaking connections but under 12 connections leaked, so work as expected)</a></p>')
        self.response.write('<p><a href="/cloud-sql-leak-connections">Cloud SQL (leak over 12 connections, so "Cloud SQL socket open failed with error: No such file or directory")</a></p>')
        self.response.write('<p><a href="/cloud-sql-pooling">Cloud SQL with SQLAlchemy (leak over 12 connections, with pooling, so you get a timeout instead )</a></p>')
        self.response.write('<p><a href="/cloud-sql-pooling?n=5">Cloud SQL with SQLAlchemy (leak under 12 connections, with pooling, work as expected)</a></p>')
        self.response.write('<p><a href="/cloud-sql-pooling-orm">Cloud SQL with SQLAlchemy ORM (leak over 12 connections, with pooling, so you get a timeout instead )</a></p>')
        self.response.write('<p><a href="/cloud-sql-pooling-orm?n=5">Cloud SQL with SQLAlchemy ORM (leak under 12 connections, with pooling, work as expected)</a></p>')
        self.response.write("</body></html>")


class CloudSQLPage(webapp2.RequestHandler):
    def get(self):
        db = setup_db()
        cursor = db.cursor()
        cursor.execute("show tables")

        tables = [cgi.escape(row[0]) for row in cursor.fetchall()]
        db.close()
        self.response.headers['Content-Type'] = "text/html"
        self.response.write('<table>\n')
        self.response.write('<tr><th>Tables</th></tr>')
        for table in tables:
            self.response.write('<tr><td>{table}</td></tr>'.format(**locals()))
        self.response.write('</table>\n')


class CloudSQLLeakConnectionsPage(webapp2.RequestHandler):
    def get(self):
        db = setup_db()

        n = self.request.get('n')
        if n:
            n = int(n)
        else:
            n = 20

        # need to store the cursor somewhere, otherwise the garbage collection
        # cleans the cursor up and we cannot "leak" connections
        garbages = []
        for i in range(n):
            cursor = db.cursor()
            cursor.execute("select * from table1")
            garbages.append(cursor)

        values = [cgi.escape(row[0]) for row in cursor.fetchall()]
        self.response.headers['Content-Type'] = "text/html"
        self.response.write('<p>n = %s</p>' % n)

        self.response.write('<table>\n')
        self.response.write('<tr><th>Values</th></tr>')
        for value in values:
            self.response.write('<tr><td>{value}</td></tr>'.format(**locals()))
        self.response.write('</table>\n')


class CloudSQLWithPoolingPage(webapp2.RequestHandler):
    def get(self):
        setup_db()

        n = self.request.get('n')
        if n:
            n = int(n)
        else:
            n = 20

        self.response.headers['Content-Type'] = "text/html"
        try:
            # need to store the session somewhere, otherwise the garbage
            # collection cleans the session up and we cannot "leak" connections
            garbages = []
            values = []
            for i in range(n):
                conn = engine_with_pool.connect()
                cursor = conn.execute("select * from table1")
                # This line will cause the same
                # "Cloud SQL socket open failed with error: No such file or directory"
                # error even we are using SQLAlchemy's pooling here. TODO
                garbages.append(cursor)

            self.response.write('<p>n = %s</p>' % n)

            self.response.write('<table>\n')
            self.response.write('<tr><th>Values</th></tr>')
            values = [cgi.escape(row[0]) for row in cursor.fetchall()]
            for value in values:
                self.response.write(
                        '<tr><td>{value}</td></tr>'.format(**locals()))
            self.response.write('</table>\n')
        except Exception as err:
            self.response.write("<p>%s</p>" % err)


class CloudSQLWithPoolingORMPage(webapp2.RequestHandler):
    def get(self):
        setup_db()

        n = self.request.get('n')
        if n:
            n = int(n)
        else:
            n = 20

        DBSession = sessionmaker(bind=engine_with_pool)

        self.response.headers['Content-Type'] = "text/html"
        try:
            # need to store the session somewhere, otherwise the garbage
            # collection cleans the session up and we cannot "leak" connections
            garbages = []
            values = []
            for i in range(n):
                session = DBSession()
                values = [d.name for d in session.query(Data).all()]
                garbages.append(session)

            self.response.write('<p>n = %s</p>' % n)

            self.response.write('<table>\n')
            self.response.write('<tr><th>Values</th></tr>')
            for value in values:
                self.response.write(
                        '<tr><td>{value}</td></tr>'.format(**locals()))
            self.response.write('</table>\n')
        except Exception as err:
            self.response.write("<p>%s</p>" % err)


app = webapp2.WSGIApplication([
    ('/', MainPage),
    ('/cloud-sql', CloudSQLPage),
    ('/cloud-sql-leak-connections', CloudSQLLeakConnectionsPage),
    ('/cloud-sql-pooling', CloudSQLWithPoolingPage),
    ('/cloud-sql-pooling-orm', CloudSQLWithPoolingORMPage),
], debug=True)
