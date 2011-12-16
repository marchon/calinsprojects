from google.appengine.ext import webapp
from google.appengine.ext.webapp import template
import os

class MainHandler(webapp.RequestHandler):
	def get(self):
		path = os.path.join(os.path.dirname(__file__), '../view/index.html')
		self.response.out.write(template.render(path, {}))