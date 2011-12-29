from google.appengine.ext import webapp, db
import csv
import datetime
from google.appengine.ext.db import BadValueError, Error
from controller.util.json_response import *
from google.appengine.api import users
from model import counter
from model.model import Transaction


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

def process_ing_csv(data):
    transactions = []
    transaction = None
    details = {}
    last_key = None

    data = csv.reader(data.split('\n'))

    data.next() #first row is headers

    for row in data:
        if row[0].find('Sold') is not -1: break #we don't need this info at the bottom

        if row[0] is not '':       #details have first column in csv row an empty string
            if transaction is not None:   #add the current transaction
                transaction['details'] = details
                transactions.append(transaction)
                details = {}
                last_key = None

            transaction = {}
            sd = row[0].split()
            transaction['date'] = datetime.datetime(int(sd[2]), months[sd[1].lower()], int(sd[0]))
            transaction['desc'] = row[1]
            if row[2] is not '': transaction['debit'] = float(row[2].replace('.', '').replace(',', '.'))
            if row[3] is not '': transaction['credit'] = float(row[3].replace('.', '').replace(',', '.'))

        else:
            pair = row[1].split(':')
            if len(pair) is 2:
                details[pair[0]] = pair[1]
                last_key = pair[0]
            else:
                if last_key is not None: details[last_key] += (',' + pair[0])

    if transaction is not None:
        transaction['details'] = details
        transactions.append(transaction)

    return transactions


class UploadHandler(webapp.RequestHandler):
    def post(self):
        user = users.get_current_user()

        if not user:
            self.response.out.write(json_response(NOT_LOGGED_IN))
            return

        data = self.request.get('data')
        if data is None:
            self.response.out.write(json_response(BAD_REQUEST))
            return

        try:
            transactions = process_ing_csv(data)
        except Exception, e:
            self.response.out.write(json_response(EX_PROCESS_DATA))
            return

        dbtr = []
        try:
            for t in transactions:
                dbtr.append(Transaction.create(t))

            db.put(dbtr)
            counter.update_counter('Transaction', len(dbtr))
        except (KeyError, ValueError, BadValueError):
            self.response.out.write(json_response(OP_BAD_VALUE))
        except Error:
            self.response.out.write(json_response(OP_DS_FAILED))

        self.response.out.write(json_response(OP_END_SUCCESS))



