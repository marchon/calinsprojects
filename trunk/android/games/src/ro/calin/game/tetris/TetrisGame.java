package ro.calin.game.tetris;

import android.util.Log;
import ro.calin.game.Game;

/**
 * Created with IntelliJ IDEA. User: calin Date: 24.02.2013 Time: 16:38 To change this template use File | Settings | File
 * Templates.
 */
public class TetrisGame implements Game<TetrisInput, TetrisCanvas> {

    private TetrisInput input;
    private TetrisCanvas canvas;

    private byte[][] game = new byte[TetrisCanvas.Const.PLAY_AREA_WIDTH][TetrisCanvas.Const.PLAY_AREA_HEIGHT];

    private static byte[][][] pieces = {
            {
                {0,0,0,0},
                {0,0,1,0},
                {0,0,1,1},
                {0,1,1,0},
            }
    };

    private byte[][] currentPiece;


    private int level = 1;
    private int score = 0;

    @Override
    public void init(TetrisInput input, TetrisCanvas canvas) {
        this.input = input;
        this.canvas = canvas;
        currentPiece = pieces[0];

        for (int i = 0; i < TetrisCanvas.Const.NEXT_PIECE_AREA_WIDTH; i++) {
            for (int j = 0; j < TetrisCanvas.Const.NEXT_PIECE_AREA_HEIGHT; j++) {
                game[4 + i][j] = currentPiece[i][j];
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        if (input.slideLeft()) {
            Log.d("TTT", "slideLeft!!!!!");
        }
        else if (input.slideRight()) {
            Log.d("TTT", "slideRight!!!!!");
        }
        else if (input.fallDown()) {
            Log.d("TTT", "fallDown!!!!!");
        }
        else if (input.rotateClockwise()) {
            Log.d("TTT", "rotateClockwise!!!!!");
        }
        else if (input.rotateCounterClockwise()) {
            Log.d("TTT", "rotateCounterClockwise!!!!!");
        }
    }

    @Override
    public void draw() {
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
        for (int i = 0; i < TetrisCanvas.Const.NEXT_PIECE_AREA_WIDTH; i++) {
            for (int j = 0; j < TetrisCanvas.Const.NEXT_PIECE_AREA_HEIGHT; j++) {
                if (currentPiece[i][j] != 0) {
                    //i is line -> y
                    //j is column -> x => need to reverse
                    canvas.drawBrickInNextPieceArea(j, i);
                }
            }
        }
    }

    private void drawInPlayArea() {
        for (int i = 0; i < TetrisCanvas.Const.PLAY_AREA_WIDTH; i++) {
            for (int j = 0; j < TetrisCanvas.Const.PLAY_AREA_HEIGHT; j++) {
                if (game[i][j] != 0) {
                    canvas.drawBrickInPlayArea(j, i);
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
