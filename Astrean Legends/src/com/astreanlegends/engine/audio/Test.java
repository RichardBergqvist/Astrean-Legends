package com.astreanlegends.engine.audio;

import static org.lwjgl.openal.AL10.*;

import java.io.IOException;

import com.astreanlegends.engine.graphics.ResourceLoader;

public class Test {

	public static void main(String[] args) throws IOException, InterruptedException {
		ResourceLoader loader = new ResourceLoader();
		SoundHandler soundHandler = new SoundHandler();
		
		soundHandler.init();
		soundHandler.setListenerData(0, 0, 0);
		alDistanceModel(AL_INVERSE_DISTANCE_CLAMPED);
		
		int bufferID = loader.loadSound("bounce.wav");
		SoundSource source = new SoundSource();
		source.setLooping(true);
		source.playSound(bufferID);
		source.setPosition(0, 0, 0);
		float xPos = 0;
		
		char c = ' ';
		while(c != 'q') {
			xPos -= 0.02F;
			source.setPosition(xPos, 0, 0);
			System.out.println(xPos);
			Thread.sleep(10);
		}
		source.delete();
		soundHandler.clean();
	}
}
