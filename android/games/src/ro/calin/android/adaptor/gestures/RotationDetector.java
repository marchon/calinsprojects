package ro.calin.android.adaptor.gestures;

/**
* Created with IntelliJ IDEA.
* User: calin
* Date: 26.02.2013
* Time: 22:54
* To change this template use File | Settings | File Templates.
*/
public abstract class RotationDetector implements GestureDetector {
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
    public void start(float x, float y) {
        prevX = x;
        prevY = y;
        rotationState = State.INIT;
        transitionCount = 0;
        detected = false;
    }

    @Override
    public void step(float x, float y) {
        if(detected) return;

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
    public boolean detected() {
        return detected;
    }

    public static class ClockwiseRotationDetector extends RotationDetector {
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

    public static class CounterClockwiseRotationDetector extends RotationDetector {
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
}
