#http://projecteuler.net/problem=12

from math import sqrt

def prime(n):
	for i in xrange(2, int(sqrt(n))+1):
		if n % i == 0: return False
	return True
	
prime_cache = [2,3,5,7,11,13,17,19]
def primes():
	for p in prime_cache:
		yield p
	
	sp = prime_cache[-1] + 1
	
	while True:
		if prime(sp):
			prime_cache.append(sp)
			yield sp
		sp += 1

def nb_div(nb):
	#nb = p1^e1 * ... * pn^en
	#d(nb) = (e1+1)...(en + 1)
	#d(24) = (3 + 1)(1 + 1) = 8.
	d = 1
	cnb = nb
	for p in primes(): 
		#if p**2 > cnb: return d * 2
		exp = 0
		while nb % p == 0:
			nb /= p
			exp += 1 
		if exp > 0: d *= (exp + 1)
		if nb == 1: return d

def solve():
	n = 1
	tr = 0
	stop = False

	while not stop:
		tr = tr + n
		
		#n and n+1 are coprime, tr = n*(n+1)/2 
		#if n%2 == 0:
		#	nd = nb_div(n/2)*nb_div(n+1)
		#else:
		#	nd = nb_div(n)*nb_div((n+1)/2)
		nd = nb_div(tr)
		if nd > 500:
			print tr, len(prime_cache)
			stop = True
		
		n += 1
		
from timeit import Timer
t = Timer('solve()', "from __main__ import solve").timeit(3)
print 'Execution time: ', t