package ro.calin.game.tetris;

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
    private int x = 0;
    private int y = 0;

    @Override
    public void init(TetrisInput input, TetrisCanvas canvas) {
        this.input = input;
        this.canvas = canvas;

        canvas.drawPlayArea();
    }

    @Override
    public void update(float deltaTime) {
        if(input.slideLeft()) x -= 1;
        if(input.slideRight()) x += 1;
        if (input.fallDown()) y += 5;
    }

    @Override
    public void draw() {
        canvas.drawBrickInPlayArea(x, y);
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
