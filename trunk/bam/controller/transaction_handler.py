from google.appengine.ext import webapp
from google.appengine.ext import db
from model.transaction import Transaction
from google.appengine.api import users

class TransactionHandler(webapp.RequestHandler):
	def get(self):
		user = users.get_current_user()
		
		if not user:
			self.response.out.write(json(NOT_LOGGED_IN))
			return
			
		op = self.request.get('op')
		
		if op == 'add':
			pass
		elif op == 'upd':
			pass
		elif op == 'del':
			pass
		elif op == 'list':
			pass
		else:
			self.response.out.write(json(NOT_SUPPORTED))
