from google.appengine.ext import webapp
from google.appengine.ext import db
from model.transaction import Transaction
from model.transaction import Category
from google.appengine.api import users
from util.json import json
from util.message_codes import *

class CategoryHandler(webapp.RequestHandler):
	def get(self):
		user = users.get_current_user()
		
		if not user:
			self.response.out.write(json(NOT_LOGGED_IN))
			return
			
		op = self.request.get('op')
		
		if op == 'add':
			name = self.request.get('name')
			rule = self.request.get('rule')
			
			if name == None or name == '':
				self.response.out.write(json(NAME_NOT_EMPTY))
				return
			
			category = Category(
				account = user,
				name = name,
				rule = rule
			)
			category.put()
			self.response.out.write(json(CAT_CREATED))
		elif op == 'upd':
			key = self.request.get('key')
			name = self.request.get('name')
			rule = self.request.get('rule')
			
			if key == None or key == '':
				self.response.out.write(json(KEY_NOT_EMPTY))
				return
			
			category = db.get(key)
			if name != None and name != '': category.name = name
			if rule != None and rule != '': category.rule = rule
			
			category.put()
			
			self.response.out.write(json(CAT_UPDATED))
		elif op == 'del':
			key = self.request.get('key')
			if key == None or key == '':
				self.response.out.write(json(KEY_NOT_EMPTY))
				return			
			db.delete(key)
			self.response.out.write(json(CAT_DELETED))
		elif op == 'list':
			pass
		else:
			self.response.out.write(json(NOT_SUPPORTED))