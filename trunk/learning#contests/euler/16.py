#http://projecteuler.net/problem=16

def mult(digits, number):
	#number has one digit
	reminder = 0
	for i in xrange(len(digits)):
		digit = digits[i]
		t = digit * number + reminder
		digits[i] = t % 10
		reminder = t / 10
		
	if reminder > 0: digits.append(reminder)
	
digits = [2]
for i in xrange(999):
	mult(digits, 2)
print sum(digits)
print sum(map(int, str(2**1000))) #python has no restrictions on number length