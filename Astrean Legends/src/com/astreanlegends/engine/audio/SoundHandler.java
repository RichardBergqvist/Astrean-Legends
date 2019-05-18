package com.astreanlegends.engine.audio;

import static org.lwjgl.openal.AL10.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;

public class SoundHandler {

	public void init() {
		try {
			AL.create();
		} catch(LWJGLException exception) {
			exception.printStackTrace();
		}
	}
	
	public void setListenerData(float x, float y, float z) {
		alListener3f(AL_POSITION, x, y, z);
		alListener3f(AL_VELOCITY, 0, 0, 0);
	}
	
	public void clean() {
		AL.destroy();
	}
}