
public class Tothello {
	private void fillBoard(int[][] board, String[] coords, int value) {
		for (int i = 0; i < coords.length; i++) {
			int column = coords[i].charAt(0) - 'A';
			int row = coords[i].charAt(1) - '1';
			board[row][column] = value;
		}
	}
	
	private void printBoard(int[][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print(board[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	/**
	 * @param board
	 * @param i
	 * @param j
	 * @param di
	 * @param dj
	 * @return the score obtained if a 1 would be put in place
	 * of the first 0 encountered by adding di/dj to i/j
	 */
	private int score(int[][] board, int i, int j, int di, int dj, int flag) {
		//must move
		if(di == dj && di == 0)
			return 0;
		
		int ii = i, jj = j;
		
		int score = 0;
		
		do {
			ii += di;
			jj += dj;
			
			//out of the board
			if(ii < 0 || ii >= 8 || jj < 0 || jj >= 8) return 0;
			
			score ++;
			
		}while(board[ii][jj] == 2);
		
		//flag is 0 when we must place piece, 1 if we backtrack to 
		//calculate whole score
		if(board[ii][jj] == flag) {
			do {
				ii -= di;
				jj -= dj;
			} while(i != ii && j != jj);
		} else {
			//no score
			return 0;
		}
		
		return score;
	}
	
	int bestMove(String[] redPieces, String[] blackPieces, String whoseTurn) {
		String[] movingPlayer = redPieces;
		String[] otherPlayer = blackPieces;
		
		//whose turn it is?
		if(whoseTurn.equals("Black")) {
			movingPlayer = blackPieces;
			otherPlayer = redPieces;
		}
		
		int[][] board = new int[8][8];
		
		//fill the board
		fillBoard(board, movingPlayer, 1);
		fillBoard(board, otherPlayer, 2);
		
		printBoard(board);
		
		return 0;
	}
	
	public static void main(String[] args) {
		int res = new Tothello().bestMove(
				"C2,C3,C4,C5,D4,E4,F2,F3,F4,F5,G6".split(","), 
				"B1,E1,G1,C6,H7,G4".split(","), "Black");
		
		System.out.println(res);
	}

}
