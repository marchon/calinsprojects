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
    private boolean leftPushed;
    private boolean rightPushed;
    private boolean downPused;

    private float x1, x2, y1, y2, dx, dy;

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
    public boolean rotateLeft() {
        return false;
    }

    @Override
    public boolean rotateRight() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public synchronized boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                break;
            case (MotionEvent.ACTION_UP): {
                x2 = event.getX();
                y2 = event.getY();
                dx = x2 - x1;
                dy = y2 - y1;

                // Use dx and dy to determine the direction
                if (Math.abs(dx) > Math.abs(dy)) {
                    if (dx > 0) rightPushed = true;
                    else leftPushed = true;
                } else {
                    if (dy > 0) downPused = true;
                    else {
                    }
                    ;
                }
            }
        }

        return true;
    }
}
