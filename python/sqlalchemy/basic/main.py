from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals

import argparse

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

import models


def print_out(obj):
    print("\n\n", ">>> ", obj, "\n\n", sep="")


def print_header(msg):
    print("\n\n", "{0:=^80}".format(" " + msg + " "), "\n\n", sep="")


def parse_args():
    parser = argparse.ArgumentParser()
    parser.add_argument(
        "--interactive",
        action="store_true",
        help="Start an ipython session immediately after adding some objects",
    )

    return parser.parse_args()

if __name__ == '__main__':
    args = parse_args()

    engine = create_engine("sqlite://", echo=True)
    models.Base.metadata.create_all(bind=engine)

    Session = sessionmaker(bind=engine)

    # add some objects
    print_header("Add a user and its profile")
    session1 = Session()
    user1 = models.User(username="user1")
    profile1 = models.Profile(
        firstname="peter",
        age=12,
        user=user1,
    )
    session1.add(user1)
    session1.commit()

    session1.close()

    # query objects and related objects
    print_header("Query the user and the profile")
    session2 = Session()
    print_out(session2.query(models.User).all())
    print_out(session2.query(models.Profile).all())
    print_out(session2.query(models.User).first().profile)
    print_out(session2.query(models.Profile).one().user)
    session2.close()

    # update an object
    print_header("Update user1 with a new username")
    session3 = Session()
    user1.username = "newname"
    session3.add(user1)
    session3.commit()
    print_out(session3.query(models.User).all())
    session3.close()

    if args.interactive:
        from IPython import embed
        embed()
