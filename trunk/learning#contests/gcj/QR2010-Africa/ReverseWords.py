__author__ = 'Calin'

def solve(words):
    for i in xrange(len(words) / 2):
        tmp = words[i]
        words[i] = words[-1-i]
        words[-1-i] = tmp

    return words

in_file = "D:\work\gcj\data\in.txt"
out_file = "D:\work\gcj\data\out.txt"
with open(in_file, 'r') as f:
    with open(out_file, 'w') as g:
        cases = int(f.readline())
        for case in xrange(cases):
            words = [a for a in f.readline().split()]
            solve(words)
            g.write('Case #' + str(case + 1) + ": ")
            for word in words:
                g.write(word)
                g.write(' ')
            g.write('\n')