#http://code.google.com/codejam/contest/1460488/dashboard#s=p0

import sys

__author__ = 'Calin'

def make_mappings(g, e):
    m = {
        'q' : 'z', #deducted
        'z' : 'q'
    }

    for i in xrange(len(g)):
        gf = g[i]
        ef = e[i]

        for j in xrange(len(gf)):
            m[gf[j]] = ef[j]

    return m

map = make_mappings(
    [
        'ejp mysljylc kd kxveddknmc re jsicpdrysi',
        'rbcpc ypc rtcsra dkh wyfrepkym veddknkmkrkcd',
        'de kr kd eoya kw aej tysr re ujdr lkgc jv'
    ],
    [
        'our language is impossible to understand',
        'there are twenty six factorial possibilities',
        'so it is okay if you want to just give up'
    ]
)

def solve(gs):
    trans = []

    for l in gs:
        trans.append(map[l])

    return ''.join(trans)


cases = int(sys.stdin.readline())

for c in xrange(cases):
    word = sys.stdin.readline().strip('\n')
    translated = solve(word)
    sys.stdout.write("Case #%d: %s\n" % (c + 1, translated))