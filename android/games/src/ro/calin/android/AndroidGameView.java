package ro.calin.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ro.calin.game.Game;

/**
 * Created with IntelliJ IDEA.
 * User: calin
 * Date: 05.03.2013
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */
public class AndroidGameView<I, C extends AndroidGameCanvas> extends SurfaceView implements Runnable {
    private final Game<I, C> game;
    private final Bitmap buffer;
    private volatile boolean running;
    private Thread renderThread;
    private final SurfaceHolder holder;

    public AndroidGameView(Context context, Game<I, C> game, I input, C canvas) {
        super(context);
        this.game = game;
        game.init(input, canvas);
        this.buffer = canvas.getFrameBuffer();
        this.holder = getHolder();
    }

    public void resume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
        game.resume();
    }

    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while (running) {
            if (!holder.getSurface().isValid())
                continue;

            float deltaTime = (System.nanoTime() - startTime) / 10000000.000f;
            startTime = System.nanoTime();

            if (deltaTime > 3.15) {
                deltaTime = (float) 3.15;
            }

            game.update(deltaTime);
            game.draw();

            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(buffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
        }
        game.pause();
    }
}
