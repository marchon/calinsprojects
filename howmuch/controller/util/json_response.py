import json

__author__ = 'Calin'

#TODO: parameters
def json_response(msg_id, content=None):
    return json.dumps({
        'type': m[msg_id][0],
        'message': m[msg_id][1],
        'content': content,
        })

NOT_LOGGED_IN = 1
BAD_REQUEST = 2
ENT_NOT_SUPPORTED = 3
OP_NOT_SUPPORTED = 4
OP_END_SUCCESS = 5
OP_BAD_VALUE = 6
OP_DS_FAILED = 7
EX_PROCESS_DATA = 8
TRANS_INCOMPLETE_ABORT = 9

SUC = 'success'
ERR = 'error'
FAT = 'fatal'

m = {
    NOT_LOGGED_IN: [ERR, 'You have to be logged in to perform this operation.'],
    BAD_REQUEST: [ERR, 'The request is not well formatted.'],
    ENT_NOT_SUPPORTED: [ERR, 'This entity is not supported.'],
    OP_NOT_SUPPORTED: [ERR, 'This operation is not supported.'],
    OP_END_SUCCESS: [SUC, 'Operation has been performed with success.'],
    OP_BAD_VALUE: [ERR, 'Bad value: {1}'],
    OP_DS_FAILED: [FAT, 'Failed to save to datastore.'],
    EX_PROCESS_DATA: [ERR, 'Exception while processing data.'],
    TRANS_INCOMPLETE_ABORT: [ERR, 'Incomplete transaction: {1}. Aborting import.']
}
