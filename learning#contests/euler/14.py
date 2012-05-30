#http://projecteuler.net/problem=14

cache = {}
def collatz_sequence_len(n):
	if n == 1: return 1
	if n in cache: 
		return cache[n]
	
	r = None
	if n % 2 == 0: 
		r = 1 + collatz_sequence_len(n / 2)
	else: 
		r = 1 + collatz_sequence_len(3 * n + 1)
	
	cache[n] = r
	return r

def solve_rec(n):
	max = 0
	max_n = 0

	for n in xrange(1, n):
		r = collatz_sequence_len(n)
		if r > max:
			max = r
			max_n = n
			
	return max_n
	

def solve_iter1(n):
	cache = {1:1}
	max = 0
	max_i = 0
	
	for i in xrange(1, n):
		len_i = 0
		r = i
		while True:
			if r in cache:
				len_i += cache[r]
				break
			else:
				len_i += 1
				if r % 2 == 0: r /= 2
				else: r = 3 * r + 1
		
		cache[i] = len_i
			
		if len_i > max:
			max = len_i
			max_i = i
	
	return max_i
	
def solve_iter2(n):
	cache = {1:1}
	max = 0
	max_i = 0
	
	for i in xrange(1, n):
		len_accumulators = [
			[i, 0]
		]
		r = i
		while True:
			if r in cache:
				for acc in len_accumulators:
					acc[1] += cache[r]
				break
			else:
				for acc in len_accumulators:
					acc[1] += 1
				if r % 2 == 0: r /= 2
				else: r = 3 * r + 1
				len_accumulators.append([r, 0])
		
		for acc in len_accumulators:
			cache[acc[0]] = acc[1]
			
		len_i = len_accumulators[0][1]	
		if len_i > max:
			max = len_i
			max_i = i
	
	return max_i

	
def solve_iter3(n):
	cache = [0] * n
	cache[1] = 1
	
	max = 0
	max_i = 0
	
	for i in xrange(1, n):
		len_i = 0
		r = i
		while True:
			if r < n and cache[r] > 0:
				len_i += cache[r]
				break
			else:
				len_i += 1
				if r % 2 == 0: r /= 2
				else: r = 3 * r + 1
		
		cache[i] = len_i
			
		if len_i > max:
			max = len_i
			max_i = i
	
	return max_i
	
	
from timeit import Timer
t = Timer('print "recursive:", solve_rec(1000000)', "from __main__ import solve_rec").timeit(1)
print 'Execution time: ', t

t = Timer('print "iter1:", solve_iter1(1000000)', "from __main__ import solve_iter1").timeit(1)
print 'Execution time: ', t

#t = Timer('print "iter2:", solve_iter2(1000000)', "from __main__ import solve_iter2").timeit(1)
#print 'Execution time: ', t

t = Timer('print "iter3:", solve_iter3(1000000)', "from __main__ import solve_iter3").timeit(1)
print 'Execution time: ', t