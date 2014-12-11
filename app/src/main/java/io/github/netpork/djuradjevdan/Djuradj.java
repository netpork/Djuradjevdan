package io.github.netpork.djuradjevdan;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by netpork on 12/8/14.
 */
public class Djuradj {

    private MainPanel panel;
    public int yOffset = 0;
    public int yOffset2 = 0;
    public int yDjura = 0, yScroller = 0;

    public static final int width = 180;
    public static final int height = 360;

    private final Rect src = new Rect();
    private final Rect dst = new Rect();

    public Djuradj(MainPanel panel) {
        this.panel = panel;
    }

    public void update() {
        setyOffset(yDjura, yScroller);
    }

    public void render(Canvas canvas) {
        src.set(0, yOffset, Video.width, Video.height + yOffset);
        dst.set(0, 0, Video.width, Video.height);
        Video.mCanvas.drawBitmap(Preload.djuradj, src, dst, null);
    }

    public void setyOffset(int yOffset, int yOffset2) {
        if (yOffset > (height - Video.height)) {
            yOffset = (height - Video.height);
        }

        if (yOffset < 0) {
            yOffset = 0;
        }

        if (yOffset2 > (Video.height - panel.scroller.fontHeight)) {
            yOffset2 = (Video.height - panel.scroller.fontHeight);
        }

        if (yOffset2 < 0) {
            yOffset2 = 0;
        }

        this.yOffset = yOffset;
        this.yOffset2 = yOffset2;
    }
}
