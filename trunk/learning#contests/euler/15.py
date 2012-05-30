#http://projecteuler.net/problem=15
cache = {}
def solve(a, b):
	key = str(a) + str(b)
	if key in cache: return cache[key]
	
	res = None
	if b[0] - a[0] == 0: res = 1
	elif b[1] - a[1] == 0: res = 1
	else:
		res = solve((a[0] + 1, a[1]), b) + solve((a[0], a[1] + 1), b) 
	
	cache[key] = res
	return res
		
print solve((0, 0), (20,20))