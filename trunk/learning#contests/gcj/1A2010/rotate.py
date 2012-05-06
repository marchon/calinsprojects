#http://code.google.com/codejam/contest/544101/dashboard#s=p0

__author__ = 'Calin'

def disp(board):
    for i in xrange(len(board)):
        print board[i]

def fall(board):
    N = len(board)
    for col in reversed(xrange(N - 1)):
        for row in xrange(N):
            if board[row][col] != '.':
                c = col
                while c + 1 < N and board[row][c + 1] == '.':
                    board[row][c + 1] = board[row][c]
                    board[row][c] = '.'
                    c += 1
    return board

def has_won(board, piece, x):
    N = len(board)
    for row in xrange(N):
        for col in xrange(N):
            r = c = d1 = d2 = True
            for delta in xrange(x):
                if col + delta == N: r = d1 = False
                if row + delta == N: c = d1 = d2 = False
                if col - delta == -1: d2 = False

                if r and not board[row][col + delta] == piece: r = False
                if c and not board[row + delta][col] == piece: c = False
                if d1 and not board[row + delta][col + delta] == piece: d1 = False
                if d2 and not board[row + delta][col - delta] == piece: d2 = False

                if not r and not c and not d1 and not d2: break

            if r or c or d1 or d2: return True

    return False

def solve(board, x):
    board = fall(board)
    r = has_won(board, 'R', x)
    b = has_won(board, 'B', x)

    if r and b: return 'Both'
    elif r: return 'Red'
    elif b: return 'Blue'
    else: return 'Neither'

cases = int(raw_input())

for c in xrange(cases):
    line = [int(a) for a in raw_input().split()]
    board = []
    for i in xrange(line[0]):
        board.append(list(raw_input()))

    res = solve(board, line[1])

    print "Case #%d: %s" % (c + 1, res)
