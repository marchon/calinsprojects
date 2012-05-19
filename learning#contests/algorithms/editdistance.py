#ADM#p292
#number of edits required to transform s into t


#levenshtein recursive - incredibly slow because of branching
#impoved with caching
cache = {}
def edit_dist_rec(s, t):
	key = s+t
	if key in cache: return cache[key]
	#matching/substitution/insertion/deletion
	
	if not s: return len(t) #insert the rest
	if not t: return len(s) #same
	
	#match/substitution
	l1 = edit_dist_rec(s[:-1], t[:-1])

	#mathing requires no additional cost
	if s[-1] == t[-1]: return l1
	
	#insertion in s /??
	l2 = edit_dist_rec(s, t[:-1])
	
	#deltion from s
	l3 = edit_dist_rec(s[:-1], t)
	
	#at this point we choose an operation that minimizes the distance
	#we also add the cost of the operation
	r =  1 + min(l1, l2, l3)
	cache[key] = r
	return r
def edit_dist_rec_helper(s, t):
	cache = {}
	return edit_dist_rec(s, t)
	
	
	
#levenshtein iterative
def edit_dist(s, t):
	m = len(s) + 1
	n = len(t) + 1
	mx = [[0] * n for x in xrange(m)]
	
	for i in xrange(1, m): mx[i][0] = i
	for i in xrange(1, n): mx[0][i] = i
	
	for i in xrange(1, m):
		for j in xrange(1, n):
			if s[i - 1] == t[j - 1]: mx[i][j] = mx[i-1][j-1]
			else:
				mx[i][j] = min(mx[i-1][j],mx[i][j-1],mx[i-1][j-1]) + 1
	
	return mx[-1][-1]
	
print 'Iterative: ', edit_dist('rosettacode', 'raisethysword')
print 'Recursive: ', edit_dist_rec_helper('rosettacode', 'raisethysword')
	
from timeit import Timer
ti = Timer('edit_dist("rosettacode","raisethysword")', "from __main__ import edit_dist").timeit(100)
tr = Timer('edit_dist_rec_helper("rosettacode","raisethysword")', "from __main__ import edit_dist_rec_helper").timeit(100)

print 'Times(I/R): ', ti, tr 
	