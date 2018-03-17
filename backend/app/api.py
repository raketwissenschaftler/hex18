import urllib.request
import json
from app import app, db
from .models import User, Interaction
from flask import jsonify, abort, request


API_PREFIX = "/api/v1/"


def return_error(code, message):
    response = jsonify({
        "message": message
    })
    response.status_code = code
    return response


@app.route(API_PREFIX + "addUser", methods=["POST"])
def add_user():
    if request.json is None:
        return_error(400, "Request has no payload")

    if request.json.get("facebook_token") is None:
        return_error(400, "No facebook token provided")

    user = User(facebook_token=request.json.get("facebook_token"))

    db.session.add(user)
    db.session.commit()

    return jsonify({"user_id": user.id })


@app.route(API_PREFIX + "profiles", methods=["POST"])
def get_profile_info():
    if request.json is None:
        return_error(400, "Request has no payload")

    users = []
    for user_id in request.json.get("ids"):
        user = User.query.filter(User.id == user_id).one()
        facebook_api_url = "https://graph.facebook.com/v2.12/me?fields=id%2Cname%2Cposts%2Cbirthday%2Ceducation%2Cinspirational_people&access_token="
        facebook_api_url += user.facebook_token
        user_data = json.loads(urllib.request.urlopen(facebook_api_url).read().decode())
        user_data["facebook_id"] = user_data["id"]
        user_data["id"] = user_id
        user_data["facebook_url"] = "https://facebook.com/" + user_data["facebook_id"]
        users.append(user_data)

    return jsonify(users)

