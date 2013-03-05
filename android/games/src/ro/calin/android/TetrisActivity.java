package ro.calin.android;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import ro.calin.android.adaptor.AndroidTetrisCanvas;
import ro.calin.android.adaptor.AndroidTetrisInput;
import ro.calin.game.Game;
import ro.calin.game.tetris.TetrisGame;

public class TetrisActivity extends Activity {
    private AndroidGameView<AndroidTetrisInput, AndroidTetrisCanvas> view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int height = dm.heightPixels;
        final int width = dm.widthPixels;

        Bitmap frameBuffer = Bitmap.createBitmap(width,
                height, Bitmap.Config.RGB_565);

        TetrisGame tetrisGame = new TetrisGame();
        AndroidTetrisCanvas canvas = new AndroidTetrisCanvas(frameBuffer);
        AndroidTetrisInput input = new AndroidTetrisInput();
        view = new AndroidGameView(this, tetrisGame, input, canvas);

        view.setOnTouchListener(input);
        this.setContentView(view);
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
}
