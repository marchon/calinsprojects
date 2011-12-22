from message_codes import *
import json

def json_response(msg_id, content=None):
    jsn = {
        "type": m[msg_id][0],
        "message": m[msg_id][1],
        "content": object_to_json(content),
    }
    return json.dumps(jsn)


def object_to_json(content):
    if content is None: return None

    if hasattr(content, 'to_dict'):
        return content.to_dict()

    if type(content) is list:
        lst = []
        for obj in content:
            lst.append(object_to_json(obj))
        return lst

    if type(content) is dict:
        dct = {}
        for key in content:
            dct[object_to_json(key)] = object_to_json(content[key])
        return dct

    return content