from math import sqrt

def div_sum(n):
	s = 1
	
	r = int(sqrt(n))
	
	#squer roots need not be counted twice
	if r * r == n: 
		s += r
		r -= 1
		
	start = 2
	step = 1
	
	if n % 2 == 1:
		start = 3
		step = 2
	
	for i in xrange(start, r + 1, step):
		if n % i == 0: s += (i + n / i)
	
	return s
	
sum = 0	
for n in xrange(2, 10000):
	ds = div_sum(n)
	if ds > n:
		if div_sum(ds) == n:
			sum += n + ds
print sum