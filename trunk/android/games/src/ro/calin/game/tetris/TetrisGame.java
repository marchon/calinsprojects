package ro.calin.game.tetris;

import ro.calin.game.Game;

/**
 * Created with IntelliJ IDEA. User: calin Date: 24.02.2013 Time: 16:38 To change this template use File | Settings | File
 * Templates.
 */
public class TetrisGame implements Game<TetrisInput, TetrisCanvas> {

    private static final int FALL_TIME = 1000;
    public static final int SCORE_INTERVAL_FOR_LEVEL_INCREASE = 10;

    private TetrisInput input;
    private TetrisCanvas canvas;

    private byte[][] board = new byte[TetrisCanvas.Const.PLAY_AREA_HEIGHT][TetrisCanvas.Const.PLAY_AREA_WIDTH];
    private byte[][][] nextPiece;
    private byte[][][] currentPiece;
    private int nextOrientationIndex;
    private int currentOrientationIndex;
    private int currentPieceLine;
    private int currentPieceCol;
    private long lastFallTime;
    private int level = 1;
    private int score = 0;
    private boolean fastFallDown;
    private boolean pieceMoved;
    private int highestLine = 0;

    @Override
    public void init(TetrisInput input, TetrisCanvas canvas) {
        this.input = input;
        this.canvas = canvas;

        highestLine = board.length;

        chooseRandomPiece();
        prepareCurrentPiece();
    }

    @Override
    public void update(float deltaTime) {
        pieceMoved = false;

        if(!fastFallDown) {
            if (input.slideLeft()) {
                slideLeftIfPossible();
                pieceMoved = true;
            }
            else if (input.slideRight()) {
                slideRightIfPossible();
                pieceMoved = true;
            }
            else if (input.fallDown()) {
                fastFallDown = true;
            }
            else if (input.rotateClockwise()) {
                rotateClockwiseIfPossible();
                pieceMoved = true;
            }
            else if (input.rotateCounterClockwise()) {
                rotateCounterClockwiseIfPossible();
                pieceMoved = true;
            }
        }

        if(fallOneLineIfNeeded()) pieceMoved = true;

        if(pieceMoved) {
            if (pieceHitTheGround() || nextPositionWillOverlap()) {
                saveCurrentPieceToBoard();
                deleteCompleteRows();
                prepareCurrentPiece();
                if(firstPositionOverlaps()) {
                    restartGame();
                }
            }
        }
    }

