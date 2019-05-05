package com.astreanlegends.engine.graphics.render;

import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
                                 
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.astreanlegends.engine.graphics.DisplayManager;
import com.astreanlegends.engine.graphics.ResourceLoader;
import com.astreanlegends.engine.graphics.lighting.Light;
import com.astreanlegends.engine.graphics.model.Model;
import com.astreanlegends.engine.graphics.shader.WaterShader;
import com.astreanlegends.engine.utility.Maths;
import com.astreanlegends.engine.utility.WaterFramebuffers;
import com.astreanlegends.engine.world.water.WaterTile;

public class WaterRenderer {

	private static final String DUDV_MAP = "dudv/waterDUDV";
	private static final String NORMAL_MAP = "normals/normal";
	private static final float WAVE_SPEED = 0.03F;
	
	private WaterShader shader;
	private WaterFramebuffers fbos;
	private Light sun;
	private Model quad;
	
	private float moveFactor = 0;
	
	private int dudvTextureID;
	private int normalMapID;
	
	public WaterRenderer(WaterShader shader, ResourceLoader loader, Matrix4f projectionMatrix, WaterFramebuffers fbos, Light sun) {
		this.shader = shader;
		this.fbos = fbos;
		this.sun = sun;
		dudvTextureID = loader.loadTexture(DUDV_MAP);
		normalMapID = loader.loadTexture(NORMAL_MAP);
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadTextureUnits();
		shader.stop();
		setupVAO(loader);
	}
	
	public void render(List<WaterTile> water) {
		initWater();
		for (WaterTile tile : water) {
			loadModelMatrix(tile);
			glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbindWater();
	}
	
	public void initWater() {
		moveFactor += WAVE_SPEED * DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(sun);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glActiveTexture(GL_TEXTURE0);
	    glBindTexture(GL_TEXTURE_2D, fbos.getReflectionTextureID());
	    glActiveTexture(GL_TEXTURE1);
	    glBindTexture(GL_TEXTURE_2D, fbos.getRefractionTextureID());
	    glActiveTexture(GL_TEXTURE2);
	    glBindTexture(GL_TEXTURE_2D, dudvTextureID);
	    glActiveTexture(GL_TEXTURE3);
	    glBindTexture(GL_TEXTURE_2D, normalMapID);
	    glActiveTexture(GL_TEXTURE4);
	    glBindTexture(GL_TEXTURE_2D, fbos.getRefractionDepthTextureID());
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbindWater() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		glDisable(GL_BLEND);
	}
	
	private void setupVAO(ResourceLoader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}
	
	private void loadModelMatrix(WaterTile water) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(water.getX(), water.getHeight(), water.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
		shader.loadTransformationMatrix(transformationMatrix);
	}
}