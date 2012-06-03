map = {
0: 0,
1: len('one'),
2: len('two'),
3: len('three'),
4: len('four'),
5: len('five'),
6: len('six'),
7: len('seven'),
8: len('eight'),
9: len('nine'),
10: len('ten'),
11: len('eleven'),
12: len('twelve'),
13: len('thirteen'),
14: len('fourteen'),
15: len('fifteen'),
16: len('sixteen'),
17: len('seventeen'),
18: len('eighteen'),
19: len('nineteen'),
20: len('twenty'),
30: len('thirty'),
40: len('forty'),
50: len('fifty'),
60: len('sixty'),
70: len('seventy'),
80: len('eighty'),
90: len('ninty'),
100: len('hundred'),
1000: len('thousand')
}

sum = 0

for n in xrange(1, 1000):	
	#eg: two hundred and
	if n >= 100: 
		sum += map[n/100] + map[100]
		if n % 100 > 0: sum += 3
	#eg: nineteen, eight, etc.
	if n % 100 <= 20: sum += map[n % 100]
	#eg: sixty four
	else: sum += map[n % 10] + map[n % 100 - n % 10]
	
sum += map[1000] + map[1]
print sum