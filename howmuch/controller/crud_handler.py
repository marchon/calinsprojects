from google.appengine.ext import webapp
from google.appengine.ext import db
from google.appengine.api import users
from model.model import entities
from util.json_helper import json_response
from util.message_codes import *
import json

class CrudHandler(webapp.RequestHandler):
    """
        The request has the following json format:

        {
            "ent" : entity_name
            "op" : "put" | "del" | "lst",
            "params": put_params | del_params | lst_params
        }

        put_params = [{"key": ?, "attr1": ?, "attr2": ?, ...}, ...] - to create, just omit "id"
        del_params = [key1, key2, ...]
        lst_params = {"offset" : ..., "limit": ..., "filter":{}}

    """
    def post(self):
        #TODO: error handling
        #TODO: better use unique keys then ids
        #TODO: security???
        user = users.get_current_user()

        if not user:
            self.response.out.write(json_response(NOT_LOGGED_IN))
            return

        j_str = self.request.get('json')
        json_r = json.loads(j_str)
        op = json_r['op']
        entity = entities[json_r['ent']]

        if op == 'put':
            ents = []

            for cat_j in json_r['params']:
                ents.append(entity.from_dict(cat_j))
            db.put(ents)

            self.response.out.write(json_response(CAT_CREATED))
        elif op == 'del':
            db.delete(json_r['params'])
            self.response.out.write(json_response(CAT_DELETED))
        elif op == 'lst':
            offset = json_r['params']['offset']
            limit = json_r['params']['limit']

            q = entity.all()
            q.filter('account =', user)
            #filter

            #fetch an extra one to see if new pages have sense to be requested
            r = q.fetch(limit=limit + 1, offset=offset)

            l = len(r)
            if l <= limit: has_next = False
            else:
                has_next = True
                r.pop()

            self.response.out.write(json_response(CAT_SELECTED, {
                        'result': r,
                        'hasNext': has_next
            }))
        else:
            self.response.out.write(json_response(NOT_SUPPORTED))