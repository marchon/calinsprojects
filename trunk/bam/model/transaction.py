from google.appengine.ext import db

class Transaction(db.Model):
	date = db.DateTimeProperty()
	desc = db.StringProperty()
	credit = db.FloatProperty()
	detailsKeys = db.StringListProperty()
	detailsValues = db.StringListProperty()