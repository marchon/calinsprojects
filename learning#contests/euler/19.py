dow = 0
d = 1
m = 1
y = 1900
cnt = 0

DAYS = ['MON', 'TUE', 'WED', 'THU', 'FRY', 'SAT', 'SUN']
months = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]

while y < 2001:
	if d == 1 and dow == 6 and y >= 1901: 
		cnt+=1
		print DAYS[dow], d, m, y
	
	d += 1
	dow += 1
	dow = dow % 7
	
	mxd = months[m - 1]
	if m == 2 and (d == 29 or d == 30):
		if y % 4 == 0 and (y % 100 != 0 or y % 400 == 0): 
			mxd += 1
			
	if d == mxd + 1:
		d = 1
		m += 1
	
	if m == 13:
		m = 1
		y += 1
		
print cnt
	