from google.appengine.api import users
from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
import os
import logging

class MainHandler(webapp.RequestHandler):
	def get(self):
		user = users.get_current_user()
		if user:
			logging.info('User ' + user.nickname() + ' requested app.')
			path = os.path.join(os.path.dirname(__file__), '../view/index.html')
			self.response.out.write(template.render(path, {"logout" : users.create_logout_url("/")}))
		else:
			self.redirect(users.create_login_url(self.request.uri))