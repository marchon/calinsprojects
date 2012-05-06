__author__ = 'Calin'

def solve(songs):
    if len(songs) == 0: return []
    if len(songs) == 1: return ['""']

    resp = {}

    for i in range(len(songs)):
        songs[i] = songs[i].upper()

    for song in songs:
        for wl in xrange(1, len(song) + 1): # <= len
            i = 0
            while True:
                word = song[i:i+wl]
                unique = True

                for s in songs:
                    if s != song and word in s:
                        unique = False
                        break

                if unique:
                    try:
                        lst = resp[song]
                        idx = 0

                        while idx < len(lst) and cmp(word, lst[idx]) == 1:
                            idx += 1

                        lst.insert(idx, word)
                    except KeyError:
                        resp[song] = [word]

                if i + wl == len(song): break
                i += 1

            if resp.has_key(song):
                break #we found some smallest substrings

    res = ['"' + resp[s][0] + '"' if resp.has_key(s) else ':(' for s in songs]

    return res


in_file = "D:\work\gcj\data\in.txt"
out_file = "D:\work\gcj\data\out.txt"
with open(in_file, 'r') as f:
    with open(out_file, 'w') as g:
        cases = int(f.readline())
        for case in xrange(cases):
            slen = int(f.readline())
            songs = [None] * slen

            for i in xrange(slen):
                songs[i] = f.readline().strip('\n')

            res = solve(songs)

            g.write('Case #' + str(case + 1) + ":\n")
            g.write('\n'.join(res))
            g.write('\n')
