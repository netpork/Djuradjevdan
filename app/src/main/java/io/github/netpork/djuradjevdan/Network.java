package io.github.netpork.djuradjevdan;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.AppController;

/**
 * Created by netpork on 12/9/14.
 */
public class Network {
    private static final String TAG = "Network";

    private MainPanel panel;
    private final String urlTracks = "https://api.soundcloud.com/users/542351/tracks.json?client_id=38ca041fa742d7b29614329ac785f41d";

    public Network(MainPanel panel) {
        this.panel = panel;
    }

    protected void getTracks() {
        showMessage("Fetching tracks from Sound Cloud");
        JsonArrayRequest req = new JsonArrayRequest(urlTracks,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.i(TAG, "tracks length: " + response.length());

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject song = (JSONObject) response.get(i);

                                Track newTrack = new Track(
                                        song.getString("title"),
                                        song.getString("description"),
                                        song.getString("genre"),
                                        song.getInt("playback_count"),
                                        song.getInt("favoritings_count"),
                                        song.getString("stream_url")
                                );

                                panel.player.tracks.add(newTrack);
//                                Log.i(TAG, "song name: " + song.getString("title"));

                            } catch (JSONException e) {
//                                e.printStackTrace();
                            }
                        }
                        Log.i(TAG, "TRACKKKS: " + panel.player.getTracksSize());
                        showMessage("Found " + panel.player.getTracksSize() + " songs...");
                        panel.player.play();
                        panel.scroller.prepareScrollText();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i(TAG, "Volley error: " + volleyError.getMessage());
                        showMessage("Error: " + volleyError.getMessage());
                    }
                }
        );

//        req.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(req);
    }

    public void showMessage(String message) {
        Toast.makeText(panel.context, message, Toast.LENGTH_SHORT).show();
    }
}
