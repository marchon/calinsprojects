from google.appengine.api import users
from google.appengine.ext import db

class Category(db.Model):
    account = db.UserProperty()

    name = db.StringProperty()
    rule = db.StringProperty()

    def to_dict(self):
        return {
            'key': str(self.key()),
            'name': self.name,
            'rule': self.rule
        }

    def from_dict(cat):
        if cat.has_key('key'): # get from datastore
            category = db.get(cat['key'])
            if cat['name']: category.name = cat['name']
            if cat['rule']: category.rule = cat['rule']
        else:         # create
            category = Category()
            category.account = users.get_current_user()
            category.name = cat['name']
            category.rule = cat['rule']

        return category
    from_dict = staticmethod(from_dict)

class Transaction(db.Model):
    account = db.UserProperty()

    date = db.DateTimeProperty()
    desc = db.StringProperty()
    debit = db.FloatProperty()
    credit = db.FloatProperty()
    detailsKeys = db.StringListProperty()
    detailsValues = db.StringListProperty()
    category = db.ReferenceProperty(Category)


    def dict(self):
        return {
            'id': self.key().id_or_name(),
            'date': self.date,
            'desc': self.desc,
            'debit': self.debit,
            'credit': self.credit,
            'details': dict(zip(self.detailsKeys, self.detailsValues))
            #'category': self.category.id_or_name() #send the id??
        }



entities = {
    "Category": Category,
    "Transaction": Transaction
}
