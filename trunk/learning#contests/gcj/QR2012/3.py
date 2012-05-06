#http://code.google.com/codejam/contest/1460488/dashboard#s=p2

import sys

__author__ = 'Calin'

def solve(A, B):
    cnt = 0
    for n in xrange(A, B):
        sn = str(n)

        occurred = []
        for i in xrange(1, len(sn)):
            m = int(sn[i:] + sn[:i])
            if n < m <= B and m not in occurred:
                occurred.append(m)
                cnt += 1
            #optimize

    return cnt


cases = int(sys.stdin.readline())

for c in xrange(cases):
    nbs = sys.stdin.readline().strip('\n').split()
    res = solve(int(nbs[0]), int(nbs[1]))
    sys.stdout.write("Case #%d: %s\n" % (c + 1, res))