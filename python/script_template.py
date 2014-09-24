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
# logger-related functions
#

def setup_logger(
    logger,
    debug=False,
    stdout=False,
    log_to_file=None,
    add_thread_id=False,
    logging_level=None,
    use_path=False,
):
    if logging_level is None:
        if debug:
            logging_level = logging.DEBUG
        else:
            logging_level = logging.INFO

    formatter = create_logging_formatter(
        add_thread_id=add_thread_id,
        use_path=use_path
    )
    hdlr = create_stream_handler(
        formatter=formatter,
        stdout=stdout,
    )
    logger.handlers = []  # clear the existing handlers
    logger.addHandler(hdlr)
    logger.setLevel(logging_level)

    if log_to_file is not None:
        init_command = 'mkdir -p %s' % os.path.dirname(log_to_file)

        subprocess.check_call(init_command, shell=True)

        hdlr = TimedRotatingFileHandler(
            log_to_file,
            when="midnight",
            interval=1,
        )
        hdlr.setFormatter(formatter)
        logger.addHandler(hdlr)


def create_plain_logger(logger_name, stdout=True):
    plain_logger = logging.getLogger(logger_name)
    plain_logger.propagate = False
    plain_logger.setLevel(logging.INFO)

    if stdout:
        out = sys.stdout
    else:
        out = sys.stderr

    handler = logging.StreamHandler(stream=out)
    handler.setFormatter(
        logging.Formatter(
            fmt="%(message)s",
            datefmt="%Y",  # does not matter
        )
    )

    plain_logger.addHandler(handler)
    return plain_logger


def create_logging_formatter(add_thread_id=False, use_path=False):
    format_str = "%(asctime)s"

    if add_thread_id:
        format_str += " thread:%(thread)s"

    format_str += " %(levelname)s "

    if use_path:
        format_str += "%(pathname)s"
    else:
        format_str += "%(name)s"

    format_str += ":%(lineno)s: %(message)s"

    detail_formatter = logging.Formatter(
        fmt=format_str,
        datefmt="%Y-%m-%d %H:%M:%S"
    )
    return detail_formatter


def create_stream_handler(formatter=None, stdout=False):
    if formatter is None:
        formatter = create_logging_formatter()

    if stdout:
        stream = sys.stdout
    else:
        stream = sys.stderr

    hdlr = logging.StreamHandler(stream=stream)
    hdlr.setFormatter(formatter)
    return hdlr

#
# end of logger-related functions
#

def parse_args(cmd_args):
    description = """
Put some description of the script here
    """
    parser = argparse.ArgumentParser(
        description=description,
        formatter_class=argparse.RawDescriptionHelpFormatter,
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

    setup_logger(_LOG, debug=args.debug)
    _PLAIN_LOGGER = create_plain_logger("PLAIN")

    app = App(
    )
    app.run()


if __name__ == '__main__':
    main()
