#!/usr/bin/env python
"""script_with_subparsers_template.py

A template for creating python scripts with subparsers. After parsing of the
command line arguments through the function parse_args, the whole application
control goes to the class App. Each subparser is handled by a
"<subcommand>_action" method of the App class. Usually, that method calls
another "<subcommand>" method to do the actual work. An example is a script
called "git" with subcommands like "branch", "commit", etc. There would be
branch_action, commit_action ... methods to handle each subcommand. They then
use the methods "branch" or "commit" to do the actual work. The idea is that
the methods without the word "_action" should be reusable outside of this
console application (the script). That means those methods should have minimum
"user-friendly" printing, prompting, etc.

The template has a logger _log being ready. Another _plain_logger for text
without formatting. The reason of using a plain logger instead of "print" is
for skipping bufferng, which can be an issue if you redirect stdout to a file.
Notice that _log uses stderr while _plain_logger uses stdout by default.
_plain_error_logger is similar to _plain_logger but uses stderr.

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
import subprocess
import sys

from sorno import logging as sorno_logging


_log = logging.getLogger(__name__)
_plain_logger = None  # will be created in main()
_plain_error_logger = None  # will be created in main()


class App(object):
    def __init__(
        self,
    ):
        pass

    def action1_action(self, args):
        self.action1()
        return 0

    def action1(self):
        _log.info("action1")
        _log.debug("action1")
        _plain_logger.info("plain action1")

    def action2_action(self, args):
        self.action2()
        return 1

    def action2(self):
        _log.info("action2")
        _log.debug("action2")
        _plain_logger.info("plain action2")

    def _run_cmd(self, cmd):
        _log.info(cmd)
        if isinstance(cmd, list):
            use_shell = False
        else:
            use_shell = True
        return subprocess.check_call(cmd, shell=use_shell)


def parse_args(app_obj, cmd_args):
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

    subparsers = parser.add_subparsers(
        title="Subcommands",
        description="Some description for subcommands",
    )

    parser_action1 = subparsers.add_parser(
        "action1",
        help="Help for action1",
        description="Description for action1",
    )
    parser_action1.add_argument(
        "files",
        nargs="*",
        help="Take action1 on files",
    )
    parser_action1.set_defaults(func=app_obj.action1_action)

    parser_action2 = subparsers.add_parser(
        "action2",
        help="Help for action2",
        description="Description for action2",
    )
    parser_action2.add_argument(
        "files",
        nargs="*",
        help="Take action2 on files",
    )
    parser_action2.set_defaults(func=app_obj.action2_action)

    args = parser.parse_args(cmd_args)
    return args


def main():
    global _plain_logger, _plain_error_logger

    app = App()
    args = parse_args(app, sys.argv[1:])

    sorno_logging.setup_logger(_log, debug=args.debug)
    _plain_logger = sorno_logging.create_plain_logger(
        "PLAIN",
        debug=args.debug,
    )
    _plain_error_logger = sorno_logging.create_plain_logger(
        "PLAIN_ERROR",
        debug=args.debug,
        stdout=False,
    )
    code = args.func(args)
    if code is not None:
        sys.exit(code)


if __name__ == '__main__':
    main()
