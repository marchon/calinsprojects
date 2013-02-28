package ro.calin.game.tetris;

import android.util.Log;
import ro.calin.game.Game;

/**
 * Created with IntelliJ IDEA. User: calin Date: 24.02.2013 Time: 16:38 To change this template use File | Settings | File
 * Templates.
 */
public class TetrisGame implements Game<TetrisInput, TetrisCanvas> {

    private static final int FALL_TIME = 1000;
    private static byte[][][][] pieces = {
            {
                {
                        {0,1},
                        {0,1},
                        {1,1}
                },
                {
                        {1,0,0},
                        {1,1,1}
                }
            },
            {{
                {1},
                {1},
                {1},
                {1}
            }},
            {{
                {0,1,0},
                {1,1,1}
            }}
    };
    private TetrisInput input;
    private TetrisCanvas canvas;

    private byte[][] board = new byte[TetrisCanvas.Const.PLAY_AREA_HEIGHT][TetrisCanvas.Const.PLAY_AREA_WIDTH];
    private byte[][][] nextPiece;
    private byte[][][] currentPiece;
    private int currentOrientationIndex = 0;
    private int currentPieceLine;
    private int currentPieceCol;
    private long lastFallTime;
    private int level = 1;
    private int score = 0;
    private boolean fastFallDown;

    @Override
    public void init(TetrisInput input, TetrisCanvas canvas) {
        this.input = input;
        this.canvas = canvas;

        nextPiece = generateRandomPiece();
        prepareCurrentPiece();
    }

    private byte[][][] generateRandomPiece() {
        return pieces[((int) (Math.random() * pieces.length))];
    }

    @Override
    public void update(float deltaTime) {
        boolean pieceMoved = false;

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
                if(!topIsReached()) {
                    prepareCurrentPiece();
                } else {
                    endGame();
                }
            }
        }
    }

    private void rotateCounterClockwiseIfPossible() {
        Log.d("TTT", "rotateCounterClockwise!!!!!");
    }

    private void rotateClockwiseIfPossible() {
        Log.d("TTT", "rotateClockwise!!!!!");
    }

    private void endGame() {
        //To change body of created methods use File | Settings | File Templates.
    }

    private boolean topIsReached() {
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    private void slideRightIfPossible() {
        currentPieceCol++;
    }

    private void slideLeftIfPossible() {
        currentPieceCol--;
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
        //To change body of created methods use File | Settings | File Templates.
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
    }

    private void prepareCurrentPiece() {
        currentPiece = nextPiece;
        nextPiece = generateRandomPiece();
        currentPieceLine = 0;
        currentPieceCol = 4;
        fastFallDown = false;
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

    @Override
    public void draw() {
        //TODO: if updated!!!
        canvas.clearScreen();

        canvas.drawPlayArea();
        drawInPlayArea();

        canvas.drawNextPieceArea();
        drawInNextArea();

        drawScore();
        drawLevel();
    }

    private void drawLevel() {
        canvas.drawLevel(level);
    }

    private void drawScore() {
        canvas.drawScore(score);
    }

    private void drawInNextArea() {
        byte[][] piece = nextPiece[currentOrientationIndex];
        for (int line = 0; line < piece.length; line++) {
            for (int col = 0; col < piece[line].length; col++) {
                if (piece[line][col] != 0) {
                    canvas.drawBrickInNextPieceArea(line, col);
                }
            }
        }
    }

    private void drawInPlayArea() {
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

    @Override
    public void pause() {
        // To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        // To change body of implemented methods use File | Settings | File Templates.
    }
}
