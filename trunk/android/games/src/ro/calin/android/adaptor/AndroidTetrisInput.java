package ro.calin.android.adaptor;

import android.view.MotionEvent;
import android.view.View;
import ro.calin.game.tetris.TetrisInput;

/**
 * Created with IntelliJ IDEA.
 * User: calin
 * Date: 24.02.2013
 * Time: 20:55
 * To change this template use File | Settings | File Templates.
 */
public class AndroidTetrisInput implements TetrisInput, View.OnTouchListener {
    private static final int SLIDE_LEFT = 0;
    private static final int SLIDE_RIGHT = 1;
    private static final int FALL_DOWN = 2;
    private static final int ROTATE_CLOCKWISE = 3;
    private static final int ROTATE_COUNTER_CLOCKWISE = 4;

    private boolean gestureDetected;

    private GestureDetector[] detectors = { new SlideLeftDetector(), new SlideRightDetector(), new SlideDownDetector(),
                                           new ClockwiseRotationDetector(), new CounterClockwiseRotationDetector() };
    private volatile boolean[] gestureFlags = { false, false, false, false, false };

    @Override
    public boolean slideLeft() {
        if(gestureFlags[SLIDE_LEFT]) {
            gestureFlags[SLIDE_LEFT] = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean slideRight() {
        if(gestureFlags[SLIDE_RIGHT]) {
            gestureFlags[SLIDE_RIGHT] = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean fallDown() {
        if(gestureFlags[FALL_DOWN]) {
            gestureFlags[FALL_DOWN] = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean rotateClockwise() {
        if(gestureFlags[ROTATE_CLOCKWISE]) {
            gestureFlags[ROTATE_CLOCKWISE] = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean rotateCounterClockwise() {
        if(gestureFlags[ROTATE_COUNTER_CLOCKWISE]) {
            gestureFlags[ROTATE_COUNTER_CLOCKWISE] = false;
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gestureDetected = false;
                for (GestureDetector detector : detectors) {
                    detector.startDetection(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (gestureDetected) break;

                for (int i = 0; i < detectors.length; i++) {
                    GestureDetector detector = detectors[i];
                    detector.addPoint(event.getX(), event.getY());
                    if(detector.isDetected()) {
                        gestureFlags[i] = true;
                        gestureDetected = true;
                    }
                }
                break;
        }

        return true;
    }

    private static interface GestureDetector {
        void startDetection(float x, float y);
        void addPoint(float x, float y);
        boolean isDetected();
    }

    private static abstract class RotationDetector implements GestureDetector {
        public static final int SUFFICIENT_TRANSITION_NUMBER = 3;
        protected float prevX;
        protected float prevY;
        private boolean detected;

        protected static enum State {
            INIT, RIGHT_DOWN, LEFT_DOWN, LEFT_UP, RIGHT_UP, END
        }

        protected State rotationState;
        private int transitionCount;

        @Override
        public void startDetection(float x, float y) {
            prevX = x;
            prevY = y;
            rotationState = State.INIT;
            transitionCount = 0;
        }

        @Override
        public void addPoint(float x, float y) {
            makeTransition(x, y);

            prevX = x;
            prevY = y;

            detected = transitionCount >= SUFFICIENT_TRANSITION_NUMBER;
        }

        protected abstract void makeTransition(float x, float y);

        protected void changeState(State state) {
            rotationState = state;
            transitionCount++;
        }

        @Override
        public boolean isDetected() {
            return detected;
        }
    }

    private class ClockwiseRotationDetector extends RotationDetector {
        @Override
        protected void makeTransition(float x, float y) {
            switch (rotationState) {
                case INIT:
                    if(x > prevX && y > prevY) changeState(State.RIGHT_DOWN);
                    if(x < prevX && y > prevY) changeState(State.LEFT_DOWN);
                    if(x < prevX && y < prevY) changeState(State.LEFT_UP);
                    if(x > prevX && y < prevY) changeState(State.RIGHT_UP);
                    break;
                case RIGHT_DOWN:
                    if(x >= prevX && y >= prevY);
                    else if(x < prevX && y >= prevY) changeState(State.LEFT_DOWN);
                    else rotationState = State.END;
                    break;
                case LEFT_DOWN:
                    if(x <= prevX && y >= prevY);
                    else if(x <= prevX && y < prevY) changeState(State.LEFT_UP);
                    else rotationState = State.END;
                    break;
                case LEFT_UP:
                    if(x <= prevX && y <= prevY);
                    else if(x > prevX && y <= prevY) changeState(State.RIGHT_UP);
                    else rotationState = State.END;
                    break;
                case RIGHT_UP:
                    if(x >= prevX && y <= prevY);
                    else if(x >= prevX && y > prevY) changeState(State.RIGHT_DOWN);
                    else rotationState = State.END;
                    break;
            }
        }
    }

    private class CounterClockwiseRotationDetector extends RotationDetector {
        @Override
        protected void makeTransition(float x, float y) {
            switch (rotationState) {
                case INIT:
                    if(x < prevX && y > prevY) changeState(State.LEFT_DOWN);
                    if(x > prevX && y > prevY) changeState(State.RIGHT_DOWN);
                    if(x > prevX && y < prevY) changeState(State.RIGHT_UP);
                    if(x < prevX && y < prevY) changeState(State.LEFT_UP);
                    break;
                case LEFT_DOWN:
                    if(x <= prevX && y >= prevY);
                    else if(x > prevX && y >= prevY) changeState(State.RIGHT_DOWN);
                    else rotationState = State.END;
                    break;
                case RIGHT_DOWN:
                    if(x >= prevX && y >= prevY);
                    else if(x >= prevX && y < prevY) changeState(State.RIGHT_UP);
                    else rotationState = State.END;
                    break;
                case RIGHT_UP:
                    if(x >= prevX && y <= prevY);
                    else if(x < prevX && y <= prevY) changeState(State.LEFT_UP);
                    else rotationState = State.END;
                    break;
                case LEFT_UP:
                    if(x <= prevX && y <= prevY);
                    else if(x <= prevX && y > prevY) changeState(State.LEFT_DOWN);
                    else rotationState = State.END;
                    break;
            }
        }
    }

    private static class SlideLeftDetector implements GestureDetector {
        @Override
        public void startDetection(float x, float y) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void addPoint(float x, float y) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean isDetected() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private static class SlideRightDetector implements GestureDetector {
        @Override
        public void startDetection(float x, float y) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void addPoint(float x, float y) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean isDetected() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    private class SlideDownDetector implements GestureDetector {
        @Override
        public void startDetection(float x, float y) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void addPoint(float x, float y) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public boolean isDetected() {
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
