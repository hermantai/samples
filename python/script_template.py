#!/usr/bin/env python
"""
script_template.py

A template for creating python scripts. It has a logger _LOG being ready.
Another _PLAIN_LOGGER for text without formatting. The reason of using a plain
logger instead of "print" is for skipping buffer, which can be an issue if
you redirect stdout to a file. Notice that _LOG uses stderr while
_PLAIN_LOGGER uses stdout by default.

Command line options of the script should be added to the parser of the
function parse_args.

Dependencies:
    sorno-py-lib - https://github.com/hermantai/sorno-libs/tree/master/sorno-py-lib


    Copyright 2014 Herman Tai

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

import logging

import sys
import subprocess

from sorno import logging as sorno_logging


_LOG = logging.getLogger(__name__)
_PLAIN_LOGGER = None  # will be created in main()


class App(object):
    def __init__(
        self,
    ):
        pass

    def run(self):
        _LOG.info("hello world")
        _LOG.debug("hello world")
        _PLAIN_LOGGER.info("plain hello world")

    def _run_cmd(self, cmd):
        _LOG.info(cmd)
        if isinstance(cmd, list):
            use_shell = False
        else:
            use_shell = True
        return subprocess.check_call(cmd, shell=use_shell)

#
# end of logger-related functions
#

def parse_args(cmd_args):
    description = """
Put some description of the script here
    """
    parser = argparse.ArgumentParser(
        description=description,
        # formatter_class=argparse.RawDescriptionHelpFormatter,
    )
    parser.add_argument(
        "--debug",
        action="store_true",
    )

    args = parser.parse_args(cmd_args)
    return args


def main():
    global _PLAIN_LOGGER

    args = parse_args(sys.argv[1:])

    sorno_logging.setup_logger(_LOG, debug=args.debug)
    _PLAIN_LOGGER = sorno_logging.create_plain_logger("PLAIN")

    app = App(
    )
    app.run()


if __name__ == '__main__':
    main()
