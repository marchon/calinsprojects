package ro.calin.android.adaptor.gestures;

/**
* Created with IntelliJ IDEA.
* User: calin
* Date: 26.02.2013
* Time: 22:55
* To change this template use File | Settings | File Templates.
*/
public abstract class SwipeDetector implements GestureDetector {
    protected static int MAX_DEV = 30;
    protected static int MIN_DIST = 110;

    protected float startX;
    protected float startY;
    protected float prevX;
    protected float prevY;

    private boolean detected;
    private boolean notThisTypeOfSwipe;

    @Override
    public void start(float x, float y) {
        startX = prevX = x;
        startY = prevY = y;
        detected = false;
        notThisTypeOfSwipe = false;
    }

    @Override
    public void step(float x, float y) {
        if(notThisTypeOfSwipe || detected) return;

        if(notThisTypeOfSwipeCondition(x, y)) {
            notThisTypeOfSwipe = true;
        }

        prevX = x;
        prevY = y;

        detected = swipeDetectedCondition(x, y);
    }

    protected abstract boolean swipeDetectedCondition(float x, float y);

    protected abstract boolean notThisTypeOfSwipeCondition(float x, float y);

    @Override
    public boolean detected() {
        return detected;
    }

    public static class SwipeLeftDetector extends SwipeDetector {
        @Override
        protected boolean swipeDetectedCondition(float x, float y) {
            return startX - x >= MIN_DIST;
        }

        @Override
        protected boolean notThisTypeOfSwipeCondition(float x, float y) {
            return Math.abs(y - startY) > MAX_DEV;
        }
    }

    public static class SwipeRightDetector extends SwipeDetector {
        @Override
        protected boolean swipeDetectedCondition(float x, float y) {
            return x - startX >= MIN_DIST;
        }

        @Override
        protected boolean notThisTypeOfSwipeCondition(float x, float y) {
            return Math.abs(y - startY) > MAX_DEV;
        }
    }

    public static class SwipeDownDetector extends SwipeDetector {
        @Override
        protected boolean swipeDetectedCondition(float x, float y) {
            return y - startY >= MIN_DIST;
        }

        @Override
        protected boolean notThisTypeOfSwipeCondition(float x, float y) {
            return Math.abs(x - startX) > MAX_DEV;
        }
    }
}
