#http://code.google.com/codejam/contest/1460488/dashboard#s=p1

import sys

__author__ = 'Calin'

def ns_max(x):
    c = x / 3
    r = x % 3

    return c + (0 if not r else 1)

def s_max(x):
    c = x / 3
    r = x % 3

    return c + (1 if r < 2 else 2)

def solve(t, s, p):
    t = reversed(sorted(t))

    g = 0

    for x in t:
        if ns_max(x) >= p: g += 1
        else:
            if s > 0 and 2 <= x <= 28:
                if s_max(x) >= p:
                    g += 1
                    s -= 1
                else: break
            else: break

    return g

cases = int(sys.stdin.readline())

for c in xrange(cases):
    nbs = sys.stdin.readline().strip('\n').split()
    nbs = [int(a) for a in nbs]
    res = solve(nbs[3:], nbs[1], nbs[2])
    sys.stdout.write("Case #%d: %s\n" % (c + 1, res))