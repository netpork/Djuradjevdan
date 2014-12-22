package io.github.netpork.djuradjevdan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;

/**
 * Created by netpork on 12/8/14.
 */
public class Preload extends AsyncTask <Void, Void, Void>{
    private static final String TAG = "Preload";

    public static BitmapFactory.Options optionsNoScale, optionsNoScaleMutable;

    private MainPanel panel;
    public static Bitmap djuradj, font, notes, fontSmall, tuki;

    public static Canvas djCanvas;

    public Preload(MainPanel panel) {
        this.panel = panel;

        optionsNoScale = new BitmapFactory.Options();
        optionsNoScale.inDither = false;
        optionsNoScale.inPreferredConfig = Bitmap.Config.ARGB_8888;
        optionsNoScale.inScaled = false;

        optionsNoScaleMutable = new BitmapFactory.Options();
        optionsNoScaleMutable.inDither = false;
        optionsNoScaleMutable.inPreferredConfig = Bitmap.Config.ARGB_8888;
        optionsNoScaleMutable.inScaled = false;
        optionsNoScaleMutable.inMutable = true;

    }

    @Override
    protected Void doInBackground(Void... voids) {
        djuradj = BitmapFactory.decodeResource(panel.getResources(), R.drawable.djuradj, optionsNoScaleMutable);
        fontSmall = BitmapFactory.decodeResource(panel.getResources(), R.drawable.dvapudva, optionsNoScale);
        font = BitmapFactory.decodeResource(panel.getResources(), R.drawable.font_cute_8, optionsNoScale);
        notes = BitmapFactory.decodeResource(panel.getResources(), R.drawable.notes, optionsNoScale);
        tuki = BitmapFactory.decodeResource(panel.getResources(), R.drawable.djuradj_tuki, optionsNoScale);
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        djCanvas = new Canvas(djuradj);
        panel.startThread();
    }
}
