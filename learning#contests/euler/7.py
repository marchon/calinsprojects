#http://projecteuler.net/problem=7
#http://projecteuler.net/project/resources/007_c1bfcd3425fd13f6e9abcfad4a222e35/007_overview.pdf

from math import sqrt

def is_prime(n):
	lim = sqrt(n)
	i = 2
	while i <= lim:
		if n % i == 0: return False
		i += 1
	return True
	
cnt = 1
nb = 3
pn = 0

while cnt < 10001:
	if is_prime(nb): 
		cnt += 1
		pn = nb
	nb += 2
	
print pn
	