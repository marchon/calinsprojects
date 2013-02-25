package ro.calin.android.adaptor;

import android.util.Log;
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
    private boolean leftPushed;
    private boolean rightPushed;
    private boolean downPused;
    private boolean rotateClockwise;
    private boolean gestureDetected;

    private ClockwiseRotationDetector clockwiseRotationDetector = new ClockwiseRotationDetector();

    @Override
    public boolean slideLeft() {
        if(leftPushed) {
            leftPushed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean slideRight() {
        if(rightPushed) {
            rightPushed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean fallDown() {
        if(downPused) {
            downPused = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean rotateClockwise() {
        if(rotateClockwise) {
            rotateClockwise = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean rotateAntiClockwise() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public synchronized boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                gestureDetected = false;
                clockwiseRotationDetector.startDetection(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (gestureDetected) break;
                clockwiseRotationDetector.addPoint(event.getX(), event.getY());
                if(clockwiseRotationDetector.isDetected()) {
                    rotateClockwise = true;
                    gestureDetected = true;
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

        protected State clockwiseRotationState;
        private int clockwiseTransitionCount;

        @Override
        public void startDetection(float x, float y) {
            prevX = x;
            prevY = y;
            clockwiseRotationState = State.INIT;
            clockwiseTransitionCount = 0;
        }

        @Override
        public void addPoint(float x, float y) {
            makeTransition(x, y);

            prevX = x;
            prevY = y;

            detected = clockwiseTransitionCount >= SUFFICIENT_TRANSITION_NUMBER;
        }

        protected abstract void makeTransition(float x, float y);

        protected void changeState(State state) {
            clockwiseRotationState = state;
            clockwiseTransitionCount ++;
        }

        @Override
        public boolean isDetected() {
            return detected;
        }
    }

    private class ClockwiseRotationDetector extends RotationDetector {
        @Override
        protected void makeTransition(float x, float y) {
            switch (clockwiseRotationState) {
                case INIT:
                    if(x > prevX && y > prevY) changeState(State.RIGHT_DOWN);
                    if(x > prevX && y < prevY) changeState(State.RIGHT_UP);
                    if(x < prevX && y < prevY) changeState(State.LEFT_UP);
                    if(x < prevX && y > prevY) changeState(State.LEFT_DOWN);
                    break;
                case RIGHT_DOWN:
                    if(x >= prevX && y >= prevY);
                    else if(x < prevX && y >= prevY) changeState(State.LEFT_DOWN);
                    else clockwiseRotationState = State.END;
                    break;
                case LEFT_DOWN:
                    if(x <= prevX && y >= prevY);
                    else if(x <= prevX && y < prevY) changeState(State.LEFT_UP);
                    else clockwiseRotationState = State.END;
                    break;
                case LEFT_UP:
                    if(x <= prevX && y <= prevY);
                    else if(x > prevX && y <= prevY) changeState(State.RIGHT_UP);
                    else clockwiseRotationState = State.END;
                    break;
                case RIGHT_UP:
                    if(x >= prevX && y <= prevY);
                    else if(x >= prevX && y > prevY) changeState(State.RIGHT_DOWN);
                    else clockwiseRotationState = State.END;
                    break;
            }
        }
    }
}
