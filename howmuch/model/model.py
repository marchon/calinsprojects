import datetime
from google.appengine.api import users
from google.appengine.ext import db

class Category(db.Model):
    account = db.UserProperty(auto_current_user_add=True)

    name = db.StringProperty()
    rule = db.StringProperty()

    def serialize(category):
        return {
            'key': str(category.key()),
            'name': category.name,
            'rule': category.rule
        }

    def create(map):
        category = Category()
        if map.has_key('name'): category.name = map['name']
        if map.has_key('rule'): category.rule = map['rule']

        return category

    def load(map):
        category = db.get(map['key'])
        if map.has_key('name'): category.name = map['name']
        if map.has_key('rule'): category.rule = map['rule']

        return category

    def query():
        q = Category.all()
        q.filter('account =', users.get_current_user())
        return q

    serialize = staticmethod(serialize)
    create = staticmethod(create)
    load = staticmethod(load)
    query = staticmethod(query)

class Transaction(db.Model):
    account = db.UserProperty(auto_current_user_add=True)

    date = db.DateTimeProperty()
    desc = db.StringProperty()
    debit = db.FloatProperty()
    credit = db.FloatProperty()
    detailsKeys = db.StringListProperty()
    detailsValues = db.StringListProperty()
    category = db.ReferenceProperty(Category)


    def serialize(trans):
        return {
            'key': str(trans.key()),
            'date': trans.date,
            'desc': trans.desc,
            'debit': trans.debit,
            'credit': trans.credit,
            'details': dict(zip(trans.detailsKeys, trans.detailsValues)),
            #'category': trans.category #send the id??
        }

    def create(map):
        transaction = Transaction()
        if map.has_key('date'): transaction.name = map['date']
        if map.has_key('desc'): transaction.desc = map['desc']
        if map.has_key('debit'): transaction.debit = map['debit']
        if map.has_key('credit'): transaction.credit = map['credit']

        return transaction

    def load(map):
        transaction = db.get(map['key'])
        if map.has_key('date'): transaction.name = map['date']
        if map.has_key('desc'): transaction.desc = map['desc']
        if map.has_key('debit'): transaction.debit = map['debit']
        if map.has_key('credit'): transaction.credit = map['credit']

        return transaction

    def query():
        q = Transaction.all()
        q.filter('account =', users.get_current_user())
        return q

    serialize = staticmethod(serialize)
    create = staticmethod(create)
    load = staticmethod(load)
    query = staticmethod(query)
