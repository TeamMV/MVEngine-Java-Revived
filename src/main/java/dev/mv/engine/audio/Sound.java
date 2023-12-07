package dev.mv.engine.audio;

import dev.mv.engine.exceptions.Exceptions;
import dev.mv.engine.game.mod.loader.ModIntegration;
import dev.mv.engine.resources.Resource;
import dev.mv.engine.resources.ResourcePath;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.openal.AL11.*;

public class Sound implements Resource {
    protected Audio audio;
    protected boolean loop;
    protected State state;
    protected float volume = 0.3f;
    protected ResourcePath path;
    protected boolean loaded;
    int alID = -1, id = -1, buffer = -1;

    private float[] pos;

    Sound(Audio audio, ResourcePath path) {
        this.audio = audio;
        loop = false;
        this.path = path;
        pos = new float[] {0.0f, 0.0f, 0.0f};
    }

    @Override
    public void load() {
        if (loaded) return;
        buffer = alGenBuffers();
        try (InputStream stream = path.getInputStream()) {
            SoundFormat format = audio.getFormat(stream);
            Raw data = format.load(stream, path.getPath());
            alBufferData(buffer, data.channels > 1 ? AL_FORMAT_STEREO16 : AL_FORMAT_MONO16, data.bytes, data.sampleRate);
            loaded = true;
        } catch (IOException e) {
            alDeleteBuffers(buffer);
            Exceptions.send(e);
        }
    }

    @Override
    public void drop() {
        if (!loaded) return;
        if (getState() != State.STOPPED) stop();
        alDeleteBuffers(buffer);
        buffer = -1;
        loaded = false;
    }

    @Override
    public boolean isLoaded() {
        return loaded;
    }

    @Override
    public String getResId() {
        return path.getResId();
    }

    public int play() {
        if (state == State.PLAYING) return id;
        if (!loaded) load();
        alID = audio.nextFreeSource();
        if (alID == -1) return -1;
        alSourcei(alID, AL_BUFFER, buffer);
        alSourcei(alID, AL_LOOPING, loop ? 0 : 1);
        alSourcef(alID, AL_GAIN, volume);
        updatePosition();
        getState();
        if (state != State.PLAYING) {
            state = State.PLAYING;
            alSourcePlay(alID);
        }
        id = audio.bind(this);
        return id;
    }

    public void pause() {
        if (alID == -1) return;
        getState();
        if (state == State.PLAYING) {
            state = State.PAUSED;
            alSourcePause(alID);
        }
    }

    public void stop() {
        if (alID == -1) return;
        getState();
        if (state != State.STOPPED) {
            state = State.STOPPED;
            alSourceStop(alID);
            alSourcei(alID, AL_BUFFER, 0);
            audio.freeSource(alID);
            alID = -1;
            audio.unbind(id);
            id = -1;
        }
    }

    public State getState() {
        if (alID == -1) return State.STOPPED;
        state = State.valueOf(alGetSourcei(alID, AL_SOURCE_STATE));
        return state;
    }

    public boolean isLooping() {
        return loop;
    }

    public void setLooping(boolean loop) {
        this.loop = loop;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setPosition(float x, float y, float z) {
        pos = new float[] {x, y, z};
        if (state != State.STOPPED && alID != -1) {
            updatePosition();
        }
    }

    public void setRelativePosition(float x, float y, float z) {
        float[] p = audio.getListenerPosition();
        setPosition(p[0] + x, p[1] + y, p[2] + z);
    }

    public void setLeft() {
        setRelativePosition(-1.0f, 0.0f, 0.0f);
    }

    public void setRight() {
        setRelativePosition(1.0f, 0.0f, 0.0f);
    }

    private void updatePosition() {
        alSource3f(alID, AL_POSITION, pos[0], pos[1], pos[2]);
    }

    public Sound() {}

    public enum State {
        PLAYING(AL_PLAYING),
        STOPPED(AL_STOPPED),
        PAUSED(AL_PAUSED);

        State(int state) {
        }

        public static State valueOf(int val) {
            return switch (val) {
                case AL_PLAYING -> PLAYING;
                case AL_STOPPED -> STOPPED;
                case AL_PAUSED -> PAUSED;
                default -> null;
            };
        }
    }

    record Raw(ByteBuffer bytes, int channels, int sampleRate) {
    }
}
