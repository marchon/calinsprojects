from message_codes import *

def json(msg_id, content=None, ops=None):
	return '{type:' + str(m[msg_id][0]) + ',message:"' + m[msg_id][1] + '",content:' + dump_object_to_json(content, ops) + '}'
	
def dump_object_to_json(content, ops):
	return '{}'