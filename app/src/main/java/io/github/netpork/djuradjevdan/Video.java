package io.github.netpork.djuradjevdan;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

/**
 * Created by netpork on 12/8/14.
 */
public class Video {

    public static final String TAG = "Video";
    public static final int width = 180;
    public static final int height = 210;
    public static Canvas mCanvas;
    public static Bitmap mBitmap;
    private MainPanel panel;
    private static float aspect;
    private static int yOffset;

    public Video(MainPanel panel) {
        this.panel = panel;

        aspect = (float) MainPanel.screenWidth / width;
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        Log.i(TAG, "s:" + MainPanel.screenWidth + " " + "w:" + MainPanel.screenHeight);
        yOffset = (int) (((MainPanel.screenHeight - (height * aspect) + 0.5) / 2) / aspect);

    }

    public static void update(Canvas canvas) {
        canvas.save();
        canvas.scale(aspect, aspect);
        canvas.drawBitmap(mBitmap, 0, yOffset, null);
        canvas.restore();
    }

}
