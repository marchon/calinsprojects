def nb_paths(m, l, a, b):
	if a == b: return 1
	
	s = 0
	for i in xrange(l):
		if m[a][i]: s += nb_paths(m, l, i, b)
		
	return s

def solve(m):
	l = len(m)
	
	for i in xrange(l):
		for j in xrange(l):
			if i != j and nb_paths(m, l, i, j) > 1:
				return "Yes"
				
	return "No"
	
tc = int(raw_input())
for c in xrange(tc):
	n = int(raw_input())
	m = [ [ 0 for i in range(n) ] for j in range(n) ]
	for cls in xrange(n):
		links = [int(l) for l in raw_input().split()]
		for i in range(1, links[0] + 1):
			m[cls][links[i] - 1] = 1
	print "Case #%d: %s" % (c+1, solve(m))

