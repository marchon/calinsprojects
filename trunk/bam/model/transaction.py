from google.appengine.ext import db
import json

class Category(db.Model):
	account = db.UserProperty()
	
	name = db.StringProperty()
	rule = db.StringProperty()
	
	def json(self):
		return {
			"id": self.key().id_or_name(),
			"name": self.name,
			"rule": self.rule
		}

class Transaction(db.Model):
	account = db.UserProperty()
	
	date = db.DateTimeProperty()
	desc = db.StringProperty()
	debit = db.FloatProperty()
	credit = db.FloatProperty()
	detailsKeys = db.StringListProperty()
	detailsValues = db.StringListProperty()
	category = db.ReferenceProperty(Category)
	
#db.put([[]e1, e2, e3]) batch put is faster
#key().id()