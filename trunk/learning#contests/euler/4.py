#http://projecteuler.net/problem=4

def is_palindrome(n):
	cn = n
	revn = 0
	while cn:
		revn = revn * 10 + cn % 10
		cn /= 10		
	return revn == n
	
def solve(digits):
	f = 10 ** (digits - 1)
	lp = 0
	for i in reversed(xrange(f, f * 10)):
		for j in reversed(xrange(i, f * 10)):
			p = i * j
			if p <= lp: break
			if is_palindrome(p): 
				lp = p
	
	return lp
	
print solve(3)
		