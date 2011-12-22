from google.appengine.ext import webapp
import csv
import datetime
from model.model import Transaction
from google.appengine.api import users

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

class UploadHandler(webapp.RequestHandler):
    def post(self):
        if not users.get_current_user():
            self.response.out.write('not logged in!')
        else:
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
                    if row[2] is not '': transaction.debit = float(row[2].replace('.', '').replace(',', '.'))
                    if row[3] is not '': transaction.credit = float(row[3].replace('.', '').replace(',', '.'))

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

            self.response.out.write('success!')