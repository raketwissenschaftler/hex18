from app import db
from uuid import uuid4


def generate_uuid():
    return str(uuid4())


class User(db.Model):
    __tablename__ = "users"

    id = db.Column(db.String(36), primary_key=True, default=generate_uuid)

    facebook_token = db.Column(db.String(512), unique=True, index=True)

    facebook_id = db.Column(db.String(200), unique=True)

    device_id = db.Column(db.String(20))

    description = db.Column(db.String(1000))

    occupation = db.Column(db.String(100))


class Interaction(db.Model):
    __tablename__ = "interactions"

    id = db.Column(db.String(36), primary_key=True, default=generate_uuid)

    coordinates = db.Column(db.String(100), index=True)

    timestamp = db.Column(db.DateTime)