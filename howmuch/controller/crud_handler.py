from google.appengine.ext import webapp
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext.db import TransactionFailedError, BadKeyError
from model.model import Category, Transaction
import json


crud_entities = {
    'Category': Category,
    'Transaction': Transaction
}

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
    OP_DS_FAILED: [FAT, 'Failed to save to datastore.']
}

class CrudHandler(webapp.RequestHandler):
    """
        REQUEST:

        {
            "ent" : entity_name
            "op" : "put" | "del" | "lst",
            "params": put_params | del_params | lst_params
        }

        put_params = [{"key": ?, "attr1": ?, "attr2": ?, ...}, ...] - to create, just omit "id"
        del_params = [key1, key2, ...]
        lst_params = {"offset" : ..., "limit": ..., "filter":{}}


        RESPONSE:

        {
            "code": "success" | "error"
            "message" : sone_string
            "content" : null | {"hasNext": true | false, [selected_entities]} - just for "lst" op
        }
    """

    def post(self):
        #TODO: security???
        user = users.get_current_user()

        if not user:
            self.response.out.write(json_response(NOT_LOGGED_IN))
            return

        j_str = self.request.get('json')

        try:
            json_r = json.loads(j_str)
        except ValueError:
            self.response.out.write(json_response(BAD_REQUEST))
            return

        ent = json_r['ent']
        op = json_r['op']
        params = json_r['params']

        if not ent or not op or not params:
            self.response.out.write(json_response(BAD_REQUEST))
            return

        try:
            ent = crud_entities[ent]
        except KeyError:
            self.response.out.write(json_response(ENT_NOT_SUPPORTED))
            return

        ## execute operation ##
        if op == 'put':
            try:
                entities = []
                for cat_j in params:
                    entities.append(ent.from_dict(cat_j))
                db.put(entities)

                self.response.out.write(json_response(OP_END_SUCCESS))
            except ValueError:
                self.response.out.write(json_response(OP_BAD_VALUE))
            except TransactionFailedError:
                self.response.out.write(json_response(OP_DS_FAILED))

        elif op == 'del':
            try:
                db.delete(params)
                self.response.out.write(json_response(OP_END_SUCCESS))
            except BadKeyError:
                self.response.out.write(json_response(OP_BAD_VALUE))
            except TransactionFailedError:
                self.response.out.write(json_response(OP_DS_FAILED))

        elif op == 'lst':
            offset = params['offset']
            limit = params['limit']
            #            filter = params['filter']

            q = ent.all_query()
            #filter

            #fetch an extra one to see if new pages have sense to be requested
            rs = q.fetch(limit=limit + 1, offset=offset)

            l = len(rs)
            if l <= limit: has_next = False
            else:
                has_next = True
                rs.pop()

            res = []
            for e in rs:
                res.append(ent.to_dict(e))

            self.response.out.write(json_response(OP_END_SUCCESS, {
                'result': res,
                'hasNext': has_next
            }))
        else:
            self.response.out.write(json_response(ENT_NOT_SUPPORTED))