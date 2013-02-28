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

    private final int brickWidth;
    private final int brickHeight;
    public static final String NEXT = "Next: ";
    private int smallBrickWidth;
    private final int smallBrickHeight;

    public AndroidTetrisCanvas(Bitmap frameBuffer) {
        this.paint = new Paint();
        this.canvas = new Canvas(frameBuffer);

        brickWidth = canvas.getWidth() / Const.PLAY_AREA_WIDTH;
        brickHeight = canvas.getHeight() / Const.PLAY_AREA_HEIGHT;
        smallBrickWidth = brickWidth / 3;
        smallBrickHeight = brickHeight / 3;

        paint.setTextSize(30);
    }

    public void clearScreen() {
        int color = 0xff000000;
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8,
                (color & 0xff));
    }

    @Override
    public void drawScore(int score) {
        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("Score: " +  score, 10, 30, paint);
    }

    @Override
    public void drawLevel(int level) {
        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText("Level: " +  level, 10, 60, paint);
    }

    @Override
    public void drawPlayArea() {
    }

    @Override
    public void drawNextPieceArea() {

        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(30);
        canvas.drawText(NEXT, canvas.getWidth() - brickWidth * 2, 30, paint);
    }

    @Override
    public void drawBrickInPlayArea(int line, int col) {
        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(col * brickWidth, line * brickHeight, + (col + 1) * brickWidth - 1, (line + 1) * brickHeight - 1, paint);
    }

    @Override
    public void drawBrickInNextPieceArea(int line, int col) {

        float startx = canvas.getWidth() - 10 - smallBrickWidth * Const.NEXT_PIECE_AREA_WIDTH;
        float starty = 40;

        paint.setColor(0xffffffff);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(startx + col * smallBrickWidth, starty + line * smallBrickHeight,
                startx + (col + 1) * smallBrickWidth - 1, starty + (line + 1) * smallBrickHeight - 1, paint);
    }
}
