package ro.calin.game.tetris;

import android.util.Log;
import ro.calin.game.Game;

/**
 * Created with IntelliJ IDEA.
 * User: calin
 * Date: 24.02.2013
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public class TetrisGame implements Game<TetrisInput, TetrisCanvas> {
    private TetrisInput input;
    private TetrisCanvas canvas;

    private int level = 1;
    private int score = 0;

    @Override
    public void init(TetrisInput input, TetrisCanvas canvas) {
        this.input = input;
        this.canvas = canvas;

        canvas.drawPlayArea();
        canvas.drawNextPieceArea();
    }

    @Override
    public void update(float deltaTime) {
        if (input.slideLeft()) {
            Log.d("TTT", "slideLeft!!!!!");
        }
        if (input.slideRight()) {
            Log.d("TTT", "slideRight!!!!!");
        }
        if (input.fallDown()) {
            Log.d("TTT", "fallDown!!!!!");
        }

        if (input.rotateClockwise()) {
            Log.d("TTT", "rotateClockwise!!!!!");
        }

        if (input.rotateCounterClockwise()) {
            Log.d("TTT", "rotateCounterClockwise!!!!!");
        }
    }

    @Override
    public void draw() {
        drawInPlayArea();
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
        //To change body of created methods use File | Settings | File Templates.
    }

    private void drawInPlayArea() {
        //To change body of created methods use File | Settings | File Templates.
    }

    @Override
    public void pause() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void resume() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
