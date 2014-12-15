package io.github.netpork.djuradjevdan;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

import app.AppController;


/**
 * Created by netpork on 12/8/14.
 */
public class MainPanel extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private static final String TAG = "MainPanel";

    public static final boolean DEVELOPMENT = false;

    public static Context context;
    private MainThread mThread;
    private GestureDetector gestureDetector;

    public static int screenWidth, screenHeight;
    public static float density;
    public String avgFps;

    public Djuradj djuradj;
    public Video mVideo;
    public Network network;
    public DrawText drawText;
    public Player player;
    public Scroller scroller;
    public Specimen specimen;

//    public MediaPlayer player;

    public float lastTouchY = 0;
    public static final Random RND = new Random();
    public Paint fpsPaint;


    public MainPanel(Context context, DisplayMetrics metrics) {
        super(context);
        this.context = context;

        density = metrics.density;
        getHolder().addCallback(this);
        setFocusable(true);
        setOnTouchListener(this);

        fpsPaint = new Paint();
        fpsPaint.setARGB(255, 255, 255, 255);
        fpsPaint.setTextSize(15 * density);

        mThread = new MainThread(getHolder(), this);
    }

     public void update(Canvas canvas) {
        djuradj.update();
        if (player.playing) scroller.update();
        specimen.update();
    }

    public void render(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);

        djuradj.render(Video.mCanvas);

        if (player.playing) {
            specimen.draw(Video.mCanvas);
            scroller.draw(Video.mCanvas);
        }

/*
            drawText.textLine("title: " + player.getTitle(), 0, 0);
            drawText.textLine("description: " + player.getDescription(), 0, 1);
            drawText.textLine("genre: " + player.getGenre(), 0, 2);
*/

        Video.update(canvas);

        if (DEVELOPMENT) {
            displayFps(canvas, avgFps);
        }
    }

    public void startThread() {
        mThread.running = true;
        mThread.start();
    }

    private void displayFps(Canvas canvas, String fps) {
        if (canvas != null && fps != null) {
            canvas.drawText(fps, (this.getWidth() - (70 * density)), 100 * density, fpsPaint);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        final Preload preload = new Preload(this);
        preload.execute();
        network = new Network(this);
        network.getTracks();
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

        player.stopMusic();

        // stop delayed switch track
        Player.handler.removeCallbacksAndMessages(Player.playerSwitchSongRunnable);

        // remove delayed toast message
        Network.handler.removeCallbacksAndMessages(Network.toastMessageRunnable);

        // cancel and remove volley requests
        AppController.getInstance().cancelPendingRequests("AppController");

        ((Activity) getContext()).finish();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        screenWidth = w;
        screenHeight = h;

        mVideo = new Video(this);
        djuradj = new Djuradj(this);
        drawText = new DrawText(this, Video.width, Video.height);
        player = new Player(this);
        scroller = new Scroller(8, 11, this);
        specimen = new Specimen(70);
        gestureDetector = new GestureDetector(context, new GestureListener(this));
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastTouchY = motionEvent.getY();
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            final float y = motionEvent.getY();
            int newY = djuradj.yOffset;
            int newY2 = djuradj.yOffset2;

            newY += (int) -(y - lastTouchY) * 1.1;
            newY2 += (int) -(y - lastTouchY) * 1.7;

            djuradj.yDjura = newY;
            djuradj.yScroller = newY2;

            lastTouchY = y;
        }

        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }
}
