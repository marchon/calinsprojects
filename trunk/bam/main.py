#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#	 http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
from google.appengine.ext import webapp
from google.appengine.ext.webapp import util
import os
from google.appengine.ext.webapp import template
from google.appengine.ext import db
import csv
import datetime

class Transaction(db.Model):
	date = db.DateTimeProperty()
	desc = db.StringProperty()
	credit = db.FloatProperty()
	detailsKeys = db.StringListProperty()
	detailsValues = db.StringListProperty()

class MainHandler(webapp.RequestHandler):
	def get(self):
		path = os.path.join(os.path.dirname(__file__), 'index.html')
		self.response.out.write(template.render(path, {}))

class UploadHandler(webapp.RequestHandler):
	def post(self):
		data = csv.reader(self.request.get('data').split('\n'))
		data.next() #first row is headers
		transaction = None
	
		for row in data:
			if row[0].find('Sold') is not -1: break
			
			if row[0] is not '':
				if transaction is not None:
					transaction.detailsKeys = keys
					transaction.detailsValues = values
					transaction.put()
				transaction = Transaction()
				sd = row[0].split()
				transaction.date = datetime.datetime(int(sd[2]), months[sd[1].lower()], int(sd[0]))
				transaction.desc = row[1]
				if row[2] is not '': transaction.debit = float(row[2].replace('.','').replace(',','.'))
				if row[3] is not '': transaction.credit = float(row[3].replace('.','').replace(',','.'))

				keys = []
				values = []
			else:
				pair = row[1].split(':')
				if len(pair) is 2:
					keys.append(pair[0])
					values.append(pair[1])
				else:
					values[-1] += (',' + pair[0])
							
		if transaction is not None:
			transaction.detailsKeys = keys
			transaction.detailsValues = values
			transaction.put()
			
months = {
	"ianuarie": 1,
	"februarie": 2,
	"martie": 3,
	"aprilie": 4,
	"mai": 5,
	"iunie": 6,
	"iulie": 7,
	"august": 8,
	"septembrie": 9,
	"octombrie": 10,
	"noiembrie": 11,
	"decembrie": 12
}

def main():
	application = webapp.WSGIApplication(
		[
			('/', MainHandler),
			('/upload', UploadHandler),
		], debug=True)
	util.run_wsgi_app(application)


if __name__ == '__main__':
	main()
