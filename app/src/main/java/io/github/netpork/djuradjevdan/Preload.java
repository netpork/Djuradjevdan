package io.github.netpork.djuradjevdan;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Created by netpork on 12/8/14.
 */
public class Preload extends AsyncTask <Void, Void, Void>{
    private static final String TAG = "Preload";

    public static BitmapFactory.Options optionsNoScale;

    private MainPanel panel;
    public static Bitmap djuradj;

    public Preload(MainPanel panel) {
        this.panel = panel;

        optionsNoScale = new BitmapFactory.Options();
        optionsNoScale.inDither = false;
        optionsNoScale.inPreferredConfig = Bitmap.Config.ARGB_8888;
        optionsNoScale.inScaled = false;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        djuradj = BitmapFactory.decodeResource(panel.getResources(), R.drawable.djuradj, optionsNoScale);
        return null;
    }

    @Override
    protected void onPostExecute(Void voids) {
        super.onPostExecute(voids);
        panel.startThread();
    }
}
