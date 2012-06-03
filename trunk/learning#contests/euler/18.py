class memoized(object):
   '''Decorator. Caches a function's return value each time it is called.
   If called later with the same arguments, the cached value is returned 
   (not reevaluated).
   '''
   def __init__(self, func):
      self.func = func
      self.cache = {}
   def __call__(self, *args):
      try:
         return self.cache[args]
      except KeyError:
         value = self.func(*args)
         self.cache[args] = value
         return value
      except TypeError:
         # uncachable -- for instance, passing a list as an argument.
         # Better to not cache than to blow up entirely.
         return self.func(*args)
   def __repr__(self):
      '''Return the function's docstring.'''
      return self.func.__doc__
   def __get__(self, obj, objtype):
      '''Support instance methods.'''
      return functools.partial(self.__call__, obj)

input="""75
95 64
17 47 82
18 35 87 10
20 04 82 47 65
19 01 23 75 03 34
88 02 77 73 07 63 67
99 65 04 28 06 16 70 92
41 41 26 56 83 40 80 70 33
41 48 72 33 47 32 37 16 94 29
53 71 44 65 25 43 91 52 97 51 14
70 11 33 28 77 73 17 78 39 68 17 57
91 71 52 38 17 14 91 43 58 50 27 29 48
63 66 04 68 89 53 67 30 73 16 69 87 40 31
04 62 98 27 23 09 70 98 73 93 38 53 60 04 23"""	  

triangle = [map(int, x.split()) for x in input.split('\n')]
	  
@memoized
def max_path(i, j):
	res = triangle[i][j]
	if i < len(triangle) - 1: res += max(max_path(i+1, j), max_path(i+1, j+1))
	return res
	
def max_path_itr(t):
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
print max_path(0,0)
print 'recursive w/ mem:', (time.clock() - tStart)

import copy
tr = copy.deepcopy(triangle)
tStart = time.clock()
print max_path_itr(tr)
print 'iterative:', (time.clock() - tStart)