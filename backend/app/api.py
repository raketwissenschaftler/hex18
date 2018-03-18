import urllib.request
import urllib.error
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
        return return_error(400, "Request has no payload")

    if request.json.get("facebook_token") is None:
        return return_error(400, "No facebook token provided")

    if request.json.get("device_id") is None:
        return return_error(400, "No device id provided")

    facebook_api_url = "https://graph.facebook.com/v2.12/me?fields=id&access_token="
    facebook_api_url += request.json.get("facebook_token")
    try:
        user_data = json.loads(urllib.request.urlopen(facebook_api_url).read().decode())
    except urllib.error.HTTPError:
        return return_error(403, "Invalid facebook token")

    user_with_current_id = User.query.filter(User.facebook_id == user_data["id"]).one_or_none()

    if user_with_current_id is None:
        user = User(
            facebook_token=request.json.get("facebook_token"),
            facebook_id=user_data["id"],
            device_id=request.json.get("device_id")
        )
    else:
        user = user_with_current_id
        user.facebook_id = user_data["id"]
        user.device_id = request.json.get("device_id")

    db.session.add(user)
    db.session.commit()

    return jsonify({"user_id": user.id})


@app.route(API_PREFIX + "profiles", methods=["POST"])
def get_profile_info():
    if request.json is None:
        return return_error(400, "Request has no payload")

    if request.json.get("ids") is None:
        return return_error(400, "Id's not included in request payload")

    if request.json.get("user_id") is None:
        return return_error(400, "No user id provided")

    print(request.json)

    user = User.query.get(request.json.get("user_id"))
    facebook_api_url = "https://graph.facebook.com/v2.12/me/friends?access_token="
    facebook_api_url += user.facebook_token
    print(facebook_api_url)
    user_data = json.loads(urllib.request.urlopen(facebook_api_url).read().decode())
    registered_friends = [friend["id"] for friend in user_data["data"]]

    users = []
    for device_id in request.json.get("ids"):
        user = User.query.filter(User.device_id == device_id).one_or_none()
        if user is not None:
            facebook_api_url = "https://graph.facebook.com/v2.12/me?fields=id%2Cname%2Cposts%2Cbirthday%2Ceducation%2Cinspirational_people&access_token="
            facebook_api_url += user.facebook_token
            user_data = json.loads(urllib.request.urlopen(facebook_api_url).read().decode())
            if user_data["id"] in registered_friends:
                user_data["facebook_id"] = user_data["id"]
                user_data["id"] = device_id
                user_data["facebook_url"] = "https://facebook.com/" + user_data["facebook_id"]
                user_data["image_url"] = "https://graph.facebook.com/" + user_data["facebook_id"] + "/picture?type=large"
                user_data["user_id"] = user.id
                user_data["description"] = user.description
                user_data["occupation"] = user.occupation
                users.append(user_data)
        else:
            print(device_id)
    return jsonify({"profiles": users})


@app.route(API_PREFIX + "addDescriptionForUser", methods=["POST"])
def add_description_for_user():
    if request.json.get("user_id") is None:
        return return_error(400, "No user id present")

    if request.json.get("description") is None:
        return return_error(400, "No description present")

    user = User.query.get(request.json.get("user_id"))

    user.description = request.json.get("description")

    db.session.add(user)

    db.session.commit()

    return jsonify({"message": "Description updated successfully"})


@app.route(API_PREFIX + "addOccupationForUser", methods=["POST"])
def add_occupation_for_user():
    if request.json.get("user_id") is None:
        return return_error(400, "No user id present")

    if request.json.get("occupation") is None:
        return return_error(400, "No occupation present")

    user = User.query.get(request.json.get("user_id"))

    user.occupation = request.json.get("occupation")

    db.session.add(user)

    db.session.commit()

    return jsonify({"message": "Occupation updated successfully"})


@app.route(API_PREFIX + "addInteractions", methods=["POST"])
def add_interactions():
    if request.json.get("interactions") is None:
        return return_error(400, "No interactions present")

    if request.json.get("user_id") is None:
        return return_error(400, "No user_id present")

    for interaction in request.json.get("interactions"):
        peer = User.query.filter(User.device_id == interaction["device_id"]).one()
        user = User.query.get(request.json.get("user_id"))

        inter = Interaction(
            coordinates=interaction["coordinates"],
            timestamp=interaction["timestamp"],
            user_id=user.id,
            peer_id=peer.id
        )

        db.session.add(inter)
        db.session.commit()

    return jsonify({"message": "Added interactions successfully"})

