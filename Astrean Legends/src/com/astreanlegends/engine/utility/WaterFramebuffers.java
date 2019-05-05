package com.astreanlegends.engine.utility;

import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

import java.nio.ByteBuffer;

public class WaterFramebuffers {

	protected static final int REFLECTION_WIDTH = 320;
	private static final int REFLECTION_HEIGHT = 180;
	
	protected static final int REFRACTION_WIDTH = 1280;
	private static final int REFRACTION_HEIGHT = 720;
	
	private int reflectionFramebuffer;
	private int reflectionTextureID;
	private int reflectionDepthbuffer;
	
	private int refractionFramebuffer;
	private int refractionTextureID;
	private int refractionDepthTextureID;
	
	public WaterFramebuffers() {
		initReflectionFramebuffer();
		initRefractionFramebuffer();
	}
	
	private void initReflectionFramebuffer() {
		reflectionFramebuffer = createFramebuffer();
		reflectionTextureID = createTextureAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		reflectionDepthbuffer = createDepthbufferAttachment(REFLECTION_WIDTH, REFLECTION_HEIGHT);
		unbindCurrentFramebuffer();
	}
	
	private void initRefractionFramebuffer() {
		refractionFramebuffer = createFramebuffer();
		refractionTextureID = createTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		refractionDepthTextureID = createDepthTextureAttachment(REFRACTION_WIDTH, REFRACTION_HEIGHT);
		unbindCurrentFramebuffer();
	}
	
	private void bindFramebuffer(int framebuffer, int width, int height) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
		glViewport(0, 0, width, height);
	}
	
	public void bindReflectionFramebuffer() {
		bindFramebuffer(reflectionFramebuffer, REFLECTION_WIDTH, REFLECTION_HEIGHT);
	}
	
	public void bindRefractionFramebuffer() {
		bindFramebuffer(refractionFramebuffer, REFRACTION_WIDTH, REFRACTION_HEIGHT);
	}
	
	private int createFramebuffer() {
		int framebuffer = glGenFramebuffers();
		glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
		glDrawBuffer(GL_COLOR_ATTACHMENT0);
		return framebuffer;
	}
	
	private int createTextureAttachment(int width, int height) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, texture, 0);
		return texture;
	}
	
	private int createDepthbufferAttachment(int width, int height) {
		int depthbuffer = glGenRenderbuffers();
		glBindRenderbuffer(GL_RENDERBUFFER, depthbuffer);
		glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT, width, height);
		glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthbuffer);
		return depthbuffer;
	}
	
	private int createDepthTextureAttachment(int width, int height) {
		int texture = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, texture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT32, width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, (ByteBuffer) null);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, texture, 0);
		return texture;
	}
	
	public void unbindCurrentFramebuffer() {
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	public void clean() {
		glDeleteFramebuffers(reflectionFramebuffer);
		glDeleteTextures(reflectionTextureID);
		glDeleteRenderbuffers(reflectionDepthbuffer);
		glDeleteFramebuffers(refractionFramebuffer);
		glDeleteTextures(refractionTextureID);
		glDeleteTextures(refractionDepthTextureID);
	}

	public int getReflectionTextureID() {
		return reflectionTextureID;
	}

	public int getRefractionTextureID() {
		return refractionTextureID;
	}

	public int getRefractionDepthTextureID() {
		return refractionDepthTextureID;
	}
}