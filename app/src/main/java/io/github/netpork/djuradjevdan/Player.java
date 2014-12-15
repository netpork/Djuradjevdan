package io.github.netpork.djuradjevdan;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by netpork on 12/10/14.
 */
public class Player implements MediaPlayer.OnPreparedListener {
    private static final String TAG = "Player";
    private MainPanel panel;

    private MediaPlayer player;

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
        player.stop();
        player.reset();
        panel.scroller.prepareScrollText();
        panel.scroller.clear();
        play();

        panel.specimen.prepare(70);
    }

    public void stopMusic() {
        if (player.isPlaying()) {
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
    }
}
