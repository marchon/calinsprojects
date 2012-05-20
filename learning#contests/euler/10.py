#http://projecteuler.net/problem=10
from math import sqrt
n = 2000000

#number is index + 2
#true means crossed out
#crossout even numbers except 2
#first two are ignored
sieve = [False if i % 2 != 0 or i == 2 else True for i in xrange(n)]	

for i in xrange(3,int(sqrt(n)) + 1, 2):
	if not sieve[i]:
		#crossout all the multiples of this prime number
		mul = i ** 2
		s = 2 * i
		while mul < n:
			sieve[mul] = True
			mul += s 

s = 0
for i in xrange(2, n):
	if not sieve[i]:
		s += i
print s
#print sum([prime for prime in xrange(2,n) if not sieve[prime]])
		