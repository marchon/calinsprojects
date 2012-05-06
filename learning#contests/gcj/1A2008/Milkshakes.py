#http://code.google.com/codejam/contest/32016/dashboard#s=p1

__author__ = 'Calin'

def solve(n, prefs):
    sol = [0] * n

    # if there is an unmalted preference, it will be at the end
    for pref in prefs:
        for i in xrange(len(pref)):
            t,f = pref[i]
            if f == 1:
                pref[i] = pref[-1]
                pref[-1] = t,f
                break

    finished = False

    while not finished:
        finished = True
        for pref in prefs:
            satisfied = False

            all_flavors_malted = True
            for t,f in pref:
                if sol[t - 1] == f:
                    satisfied = True
                    break
                elif sol[t - 1] == 0:
                    all_flavors_malted = False

            if not satisfied:
                finished = False

                if pref[-1][1] == 0 and all_flavors_malted:
                    return "IMPOSSIBLE"

                if pref[-1][1] == 1:
                    sol[pref[-1][0] - 1] = 1

    return sol

def group(lst, n):
    ret = []
    for i in range(0, len(lst), n):
        val = lst[i:i+n]
        ret.append(tuple(val))
    return ret

in_file = "D:\work\gcj\data\in.txt"
out_file = "D:\work\gcj\data\out.txt"
with open(in_file, 'r') as f:
    with open(out_file, 'w') as g:
        cases = int(f.readline())

        for case in xrange(cases):
            n, cust = int(f.readline()), int(f.readline())

            prefs = [group([int(a) for a in f.readline().split()[1:]], 2) for c in range(cust)]

            res = 'Case #' + str(case + 1) + ": " + str(solve(n, prefs)).replace(',', '').strip('[]')

            g.write(res + '\n')
