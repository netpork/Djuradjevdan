package io.github.netpork.djuradjevdan;

        import android.graphics.Canvas;
        import android.util.Log;
        import android.view.SurfaceHolder;

        import java.text.DecimalFormat;

/**
 * Created by netpork on 10/3/14.
 */
public class MainThread extends Thread {
    private static final String TAG = "MainThread";
    private SurfaceHolder mHolder;
    public static Canvas canvas;
    private MainPanel mPanel;

    //desired fps
    private final static int MAX_FPS = 50;
    // maximum number of frames to be skipped
    private final static int MAX_FRAME_SKIPS = 5;
    // the frame period
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    private static long timeDiff, startTime;
    public boolean running;

    // Stuff for stats */
    private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
    // we'll be reading the stats every second
    private final static int STAT_INTERVAL = 1000; //ms
    // the average will be calculated by storing
    // the last n FPSs
    private final static int FPS_HISTORY_NR = 10;
    // last time the status was stored
    private long lastStatusStore = 0;
    // the status time counter
    private long statusIntervalTimer	= 0l;
    // number of frames skipped since the game started
    private long totalFramesSkipped			= 0l;
    // number of frames skipped in a store cycle (1 sec)
    private long framesSkippedPerStatCycle 	= 0l;

    // number of rendered frames in an interval
    private int frameCountPerStatCycle = 0;
    private long totalFrameCount = 0l;
    // the last FPS values
    private double fpsStore[];
    // the number of times the stat has been read
    private long statsCount = 0;
    // the average FPS since the game started
    private double averageFps = 0.0;

    public MainThread(SurfaceHolder holder, MainPanel panel) {
        super();
        mHolder = holder;
        mPanel = panel;
    }

    @Override
    public void run() {
//        Canvas canvas;

        long beginTime;
        int sleepTime = 0, frameSkipped;

        if (MainPanel.DEVELOPMENT) {
            initTimingElements();
        }

        startTime = System.currentTimeMillis();


        while(running) {
            canvas = null;

            try {
                canvas = mHolder.lockCanvas();
                synchronized (mHolder) {
                    beginTime = System.currentTimeMillis();
                    frameSkipped = 0;

                    if (canvas != null) {
                        mPanel.update(canvas);
                        mPanel.render(canvas);
                    }

                    timeDiff = System.currentTimeMillis() - beginTime;
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {

                        }
                    }

                    while (sleepTime < 0 && frameSkipped < MAX_FRAME_SKIPS) {
                        if (canvas != null) {
                            mPanel.update(canvas);
                        }
                        sleepTime += FRAME_PERIOD;
                        frameSkipped++;
                    }

                    if (MainPanel.DEVELOPMENT) {
                        // for statistics
                        framesSkippedPerStatCycle += frameSkipped;
                        // calling the routine to store the gathered statistics
                        storeStats();
                    }
                }

            } finally {
                if (canvas != null) {
                    mHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void storeStats() {
        if (MainPanel.DEVELOPMENT) {
            frameCountPerStatCycle++;
            totalFrameCount++;

            // check the actual time
            statusIntervalTimer += (System.currentTimeMillis() - statusIntervalTimer);

            if (statusIntervalTimer >= lastStatusStore + STAT_INTERVAL) {
                // calculate the actual frames pers status check interval
                double actualFps = (double) (frameCountPerStatCycle / (STAT_INTERVAL / 1000));

                //stores the latest fps in the array
                fpsStore[(int) statsCount % FPS_HISTORY_NR] = actualFps;

                // increase the number of times statistics was calculated
                statsCount++;

                double totalFps = 0.0;
                // sum up the stored fps values
                for (int i = 0; i < FPS_HISTORY_NR; i++) {
                    totalFps += fpsStore[i];
                }

                // obtain the average
                if (statsCount < FPS_HISTORY_NR) {
                    // in case of the first 10 triggers
                    averageFps = totalFps / statsCount;
                } else {
                    averageFps = totalFps / FPS_HISTORY_NR;
                }
                // saving the number of total frames skipped
                totalFramesSkipped += framesSkippedPerStatCycle;
                // resetting the counters after a status record (1 sec)
                framesSkippedPerStatCycle = 0;
                statusIntervalTimer = 0;
                frameCountPerStatCycle = 0;

                statusIntervalTimer = System.currentTimeMillis();
                lastStatusStore = statusIntervalTimer;
//			Log.d(TAG, "Average FPS:" + df.format(averageFps));
//            mPanel.setAvgFps("fps: " + df.format(averageFps));
                mPanel.avgFps = "fps: " + df.format(averageFps);
            }
        }
    }

    private void initTimingElements() {
        if (MainPanel.DEVELOPMENT) {
            // initialise timing elements
            fpsStore = new double[FPS_HISTORY_NR];
            for (int i = 0; i < FPS_HISTORY_NR; i++) {
                fpsStore[i] = 0.0;
            }
            Log.d(TAG + ".initTimingElements()", "Timing elements for stats initialised");
        }
    }

    public static long getTimeDiff() {
        return timeDiff;
    }

}
