package ro.calin.game.tetris;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: calin
 * Date: 24.02.2013
 * Time: 16:38
 * To change this template use File | Settings | File Templates.
 */
public interface TetrisInput {
    boolean slideLeft();
    boolean slideRight();
    boolean fallDown();
    boolean rotateClockwise();
    boolean rotateCounterClockwise();
}
