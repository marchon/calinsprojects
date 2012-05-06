__author__ = 'Calin'

t9_mappings = {
    ' ' : '0',
    'a' : '2',
    'b' : '22',
    'c' : '222',
    'd' : '3',
    'e' : '33',
    'f' : '333',
    'g' : '4',
    'h' : '44',
    'i' : '444',
    'j' : '5',
    'k' : '55',
    'l' : '555',
    'm' : '6',
    'n' : '66',
    'o' : '666',
    'p' : '7',
    'q' : '77',
    'r' : '777',
    's' : '7777',
    't' : '8',
    'u' : '88',
    'v' : '888',
    'w' : '9',
    'x' : '99',
    'y' : '999',
    'z' : '9999'
}

def solve(word):
    t9_word = []
    for c in word:
        m = t9_mappings[c]
        if len(t9_word) > 0 and t9_word[-1][0] is m[0]:
            t9_word.append(' ')
        t9_word.append(m)

    return ''.join(t9_word)

in_file = "D:\work\gcj\data\in.txt"
out_file = "D:\work\gcj\data\out.txt"
with open(in_file, 'r') as f:
    with open(out_file, 'w') as g:
        cases = int(f.readline())
        for case in xrange(cases):
            word = f.readline().strip('\n')
            res = solve(word)
            g.write('Case #' + str(case + 1) + ": " + res)
            g.write('\n')
