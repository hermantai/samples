from sqlalchemy import (
    Column,
    ForeignKey,
    String,
    Integer,
)
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import backref, relationship

Base = declarative_base()


class ReprMixin(object):
    def __repr__(self):
        properties = [
            "{0}={1}".format(k, repr(v))
            for k, v in vars(self).iteritems()
            if not k.startswith("_")
        ]
        property_str = ", ".join(properties)

        return "{0}({1})".format(
            self.__class__.__name__,
            property_str,
        )


class User(Base, ReprMixin):
    __tablename__ = "user"

    id = Column(Integer, primary_key=True)
    username = Column(String(200), nullable=False, unique=True)


class Profile(Base, ReprMixin):
    __tablename__ = "profile"

    id = Column(Integer, primary_key=True)
    firstname = Column(String(100))
    age = Column(Integer)
    user_id = Column(Integer, ForeignKey(User.id), index=True)

    user = relationship(
        User,
        backref=backref(
            "profile",
            uselist=False,
        ),
    )
