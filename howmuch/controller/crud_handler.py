from google.appengine.ext import webapp
from google.appengine.ext import db
from google.appengine.api import users
from google.appengine.ext.db import Error, BadKeyError, BadValueError
from controller.util.json_response import *
from model import counter
from model.model import Category, Transaction
import json

crud_entities = {
    'Category': Category,
    'Transaction': Transaction
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
            "content" : null | {"total  ": ?, "result": [selected_entities]} - just for "lst" op
        }
    """

    def post(self):
        #TODO: security???
        #TODO: logging
        #TODO: test ex handling
        user = users.get_current_user()

        if not user:
            self.response.out.write(json_response(NOT_LOGGED_IN))
            return

        j_str = self.request.get('json')

        try:
            json_r = json.loads(j_str)
            ent_name = json_r['ent']
            op = json_r['op']
            params = json_r['params']
        except (ValueError, KeyError):
            self.response.out.write(json_response(BAD_REQUEST))
            return

        try:
            ent_class = crud_entities[ent_name]
        except KeyError:
            self.response.out.write(json_response(ENT_NOT_SUPPORTED))
            return

        ## execute operation ##
        if op == 'put':
            try:
                entities = []
                new_amount = 0

                for cat_j in params:
                    if cat_j.has_key('key'):
                        entity =  ent_class.load(cat_j)
                    else:
                        entity = ent_class.create(cat_j)
                        new_amount += 1

                    entities.append(entity)

                db.put(entities)
                counter.update_counter(ent_name, new_amount)

                self.response.out.write(json_response(OP_END_SUCCESS))
            except (KeyError, ValueError, BadValueError):
                self.response.out.write(json_response(OP_BAD_VALUE))
            except Error:
                self.response.out.write(json_response(OP_DS_FAILED))

        elif op == 'del':
            try:
                db.delete(params)
                counter.update_counter(ent_name, -len(params))
                self.response.out.write(json_response(OP_END_SUCCESS))
            except BadKeyError:
                self.response.out.write(json_response(OP_BAD_VALUE))
            except Error:
                self.response.out.write(json_response(OP_DS_FAILED))

        elif op == 'lst':
            offset = params['offset']
            limit = params['limit']
            #filter = params['filter']

            q = ent_class.query()
            #filter

            rs = q.fetch(limit=limit, offset=offset)

            res = []
            for e in rs:
                res.append(ent_class.serialize(e))

            _counter = counter.get_counter(ent_name)
            self.response.out.write(json_response(OP_END_SUCCESS, {
                'result': res,
                'total': _counter.count if _counter is not None else 0
            }))
        else:
            self.response.out.write(json_response(ENT_NOT_SUPPORTED))