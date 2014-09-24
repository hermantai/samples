from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
from __future__ import unicode_literals

import models

from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker


def print_out(obj):
    print("\n\n", ">>> ", obj, "\n\n", sep="")


if __name__ == '__main__':
    engine = create_engine("sqlite://", echo=True)
    models.Base.metadata.create_all(bind=engine)

    Session = sessionmaker(bind=engine)

    # add some objects
    session1 = Session()
    user1 = models.User(username="user1")
    session1.add(user1)
    session1.commit()

    profile1 = models.Profile(
        firstname="peter",
        age=12,
        user=user1,
    )
    session1.add(profile1)
    session1.commit()

    session1.close()

    # query objects and related objects
    session2 = Session()
    print_out(session2.query(models.User).all())
    print_out(session2.query(models.Profile).all())
    print_out(session2.query(models.User).first().profile)
    print_out(session2.query(models.Profile).one().user)
    session2.close()

    # update an object
    session3 = Session()
    user1.username = "newname"
    session3.add(user1)
    session3.commit()
    print_out(session3.query(models.User).all())
    session3.close()
