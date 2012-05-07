#http://projecteuler.net/problem=9
def solve(n):
	for a in xrange(n/3):
		for b in xrange(a+1, (n-a)/2):
			c = n - a - b
			if a**2 + b**2 == c**2: return a*b*c, a, b, c
	return -1
		
print solve(1000)