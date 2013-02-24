package ro.calin.android.adaptor;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import ro.calin.game.tetris.TetrisCanvas;

/**
 * Created with IntelliJ IDEA.
 * User: calin
 * Date: 24.02.2013
 * Time: 17:01
 * To change this template use File | Settings | File Templates.
 */
public class AndroidTetrisCanvas implements TetrisCanvas {
    private final Paint paint;
    private final Canvas canvas;

    public AndroidTetrisCanvas(Bitmap frameBuffer) {
        this.paint = new Paint();
        this.canvas = new Canvas(frameBuffer);
    }

    private void drawRect(int x, int y, int width, int height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    private void clearScreen(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawScore(int score) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawLevel(int level) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawPlayArea() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawNextPieceArea() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawBrickInPlayArea(int x, int y) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void drawBrickInNextPieceArea(int x, int y) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