    @Override
    public void draw() {
        if(pieceMoved) {
            canvas.clearScreen();

            canvas.drawPlayArea();
            drawBoard();

            canvas.drawNextPieceArea();
            drawNextPiece();

            drawScore();
            drawLevel();
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    private void chooseRandomPiece() {
        nextPiece = TetrisPieces.PIECES[((int) (Math.random() * TetrisPieces.PIECES.length))];
        nextOrientationIndex = (int) (Math.random() * nextPiece.length);
    }

    private void rotateCounterClockwiseIfPossible() {
        int oi = currentOrientationIndex - 1;
        if(oi == -1) oi = currentPiece.length - 1;

        if(currentPieceCol + currentPiece[oi][0].length < board[0].length &&
                !pieceOvelaps(currentPiece[oi], currentPieceLine, currentPieceCol)) {
            currentOrientationIndex = oi;
        }
    }

    private void rotateClockwiseIfPossible() {
        int oi = currentOrientationIndex + 1;
        if(oi == currentPiece.length) oi = 0;

        if(currentPieceCol + currentPiece[oi][0].length < board[0].length&&
                !pieceOvelaps(currentPiece[oi], currentPieceLine, currentPieceCol)) {
            currentOrientationIndex = oi;
        }
    }

    private void restartGame() {
        board = new byte[TetrisCanvas.Const.PLAY_AREA_HEIGHT][TetrisCanvas.Const.PLAY_AREA_WIDTH];
        score = 0;
        level = 1;
        prepareCurrentPiece();
    }

    private boolean firstPositionOverlaps() {
        return pieceOvelaps(currentPiece[currentOrientationIndex], currentPieceLine, currentPieceCol);
    }

    private void slideRightIfPossible() {
        if(currentPieceCol + currentPiece[currentOrientationIndex][0].length < board[0].length &&
                !pieceOvelaps(currentPiece[currentOrientationIndex], currentPieceLine, currentPieceCol + 1)) {
            currentPieceCol++;
        }
    }

    private void slideLeftIfPossible() {
        if(currentPieceCol > 0 &&
                !pieceOvelaps(currentPiece[currentOrientationIndex], currentPieceLine, currentPieceCol - 1)) {
            currentPieceCol--;
        }
    }

    private boolean fallOneLineIfNeeded() {
        final long currentTime = System.currentTimeMillis();
        long fallInterval = fastFallDown? 20 : FALL_TIME / level;
        if (currentTime - lastFallTime > fallInterval) {
            currentPieceLine++;
            lastFallTime = currentTime;
            return true;
        }
        return false;
    }

    private void deleteCompleteRows() {
        byte[][] piece = currentPiece[currentOrientationIndex];
        for (int line = 0; line < piece.length; line++) {
            boolean isRowFull = true;
            for(int col = 0; col < board[0].length; col ++) {
                if(board[currentPieceLine + line][col] == 0) {
                    isRowFull = false;
                    break;
                }
            }
            if(isRowFull) {
                deleteRow(currentPieceLine + line);
                increaseScore();
            }
        }
    }

    private void increaseScore() {
        score ++;
        if(score % SCORE_INTERVAL_FOR_LEVEL_INCREASE == 0) {
            level ++;
        }
    }

    private void deleteRow(int row) {
        if(highestLine - 1 < row) {
            for(int line = row; line > highestLine - 1; line--) {
                for(int col = 0; col < board[line].length; col++) {
                    board[line][col] = board[line - 1][col];
                }
            }
        }
        highestLine++;
    }

    private boolean nextPositionWillOverlap() {
        return pieceOvelaps(currentPiece[currentOrientationIndex], currentPieceLine + 1, currentPieceCol);
    }

    private void saveCurrentPieceToBoard() {
        byte[][] piece = currentPiece[currentOrientationIndex];
        for (int line = 0; line < piece.length; line++) {
            for (int col = 0; col < piece[line].length; col++) {
                if (piece[line][col] == 1) {
                    board[currentPieceLine + line][currentPieceCol + col] = piece[line][col];
                }
            }
        }
        if(highestLine > currentPieceLine) {
            highestLine = currentPieceLine;
        }
    }

    private void prepareCurrentPiece() {
        currentPiece = nextPiece;
        currentPieceLine = 0;
        currentPieceCol = 4; //TODO: try to center
        currentOrientationIndex = nextOrientationIndex;

        fastFallDown = false;

        chooseRandomPiece();
    }

    private boolean pieceHitTheGround() {
        return currentPieceLine + currentPiece[currentOrientationIndex].length == board.length;
    }

    private boolean pieceOvelaps(byte[][] piece, int line, int col) {
        for (int l = 0; l < piece.length; l++) {
            for (int c = 0; c < piece[l].length; c++) {
                if (piece[l][c] != 0 && board[line + l][col + c] != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void drawLevel() {
        canvas.drawLevel(level);
    }

    private void drawScore() {
        canvas.drawScore(score);
    }

    private void drawNextPiece() {
        byte[][] piece = nextPiece[nextOrientationIndex];
        for (int line = 0; line < piece.length; line++) {
            for (int col = 0; col < piece[line].length; col++) {
                if (piece[line][col] != 0) {
                    canvas.drawBrickInNextPieceArea(line, col);
                }
            }
        }
    }

    private void drawBoard() {
        for (int line = 0; line < board.length; line++) {
            for (int col = 0; col < board[line].length; col++) {
                if (board[line][col] != 0) {
                    canvas.drawBrickInPlayArea(line, col);
                }
            }
        }

        byte[][] piece = currentPiece[currentOrientationIndex];
        for (int line = 0; line < piece.length; line++) {
            for (int col = 0; col < piece[line].length; col++) {
                if (piece[line][col] != 0) {
                    canvas.drawBrickInPlayArea(currentPieceLine + line, currentPieceCol + col);
                }
            }
        }
    }


}
