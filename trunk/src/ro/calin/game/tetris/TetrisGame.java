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

    @Override
    public void init(TetrisInput input, TetrisCanvas canvas) {
        this.input = input;
        this.canvas = canvas;
    }

    @Override
    public void update(float deltaTime) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void draw() {
        //To change body of implemented methods use File | Settings | File Templates.
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
