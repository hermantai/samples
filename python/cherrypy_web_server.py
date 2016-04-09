#!/usr/bin/env python
"""A simple web server using CherryPy. It's good for prototyping REST API.

Synopsis:
    cherrypy_web_server.py

   Copyright 2016 Heung Ming Tai

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
"""
from __future__ import print_function
from __future__ import absolute_import
from __future__ import division
from __future__ import unicode_literals

import argparse
import cherrypy


class HelloWorld(object):
    @cherrypy.expose
    def index(self):
        return "Hello World!"

    @cherrypy.expose
    def echo(self, se=None):
        return se

def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "-p",
        "--port",
        type=int,
        default=8080,
        help="Port number of the server",
    )
    parser.add_argument(
        "-h",
        "--host",
        default="localhost",
        help="The host that the server binds to",
    )
    return parser.parse_args()

if __name__ == '__main__':
    args = parse_args()
    cherrypy.config.update(
        {
            'server.socket_port': args.port,
            'server.socket_host': args.host,
        }
    )
    cherrypy.quickstart(HelloWorld())
