__author__ = 'Calin'

def isVowel(letter):
    return letter == 'a' or letter == 'e' or letter == 'i' or letter == 'o' or letter == 'u'

def vowelCount(word):
    cnt = 0
    for l in word:
        cnt += isVowel(l)
    return cnt

def solve(pspell):
    l = len(pspell)
    for p0 in xrange(l-5):
        brk = False
        for p1 in xrange(p0+2, l-3):
            if vowelCount(pspell[p0:p1]) < 2: continue       #optimize this
            for p2 in xrange(p1+1, l-2):
                if vowelCount(pspell[p1:p2]) < 1: continue
                p3 = p2 + p1 - p0
                if p3 > l:
                    brk = True
                    break
                #print pspell[p0:p1], pspell[p1:p2], pspell[p2:p3]
                if not cmp(pspell[p0:p1], pspell[p2:p3]): return pspell[p0:p1], pspell[p1:p2], pspell[p2:p3], "Spell!"
            if brk: break

    return 'Nothing.'

in_file = "D:\work\gcj\data\in.txt"
out_file = "D:\work\gcj\data\out.txt"

with open(in_file, "r") as f:
    with open(out_file, "w") as g:
        cases = int(f.readline())
        for c in xrange(cases):
            word  = f.readline().strip('\n')
            res = solve(word)
            g.write('Case #' + str(c + 1) + ": " + str(res))
            g.write('\n')