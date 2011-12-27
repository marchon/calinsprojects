from google.appengine.api import users
from google.appengine.ext import db

__author__ = 'Calin'

class Counter(db.Model):
    account = db.UserProperty(auto_current_user_add=True)

    entityName = db.StringProperty()
    count = db.IntegerProperty()

def get_counter(entity):
    q = Counter.all()
    q.filter('account =', users.get_current_user())
    q.filter('entityName = ', entity)
    r = q.fetch(1)

    return r[0] if len(r) is 1 else None

def update_counter(entity, amount):
    counter = get_counter(entity)

    if counter is None:
        if amount < 0: raise ValueError('There cannot be a negative count.')
        counter = Counter(entityName = entity, count = amount)
    else:
        counter.count += amount
        if counter.count < 0: raise ValueError('There cannot be a negative count.')

    counter.put()
