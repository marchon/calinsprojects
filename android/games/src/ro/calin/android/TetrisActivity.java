package ro.calin.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import ro.calin.android.adaptor.AndroidTetrisCanvas;
import ro.calin.android.adaptor.AndroidTetrisInput;
import ro.calin.game.Game;
import ro.calin.game.tetris.TetrisGame;

public class TetrisActivity extends Activity
{
    private AndroidFastRenderView view;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;

        int frameBufferWidth = isPortrait ? 800: 1280;
        int frameBufferHeight = isPortrait ? 1280: 800;

        Bitmap frameBuffer = Bitmap.createBitmap(frameBufferWidth,
                frameBufferHeight, Bitmap.Config.RGB_565);

//        float scaleX = (float) frameBufferWidth
//                / getWindowManager().getDefaultDisplay().getWidth();
//        float scaleY = (float) frameBufferHeight
//                / getWindowManager().getDefaultDisplay().getHeight();

        view = new AndroidFastRenderView(this, frameBuffer, new TetrisGame());

        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        view.pause();
    }

    public static class AndroidFastRenderView extends SurfaceView implements Runnable {
        Thread renderThread = null;
        volatile boolean running = false;
        private final Bitmap framebuffer;
        private final SurfaceHolder holder;
        private final Game game;
        private AndroidTetrisInput tetrisInput;
        private AndroidTetrisCanvas tetrisCanvas;

        public AndroidFastRenderView(Activity activity, Bitmap framebuffer, Game game) {
            super(activity);
            this.framebuffer = framebuffer;
            this.game = game;
            this.holder = getHolder();

            tetrisInput = new AndroidTetrisInput();
            tetrisCanvas = new AndroidTetrisCanvas(framebuffer);

            game.init(tetrisInput, tetrisCanvas);

            this.setOnTouchListener(tetrisInput);
        }

        public void resume() {
            running = true;
            renderThread = new Thread(this);
            renderThread.start();
        }

        public void run() {
            Rect dstRect = new Rect();
            long startTime = System.nanoTime();
            while(running) {
                if(!holder.getSurface().isValid())
                    continue;

                float deltaTime = (System.nanoTime() - startTime) / 10000000.000f;
                startTime = System.nanoTime();

                if (deltaTime > 3.15){
                    deltaTime = (float) 3.15;
                }

                game.update(deltaTime);
                game.draw();

                Canvas canvas = holder.lockCanvas();
                canvas.getClipBounds(dstRect);
                canvas.drawBitmap(framebuffer, null, dstRect, null);
                holder.unlockCanvasAndPost(canvas);
            }
        }

        public void pause() {
            running = false;
            while(true) {
                try {
                    renderThread.join();
                    break;
                } catch (InterruptedException e) {
                    // retry
                }
            }
        }


    }
}
