package io.github.netpork.djuradjevdan;

/**
 * Created by netpork on 12/11/14.
 */

import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;

public class Scroller {
    public static final String TAG = "Scroller";

    private final MainPanel mPanel;

    public final int fontWidth, fontHeight;
    private float scrollThreshold;

    private Canvas scrollCanvas;
    private Bitmap scrollBitmap;
    private int bitmapScrollIndex = 0;
    private int textScrollIndex = 0;
    final int charsPerLine;
    private int counter = 0;

    private int[] pixels;
//    private final Paint scrollerBackground;

    private Rect src = new Rect();
    private Rect dst = new Rect();

    public static final String fontMap = " abcdefghijklmnopqrstuvwxyz0123456789!,.:;?*()-";

    public String scrollText = "";

    public Scroller(int fontWidth, int fontHeight, MainPanel panel) {
        mPanel = panel;

        this.fontWidth = fontWidth;
        this.fontHeight = fontHeight;

        this.scrollThreshold = 1;

        charsPerLine = Video.width / this.fontWidth;

        // make scroll bitmap and canvas with SCREEN_WIDTH + FONT_WIDTH on both sides
        scrollBitmap = Bitmap.createBitmap(Video.width + (this.fontWidth * 2), this.fontHeight, Bitmap.Config.ARGB_8888);
        scrollCanvas = new Canvas(scrollBitmap);
        pixels = new int[(Video.width + this.fontWidth) * this.fontHeight];
    }

    private int getCharPosition(int index) {
        char c = scrollText.charAt(index);

        if (c == '#') {
            // new scroll speed, skip #speed
            textScrollIndex += 3;
            scrollThreshold = Character.getNumericValue(scrollText.charAt(index + 1));
//            Log.i(TAG, "scrollt=========" + scrollThreshold);
            c = scrollText.charAt(index + 3);
        }

        return fontMap.indexOf(c);
    }

    private void scroll() {
        // do we need to draw a new char from a text
        if (bitmapScrollIndex <= 0) {
            drawNewChar();
            bitmapScrollIndex = fontWidth;

            if (textScrollIndex < scrollText.length() - 1) {
                textScrollIndex++;
            } else {
                textScrollIndex = 0;
            }
        }

        bitmapScrollIndex -= scrollThreshold;
    }

    private void drawNewChar() {
        final int pos = getCharPosition(textScrollIndex);
        scrollBitmap.getPixels(pixels, 0, Video.width + fontWidth, fontWidth, 0, Video.width + fontWidth, fontHeight);
        scrollBitmap.eraseColor(Color.TRANSPARENT);
        scrollCanvas.drawBitmap(pixels, 0, Video.width + fontWidth, 0, 0, Video.width + fontWidth, fontHeight, true, null);
        src.set((int) (pos * fontWidth), 0, (int) (pos * fontWidth + fontWidth), fontHeight);
        dst.set(Video.width + fontWidth, 0, Video.width + (fontWidth << 1), fontHeight);
        scrollCanvas.drawBitmap(Preload.font, src, dst, null);
    }

    private void drawScroller(Canvas c) {
        final int offset = (fontWidth - 1) - bitmapScrollIndex;
        src.set(offset, 0, Video.width + offset, fontHeight);
        dst.set(0, mPanel.djuradj.yOffset2, Video.width, mPanel.djuradj.yOffset2 + fontHeight);
        c.drawBitmap(scrollBitmap, src, dst, null);
    }

    public void update() {
        scroll();
    }

    public void draw(Canvas c) {
        drawScroller(c);
    }

    public void clear() {
        scrollBitmap.eraseColor(Color.TRANSPARENT);
    }

    public void prepareScrollText() {
        setScrollText(
                "Song: " + mPanel.player.getTitle() + " *" +
                " Description: " + mPanel.player.getDescription() + " *" +
                " Genre: " + mPanel.player.getGenre() + " *" +
                " Playback count: " + mPanel.player.getPlaybackCount() + " *" +
                " Favourtings count: " + mPanel.player.getFavorites()
        );
        textScrollIndex = 0;
        bitmapScrollIndex = 0;
    }


    public void setScrollText(String text) {
        scrollText = (text + "                ").toLowerCase();
    }
}