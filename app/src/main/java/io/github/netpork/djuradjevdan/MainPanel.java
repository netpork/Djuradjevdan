package io.github.netpork.djuradjevdan;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by netpork on 12/8/14.
 */
public class MainPanel extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private static final String TAG = "MainPanel";
    private MainThread mThread;

    public static int screenWidth, screenHeight;
    public static float density;
    public String avgFps;

    public Djuradj djuradj;
    public Video mVideo;

    public float lastTouchY = 0;


    public MainPanel(Context context, DisplayMetrics metrics) {
        super(context);

        density = metrics.density;
        getHolder().addCallback(this);
        setFocusable(true);
        setOnTouchListener(this);

        mThread = new MainThread(getHolder(), this);
    }

     public void update(Canvas canvas) {
        djuradj.update(canvas);
    }

    public void render(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);

        djuradj.render(Video.mCanvas);

        Video.update(canvas);

        displayFps(canvas, avgFps);
    }

    public void startThread() {
        mThread.running = true;
        mThread.start();
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            Paint paint = new Paint();
            paint.setARGB(255, 0, 0, 0);
            paint.setTextSize(15 * density);
            canvas.drawText(fps, (this.getWidth() - (70 * density)), 20 * density, paint);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        final Preload preload = new Preload(this);
        preload.execute();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        mThread.running = false;

        while (retry) {
            try {
                mThread.join();
                retry = false;
            } catch (InterruptedException e) {}
        }

        ((Activity)getContext()).finish();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;

        mVideo = new Video(this);
        djuradj = new Djuradj(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastTouchY = motionEvent.getY();
        }


        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            final float y = motionEvent.getY();
            int newY = djuradj.yOffset;
            newY += (int) -(y - lastTouchY);

            djuradj.setyOffset(newY);
            lastTouchY = y;
        }

        return true;
    }
}
