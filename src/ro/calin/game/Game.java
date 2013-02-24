package ro.calin.game;

/**
 * Created with IntelliJ IDEA.
 * User: calin
 * Date: 24.02.2013
 * Time: 16:36
 * To change this template use File | Settings | File Templates.
 */
public interface Game<INPUT, CANVAS> {
    void init(INPUT input, CANVAS canvas);
    void update(float deltaTime);
    void draw();
    void pause();
    void resume();
}
