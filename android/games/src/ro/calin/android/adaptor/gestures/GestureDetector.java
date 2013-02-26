package ro.calin.android.adaptor.gestures;

/**
* Created with IntelliJ IDEA.
* User: calin
* Date: 26.02.2013
* Time: 22:53
* To change this template use File | Settings | File Templates.
*/
public interface GestureDetector {
    void start(float x, float y);
    void step(float x, float y);
    boolean detected();
}
