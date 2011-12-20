from message_codes import *
import json

def json_response(msg_id, content=None, info=None):
	jsn = {
		"type": m[msg_id][0],
		"message": m[msg_id][1],
		"content": object_to_json(content),
		"info": info
	}
	return json.dumps(jsn)
	
def object_to_json(content):
	if content is None: return None
	
	if hasattr(content, 'json'): return content.json()
	
	if type(content) is list:
		lst = []
		for obj in content:
			lst.append(object_to_json(obj))
		return lst
		
	return None