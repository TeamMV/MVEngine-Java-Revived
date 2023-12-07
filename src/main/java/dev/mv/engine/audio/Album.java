package dev.mv.engine.audio;

import java.util.List;

public class Album {
    private List<String> songs;

    public Album(List<String> songs) {
        this(songs, null);
    }

    public Album(List<String> songs, String resId) {
        this.songs = songs;
        
    }

    public void addMusic(String music) {
        songs.add(music);
    }

    public void remove(String music) {
        songs.remove(music);
    }

    public List<String> getSongs() {
        return songs;
    }
}
