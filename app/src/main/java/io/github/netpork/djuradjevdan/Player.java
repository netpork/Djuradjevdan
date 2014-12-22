package io.github.netpork.djuradjevdan;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by netpork on 12/10/14.
 */
public class Player implements MediaPlayer.OnPreparedListener {
    private static final String TAG = "Player";
    private MainPanel panel;

    private MediaPlayer player;
    public static float FFTAmplitude = 0;
    private Visualizer mVisualizer;
    private Visualizer.OnDataCaptureListener captureListener;

    public List<Track> tracks = new ArrayList<Track>();

    public int currentTrackIndex = 0;
    public boolean playing = false;
    public boolean buffering = true;

    public static Handler handler;
    public static Runnable playerSwitchSongRunnable;
    public Toast toast;

    public Player(MainPanel panel) {
        this.panel = panel;

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);

        handler = new Handler();
        playerSwitchSongRunnable = new Runnable() {
            @Override
            public void run() {
                switchTrack();
            }
        };

        captureListener = new Visualizer.OnDataCaptureListener() {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int i) {

            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int i) {
                float accumulator = 0;
                for (int j = 0; j < bytes.length - 1; j++) {
                    accumulator += Math.abs(bytes[j]);
                }

                final float amp = accumulator / (128 * bytes.length);
//                if (amp > FFTAmplitude) {
                    FFTAmplitude = amp;
//                } else {
//                    FFTAmplitude *= 0.99;
//                }

//                Log.i(TAG, "amp: " + FFTAmplitude);
            }
        };
    }

    public void play() {
        player.setLooping(true);

        try {
            player.setDataSource(tracks.get(currentTrackIndex).streamUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.prepareAsync();
    }

    public void switchTrack() {
//        panel.network.showMessage("Switching track...");
        playing = false;
        buffering = true;
        mVisualizer.setEnabled(false);
        player.stop();
        player.reset();
        panel.scroller.prepareScrollText();
        panel.scroller.clear();
        play();

        panel.specimen.prepare(70);
    }

    public void stopMusic() {
        if (player.isPlaying()) {
            mVisualizer.setEnabled(false);
            mVisualizer.release();
            player.stop();
            player.release();
        }
    }

    public int getTracksSize() {
        return tracks.size();
    }

    public String getTitle() {
        return tracks.get(currentTrackIndex).title.toLowerCase();
    }

    public String getDescription() {
        return tracks.get(currentTrackIndex).description.toLowerCase();
    }

    public String getGenre() {
        return tracks.get(currentTrackIndex).genre.toLowerCase();
    }

    public String getPlaybackCount() {
        return tracks.get(currentTrackIndex).playbackCount.toString().toLowerCase();
    }

    public String getFavorites() {
        return tracks.get(currentTrackIndex).favouritingsCount.toString().toLowerCase();
    }

    public void decreaseTrackIndex() {
        if (currentTrackIndex != 0) {
            currentTrackIndex--;
        } else {
            currentTrackIndex = tracks.size() - 1;
        }

        handler.removeCallbacksAndMessages(null);
        showCurrentTrack();
        handler.postDelayed(playerSwitchSongRunnable, 2000);
//        switchTrack();
        if (MainPanel.DEVELOPMENT) {
            Log.i(TAG, "idx: " + currentTrackIndex);
        }
    }

    public void increaseTrackIndex() {
        if (currentTrackIndex < tracks.size() - 1) {
            currentTrackIndex++;
        } else {
            currentTrackIndex = 0;
        }

        handler.removeCallbacksAndMessages(null);
        showCurrentTrack();
        handler.postDelayed(playerSwitchSongRunnable, 2000);
//        switchTrack();
        if (MainPanel.DEVELOPMENT) {
            Log.i(TAG, "idx: " + currentTrackIndex);
        }
    }

    public void showCurrentTrack() {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(MainPanel.context, tracks.get(currentTrackIndex).title, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
        playing = true;
        buffering = false;
//        panel.network.showMessage("Play!");

        mVisualizer = new Visualizer(player.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[0]);
        mVisualizer.setDataCaptureListener(captureListener, Visualizer.getMaxCaptureRate() / 2, false, true);
        mVisualizer.setEnabled(true);
    }
}
