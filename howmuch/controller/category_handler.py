from google.appengine.ext import webapp
from google.appengine.ext import db
from model.transaction import Category
from google.appengine.api import users
from util.json_helper import json_response
from util.message_codes import *

class CategoryHandler(webapp.RequestHandler):
    def get(self):
        user = users.get_current_user()

        if not user:
            self.response.out.write(json_response(NOT_LOGGED_IN))
            return

        op = self.request.get('op')

        if op == 'add':
            name = self.request.get('name')
            rule = self.request.get('rule')

            if not name:
                self.response.out.write(json_response(NAME_NOT_EMPTY))
                return

            category = Category(
                account=user,
                name=name,
                rule=rule
            )
            category.put()
            self.response.out.write(json_response(CAT_CREATED))
        elif op == 'upd':
            id = self.request.get('id')
            name = self.request.get('name')
            rule = self.request.get('rule')

            if not id:
                self.response.out.write(json_response(ID_NOT_EMPTY))
                return

            key = db.Key.from_path('Category', id)
            category = db.get(key)

            if not name: category.name = name
            if not rule: category.rule = rule

            category.put()

            self.response.out.write(json_response(CAT_UPDATED))
        elif op == 'del':
            id = self.request.get('id')
            if not id:
                self.response.out.write(json_response(ID_NOT_EMPTY))
                return

            key = db.Key.from_path('Category', id)
            db.delete(key)

            self.response.out.write(json_response(CAT_DELETED))
        elif op == 'list':
            offset = int(self.request.get('offset', 0))
            limit = int(self.request.get('limit', 10))

            q = Category.all()
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