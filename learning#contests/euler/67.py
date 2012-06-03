f = open('67.txt', 'r')
t = [map(int, x.strip().split()) for x in f.readlines()]
f.close()

def max_path_itr():
	n = len(t)
	for i in reversed(xrange(n - 1)):
		for j in xrange(i + 1):
			if t[i+1][j] > t[i+1][j+1]:
				t[i][j] += t[i+1][j]
			else:
				t[i][j] += t[i+1][j+1]
	return t[0][0]

import time
tStart = time.clock()
print max_path_itr()
print 'iterative:', (time.clock() - tStart)