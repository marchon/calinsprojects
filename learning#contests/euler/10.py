n = 10

primes = [True] * n
primes[0] = False
primes[1] = False
primes[2] = True
	

for i in xrange(3,n):
	if primes[i]:
		if is_prime(i):
			#if it really is prime, mark it's multiples not primes
			mul = 2 * i
			while mul < n:
				primes[mul] = False
				mul += i
		else:
			#otherwise, mark it as not prime
			primes[i] = False

print sum([prime for prime in xrange(2,n) if primes[prime]])
		