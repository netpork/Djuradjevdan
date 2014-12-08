package io.github.netpork.djuradjevdan;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by netpork on 12/8/14.
 */
public class Djuradj {

    private MainPanel panel;
    public int yOffset = 0;

    public static final int width = 180;
    public static final int height = 360;

    private final Rect src = new Rect();
    private final Rect dst = new Rect();

    public Djuradj(MainPanel panel) {
        this.panel = panel;
    }

    public void update(Canvas canvas) {

    }

    public void render(Canvas canvas) {
        src.set(0, yOffset, Video.width, Video.height + yOffset);
        dst.set(0, 0, Video.width, Video.height);
        Video.mCanvas.drawBitmap(Preload.djuradj, src, dst, null);
    }

    public void setyOffset(int yOffset) {
        if (yOffset > (height - Video.height)) {
            yOffset = (height - Video.height);
        }

        if (yOffset < 0) {
            yOffset = 0;
        }

        this.yOffset = yOffset;
    }
}
