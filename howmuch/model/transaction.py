from google.appengine.ext import db

class Category(db.Model):
    account = db.UserProperty()

    name = db.StringProperty()
    rule = db.StringProperty()

    def dict(self):
        return {
            'id': self.key().id_or_name(),
            'name': self.name,
            'rule': self.rule
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

#db.put([[]e1, e2, e3]) batch put is faster
#key().id()