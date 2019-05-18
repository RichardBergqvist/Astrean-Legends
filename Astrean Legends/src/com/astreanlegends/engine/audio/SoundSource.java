package com.astreanlegends.engine.audio;

import static org.lwjgl.openal.AL10.*;

public class SoundSource {

	private int sourceID;
	
	public SoundSource() {
		sourceID = alGenSources();
		alSourcef(sourceID, AL_ROLLOFF_FACTOR, 6);
		alSourcef(sourceID, AL_REFERENCE_DISTANCE, 6);
		alSourcef(sourceID, AL_MAX_DISTANCE, 50);
	}
	
	public void playSound(int bufferID) {
		stopSound();
		alSourcei(sourceID, AL_BUFFER, bufferID);
		continuePlayingSound();
	}
	
	public void pauseSound() {
		alSourcePause(sourceID);
	}
	
	public void continuePlayingSound() {
		alSourcePlay(sourceID);
	}
	
	public void stopSound() {
		alSourceStop(sourceID);
	}
	
	public void delete() {
		stopSound();
		alDeleteSources(sourceID);
	}
	
	public void setVolume(float volume) {
		alSourcef(sourceID, AL_GAIN, volume);
	}
	
	public void setPitch(float pitch) {
		alSourcef(sourceID, AL_PITCH, pitch);
	}
	
	public void setPosition(float x, float y, float z) {
		alSource3f(sourceID, AL_POSITION, x, y, z);
	}
	
	public void setVelocity(float x, float y, float z) {
		alSource3f(sourceID, AL_VELOCITY, x, y, z);
	}
	
	public void setLooping(boolean loop) {
		alSourcei(sourceID, AL_LOOPING, loop ? AL_TRUE : AL_FALSE);
	}
	
	public boolean isPlaying() {
		return alGetSourcei(sourceID, AL_SOURCE_STATE) == AL_PLAYING;
	}
}