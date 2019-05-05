package com.astreanlegends.engine.graphics.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.util.vector.Matrix4f;

import com.astreanlegends.engine.graphics.DisplayManager;
import com.astreanlegends.engine.graphics.ResourceLoader;
import com.astreanlegends.engine.graphics.model.Model;
import com.astreanlegends.engine.graphics.shader.SkyboxShader;

public class SkyboxRenderer {

private static final float SIZE = 500F;
	
	private static final float[] VERTICES = {        
	    -SIZE,  SIZE, -SIZE,
	    -SIZE, -SIZE, -SIZE,
	    SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE, -SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,

	    -SIZE, -SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE,
	    -SIZE, -SIZE,  SIZE,

	    -SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE, -SIZE,
	     SIZE,  SIZE,  SIZE,
	     SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE,  SIZE,
	    -SIZE,  SIZE, -SIZE,

	    -SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE, -SIZE,
	     SIZE, -SIZE, -SIZE,
	    -SIZE, -SIZE,  SIZE,
	     SIZE, -SIZE,  SIZE
	};
	
	private static String[] TEXTURE_FILES = { "right", "left", "top", "bottom", "back", "front" };
	private static String[] TEXTURE_FILES_NIGHT = { "nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront" };
	
	private SkyboxShader shader;
	private Model cube;
	private int textureDay;
	private int textureNight;
	
	private float time = 0;
	
	public SkyboxRenderer(SkyboxShader shader, ResourceLoader loader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadTextureUnits();
		shader.stop();
		cube = loader.loadToVAO(VERTICES, 3);
		textureDay = loader.loadCubeMap(TEXTURE_FILES);
		textureNight = loader.loadCubeMap(TEXTURE_FILES_NIGHT);
	}
	
	public void render() {
		glBindVertexArray(cube.getVaoID());
		glEnableVertexAttribArray(0);
		bindTextures();
		glDrawArrays(GL_TRIANGLES, 0, cube.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	private void bindTextures() {
		time += DisplayManager.getFrameTimeSeconds() * 1000;
		time %= 24000;
		int texture1;
		int texture2;
		float blendFactor;
		if(time >= 0 && time < 5000) {
			texture1 = textureNight;
			texture2 = textureNight;
			blendFactor = (time - 0) / (5000 - 0);
		} else if(time >= 5000 && time < 8000) {
			texture1 = textureNight;
			texture2 = textureDay;
			blendFactor = (time - 5000) / (8000 - 5000);
		} else if(time >= 8000 && time < 21000) {
			texture1 = textureDay;
			texture2 = textureDay;
			blendFactor = (time - 8000) / (21000 - 8000);
		} else {
			texture1 = textureDay;
			texture2 = textureNight;
			blendFactor = (time - 21000) / (24000 - 21000);
		}
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}
}
