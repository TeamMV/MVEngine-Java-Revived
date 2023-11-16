package dev.mv.engine.audio;

import dev.mv.engine.exceptions.UnimplementedException;
import dev.mv.engine.resources.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Album implements Resource {
    private List<String> songs;
    private String resId;

    public Album() {
        this(Resource.NO_R);
    }
    
    public Album(String resId) {
        songs = new ArrayList<>();
        this.resId = resId;
        
    }

    public Album(List<String> songs) {
        this(songs, Resource.NO_R);
    }

    public Album(List<String> songs, String resId) {
        this.songs = songs;
        this.resId = resId;
        
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

    @Override
    public String resId() {
        return resId;
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void load(InputStream inputStream, String resId) throws IOException {
        throw new UnimplementedException();
    }
}
