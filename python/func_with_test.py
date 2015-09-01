#!/usr/bin/env python
"""A template for creating functions or classes with unit tests.
It's mainly used for rapid protyping several functions or classes.

Run this template with:
    python func_with_test.py
or
    python func_with_test.py -v
"""
from __future__ import print_function
from __future__ import absolute_import
from __future__ import division
from __future__ import unicode_literals

import unittest


class ClassA(object):
    pass


def func(a, b):
    return a + b


class ClassATestCase(unittest.TestCase):
    pass


# To test functions in the module, I prefer having a single TestCase using the
# module name.
class FuncWithTestTestCase(unittest.TestCase):
    def test_func_1And2_3(self):
        self.assertEqual(3, func(1, 2))


if __name__ == '__main__':
    unittest.main()
