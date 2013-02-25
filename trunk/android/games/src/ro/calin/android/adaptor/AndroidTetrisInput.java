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


    private float startX;
    private float prevX;
    private float startY;
    private float prevY;
    private int count;

    private static enum State {
        INIT, RIGHT_DOWN, LEFT_DOWN, LEFT_UP, RIGHT_UP, END
    }

    private State clockwiseRotationState;
    private int clockwiseTransitionCount;

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
                count = 1;
                startX = prevX = event.getX();
                startY = prevY = event.getY();
                clockwiseRotationState = State.INIT;
                clockwiseTransitionCount = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                float y = event.getY();
                count ++;
                processClockwiseAction(x, y);

                prevX = x;
                prevY = y;

                if(clockwiseTransitionCount >= 3 && clockwiseRotationState != State.END) {
                    rotateClockwise = true;
                    clockwiseRotationState = State.END;
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d("TTT", "Events: " + count);
                Log.d("TTT", "State: " + clockwiseRotationState);
                break;
        }

        return true;
    }

    private void processClockwiseAction(float x, float y) {
        switch (clockwiseRotationState) {
            case INIT:
                if(x > prevX && y > prevY) changeState(State.RIGHT_DOWN);
                if(x > prevX && y < prevY) changeState(State.RIGHT_UP);
                if(x < prevX && y < prevY) changeState(State.LEFT_UP);
                if(x < prevX && y > prevY) changeState(State.LEFT_DOWN);
//                else clockwiseRotationState = State.END;
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

    private void changeState(State state) {
        clockwiseRotationState = state;
        clockwiseTransitionCount ++;
    }

    //TODO: use this interface
    private interface GestureDetector {
        void start(float x, float y);
        void process(float x, float y);
        boolean isDetected();
    }
}
