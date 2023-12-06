package dev.mv.engine.audio;

import dev.mv.engine.resources.ResourcePath;

import static org.lwjgl.openal.AL11.*;

public final class Music extends Sound {

    private float nextOffset, startingOffset;

    Music(Audio audio, ResourcePath path) {
        super(audio, path);
    }

    public void terminate() {
        if (getState() != State.STOPPED) stop();
    }

    public int play() {
        if (!loaded) load();
        alID = audio.nextFreeSource();
        if (alID == -1) return -1;
        alSourcei(alID, AL_BUFFER, buffer);
        alSourcei(alID, AL_LOOPING, 0);
        alSourcef(alID, AL_GAIN, volume);
        if (startingOffset > 0 && nextOffset == 0) {
            alSourcef(alID, AL_SEC_OFFSET, startingOffset);
        } else if (nextOffset > 0) {
            alSourcef(alID, AL_SEC_OFFSET, nextOffset);
            nextOffset = 0;
        }
        getState();
        if (state != State.PLAYING) {
            state = State.PLAYING;
            alSourcePlay(alID);
        }
        id = audio.bind(this);
        return id;
    }

    public void stop() {
        if (alID == -1) return;
        getState();
        if (state != Sound.State.STOPPED) {
            state = Sound.State.STOPPED;
            alSourceStop(alID);
            alSourcei(alID, AL_BUFFER, 0);
            audio.freeSource(alID);
            alID = -1;
            audio.unbind(id);
            id = -1;
        }
    }

    public void rewind() {
        setOffset(0);
    }

    public void setOffset(float seconds) {
        if (alID == -1) {
            nextOffset = seconds;
            return;
        }
        boolean wasPlaying = getState() == State.PLAYING;
        alSourceStop(alID);
        alSourcef(alID, AL_SEC_OFFSET, seconds);
        if (wasPlaying) {
            alSourcePlay(alID);
        }
    }

    public float getStartingOffset() {
        return startingOffset;
    }

    public void setStartingOffset(float offset) {
        startingOffset = offset;
    }
}
