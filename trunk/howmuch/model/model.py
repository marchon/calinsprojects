import datetime
from google.appengine.api import users
from google.appengine.ext import db

class Category(db.Model):
    account = db.UserProperty()

    name = db.StringProperty()
    rule = db.StringProperty()

    def to_dict(cat):
        return {
            'key': str(cat.key()),
            'name': cat.name,
            'rule': cat.rule
        }

    def from_dict(cat):
        if cat.has_key('key'): # get from datastore
            category = db.get(cat['key'])
            if cat.has_key('name'): category.name = cat['name']
            if cat.has_key('rule'): category.rule = cat['rule']
        else:         # create
            category = Category()
            category.account = users.get_current_user()
            if cat.has_key('name'): category.name = cat['name']
            if cat.has_key('rule'): category.rule = cat['rule']

        return category

    def all_query():
        q = Category.all()
        q.filter('account =', users.get_current_user())
        return q

    to_dict = staticmethod(to_dict)
    from_dict = staticmethod(from_dict)
    all_query = staticmethod(all_query)

class Transaction(db.Model):
    account = db.UserProperty()

    date = db.DateTimeProperty()
    desc = db.StringProperty()
    debit = db.FloatProperty()
    credit = db.FloatProperty()
    detailsKeys = db.StringListProperty()
    detailsValues = db.StringListProperty()
    category = db.ReferenceProperty(Category)


    def to_dict(trans):
        return {
            'key': str(trans.key()),
            'date': trans.date,
            'desc': trans.desc,
            'debit': trans.debit,
            'credit': trans.credit,
            'details': dict(zip(trans.detailsKeys, trans.detailsValues)),
            #'category': trans.category #send the id??
        }

    def from_dict(trans):
        if trans.has_key('key'): # get from datastore
            transaction = db.get(trans['key'])
            if trans.has_key('date'): transaction.name = trans['date']
            if trans.has_key('desc'): transaction.desc = trans['desc']
            if trans.has_key('debit'): transaction.debit = trans['debit']
            if trans.has_key('credit'): transaction.credit = trans['credit']

        else:         # create
            transaction = Transaction()
            transaction.account = users.get_current_user()
            if trans.has_key('date'): transaction.name = trans['date']
            if trans.has_key('desc'): transaction.desc = trans['desc']
            if trans.has_key('debit'): transaction.debit = trans['debit']
            if trans.has_key('credit'): transaction.credit = trans['credit']

        return transaction

    def all_query():
        q = Transaction.all()
        q.filter('account =', users.get_current_user())
        return q

    to_dict = staticmethod(to_dict)
    from_dict = staticmethod(from_dict)
    all_query = staticmethod(all_query)
