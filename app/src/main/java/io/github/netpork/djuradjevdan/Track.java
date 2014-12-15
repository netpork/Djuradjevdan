package io.github.netpork.djuradjevdan;

/**
 * Created by netpork on 12/10/14.
 */
public class Track {
    public final String title;
    public final String description;
    public final String genre;
    public final Integer playbackCount;
    public final Integer favouritingsCount;
    public final String streamUrl;

    public Track(String title, String description, String genre, Integer playbackCount, Integer favouritingsCount, String streamUrl) {
        this.title = title;
        this.description = description;
        this. genre = genre;
        this.playbackCount = playbackCount;
        this.favouritingsCount = favouritingsCount;
        this.streamUrl = streamUrl + clientId;
    }
}