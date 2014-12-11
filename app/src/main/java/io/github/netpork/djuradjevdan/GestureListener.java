package io.github.netpork.djuradjevdan;

        import android.view.GestureDetector;
        import android.view.MotionEvent;
        import android.widget.Toast;

public class GestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final String TAG = "GestureListener";

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private final MainPanel mPanel;

    public GestureListener(MainPanel mPanel) {
        this.mPanel = mPanel;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return super.onDown(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,	float velocityY) {
        if (!mPanel.player.playing) return super.onFling(e1, e2, velocityX, velocityY);

        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Toast.makeText(mPanel.context, "fling left", Toast.LENGTH_SHORT).show();
            mPanel.player.decreaseTrackIndex();
        }

        if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Toast.makeText(mPanel.context, "fling right", Toast.LENGTH_SHORT).show();
            mPanel.player.increaseTrackIndex();
        }

        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(mPanel.context, "double tap", Toast.LENGTH_SHORT).show();
        return super.onDoubleTap(e);
    }
}
