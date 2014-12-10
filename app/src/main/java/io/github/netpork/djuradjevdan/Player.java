package io.github.netpork.djuradjevdan;

import android.media.AudioManager;
import android.media.MediaPlayer;

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

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        player.start();
        playing = true;
    }
}
