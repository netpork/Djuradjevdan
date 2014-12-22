package io.github.netpork.djuradjevdan;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by netpork on 12/8/14.
 */
public class Djuradj {

    private MainPanel panel;
    public static final int width = 180;
    public static final int height = 360;
    public static final int tukiWidth = 9, tukiHeight = 9, tukiX = 87, tukiY = 325;
    public static final Paint clearPaint = new Paint();

    public int yOffset = 0;
    public int yOffset2 = 0;
    public int yDjura = 0, yScroller = 0;

    private final Rect src = new Rect();
    private final Rect dst = new Rect();

    public Djuradj(MainPanel panel) {
        this.panel = panel;
        clearPaint.setAntiAlias(false);
        clearPaint.setDither(false);
        clearPaint.setColor(Color.BLACK);
    }

    public void update() {
        setyOffset(yDjura, yScroller);
    }

    public void render(Canvas canvas) {
        src.set(0, yOffset, Video.width, Video.height + yOffset);
        dst.set(0, 0, Video.width, Video.height);
        Video.mCanvas.drawBitmap(Preload.djuradj, src, dst, null);
        drawTuki(canvas);
        drawNotes();
    }

    public void drawTuki(Canvas c) {
        int newTukiHeight = (int) (tukiY + tukiHeight + (Player.FFTAmplitude * 300));
        newTukiHeight = (newTukiHeight <= height) ? newTukiHeight : height;

        src.set(0, 0, tukiWidth, tukiHeight);
        dst.set(tukiX, tukiY, tukiX + tukiWidth, newTukiHeight);
        // clear
        Preload.djCanvas.drawRect(tukiX, tukiY, tukiX + tukiWidth, height, clearPaint);
        Preload.djCanvas.drawBitmap(Preload.tuki, src, dst, null);
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

    public void drawNotes() {
        Video.mCanvas.drawBitmap(Preload.notes, 2, 2, null);
        panel.drawText.textLine(panel.player.currentTrackIndex + 1 + "-" + panel.player.getTracksSize(), 2, 19);
        if (panel.player.buffering) panel.drawText.textLine("buffering...", 20, 7);
    }
}
