CAT_CREATED = 0
NAME_NOT_EMPTY = 1
NOT_LOGGED_IN = 2
NOT_SUPPORTED = 3
KEY_NOT_EMPTY = 4
CAT_UPDATED = 5
CAT_DELETED = 6

SUC = 0
ERR = 1

m = {
	NOT_LOGGED_IN: 	[ERR, 'You have to be logged in to perform this operation.'],
	NOT_SUPPORTED:	[ERR, 'This operation is not supported.'],
	CAT_CREATED: 	[SUC, 'New category was created.'],
	CAT_UPDATED:	[SUC, 'Category was updated.'],
	CAT_DELETED:	[SUC, 'Category was deleted.'],
	NAME_NOT_EMPTY: [ERR, 'Name must not be empty.'],
	KEY_NOT_EMPTY: 	[ERR, 'Key must not be empty.']
}