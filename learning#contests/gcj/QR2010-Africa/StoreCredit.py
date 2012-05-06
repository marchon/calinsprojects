__author__ = 'Calin'


def solve(c, items):
    for i in xrange(len(items)):
        for j in xrange(i + 1, len(items)):
            if items[i] + items[j] == c:
                return [i + 1, j + 1]

    return None


in_file = "D:\work\gcj\data\in.txt"
out_file = "D:\work\gcj\data\out.txt"
with open(in_file, 'r') as f:
    with open(out_file, 'w') as g:
        cases = int(f.readline())
        for case in xrange(cases):
            c = int(f.readline())
            f.readline() #skip len
            items = [int(a) for a in f.readline().split()]

            res = solve(c, items)
            out = 'Case #' + str(case + 1) + ": " + str(res[0]) + ' ' + str(res[1])
            g.write(out + '\n')
