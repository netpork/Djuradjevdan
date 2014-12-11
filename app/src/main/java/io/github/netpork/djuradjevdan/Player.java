package io.github.netpork.djuradjevdan;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

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

    public Player(MainPanel panel) {
        this.panel = panel;

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setLooping(true);
    }

    public void play() {
        try {
            player.setDataSource(tracks.get(currentTrackIndex).streamUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        player.prepareAsync();
    }

    public void switchTrack() {
        panel.network.showMessage("Switching track...");
        playing = false;
        player.stop();
        player.reset();
        panel.scroller.prepareScrollText();
        panel.scroller.clear();
        play();
    }

    public void stopMusic() {
        player.stop();
        player.release();
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

        panel.player.switchTrack();
        Log.i(TAG, "idx: " + currentTrackIndex);
    }

    public void increaseTrackIndex() {
        if (currentTrackIndex < tracks.size() - 1) {
            currentTrackIndex++;
        } else {
            currentTrackIndex = 0;
        }

        panel.player.switchTrack();
        Log.i(TAG, "idx: " + currentTrackIndex);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
        playing = true;
        panel.network.showMessage("Play!");
    }
}
