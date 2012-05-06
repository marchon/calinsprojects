#http://code.google.com/codejam/contest/1645485/dashboard#s=p0

__author__ = 'Calin'

#def solve(P, b):
#    a = len(P)
#    spl = pow(2, a)
#
#    #keep typing, press enter, erase 1..len(P) chars
#    exp_v = [0] * (2 + a)
#
#    for i in xrange(spl):
#        total_p = 1
#
#        for j in xrange(len(P)):
#            this_p = P[j] if i & (1 << j) else 1 - P[j]
#            total_p *= this_p
#
#        key_press_to_end = b - a + 1
#        key_press_whole_word = b + 1
#
#        #keep typing - write again whole word if not all chars are correct
#        kt = key_press_to_end + (0 if i == spl - 1 else key_press_whole_word)
#        exp_v[0] += kt * total_p
#
#        #press enter - if whole word is written correct - that's it(just enter)
#        pe = 1 + (0 if i == spl - 1 and a == b else key_press_whole_word)
#        exp_v[1] += pe * total_p
#
#        for k in xrange(a):
#            mask = pow(2, (a - k - 1)) - 1
#            kpk = (k + 1) * 2 + key_press_to_end + (0 if i & mask == mask else key_press_whole_word)
#            exp_v[2 + k] += kpk * total_p
#
#    return min(exp_v)
#
#cases = int(raw_input())
#
#for c in xrange(cases):
#    line = [int(a) for a in raw_input().split()]
#    P = [float(a) for a in raw_input().split()]
#
#    res = solve(P, line[1])
#
#    print "Case #%d: %.6f" % (c + 1, res)

import sys

for tc in xrange(1, int(sys.stdin.readline())+1):
    A, B = [int(w) for w in sys.stdin.readline().split()]
    p = [float(w) for w in sys.stdin.readline().split()]
    best, x = B + 2.0, 1
    for i in xrange(A):
        x *= p[i]
        best = min(best, (B - A + 2 * i + 1) + (B + 1) * (1 - x))
    print 'Case #%d: %f' % (tc, best)



