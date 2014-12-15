package io.github.netpork.djuradjevdan;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

/**
 * Created by netpork on 12/10/14.
 */
public class DrawText {
    private static final String TAG = "DrawText";

    private MainPanel panel;

    private final String map = " abcdefghijklmnopqrstuvwxyz0123456789!?,.:-=*";
    private final int[] charWidths = {5, 3, 4, 3, 3, 3, 3, 3, 3, 1, 2, 4, 3, 5, 3, 3, 3, 4, 4, 3, 3, 3, 3, 5, 4, 3, 3, 3, 2, 3, 3, 3, 3, 3, 3, 3, 3, 1, 3, 1, 1, 1, 3, 3, 5};
    private final int charHeight = 5;
    private int pageWidth, pageHeight;

    private Rect src = new Rect();
    private Rect dst = new Rect();

    public DrawText(MainPanel panel, int pageWidth, int pageHeight) {
        this.panel = panel;
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
    }

    protected void textLine(String line, int x, int y) {
        int newX = x;
//        final int newY = y * (charHeight + 2) + panel.djuradj.yOffset2;
        final int newY = y;
        final int lineLength = line.length();
        int charIndex = 0;

        for (int i = 0; i < lineLength; i++) {
            int charPosition = map.indexOf(line.charAt(charIndex));
            if (charPosition == -1) charPosition = 0;
//            Log.i(TAG, "pos: " + charPosition);

            src.set(0, (charPosition * charHeight), charWidths[charPosition], charHeight + (charPosition * charHeight));
            dst.set(newX, newY, newX + charWidths[charPosition], newY + charHeight);

            Video.mCanvas.drawBitmap(Preload.fontSmall, src, dst, null);

            charIndex++;
            newX += charWidths[charPosition] + 1;
        }
    }
}
