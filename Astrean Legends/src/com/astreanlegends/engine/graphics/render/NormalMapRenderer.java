package com.astreanlegends.engine.graphics.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.astreanlegends.engine.entity.Camera;
import com.astreanlegends.engine.entity.Entity;
import com.astreanlegends.engine.graphics.lighting.Light;
import com.astreanlegends.engine.graphics.model.Model;
import com.astreanlegends.engine.graphics.model.TexturedModel;
import com.astreanlegends.engine.graphics.shader.NormalMapShader;
import com.astreanlegends.engine.graphics.texture.ModelTexture;
import com.astreanlegends.engine.utility.Maths;

public class NormalMapRenderer {

	private NormalMapShader shader;
	
	public NormalMapRenderer(NormalMapShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadTextureUnits();
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<Entity>> entities, Vector4f clipPlane, List<Light> lights, Camera camera) {
		for(TexturedModel model : entities.keySet()) {
			initTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for(Entity entity : batch) {
				loadModelMatrix(entity);
				glDrawElements(GL_TRIANGLES, model.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}
	
	private void initTexturedModel(TexturedModel texturedModel) {
		Model model = texturedModel.getModel();
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		ModelTexture texture = texturedModel.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if(texture.isTransparent())
			MasterRenderer.disableCulling();
		shader.loadReflectiveVariables(texture.getShineDamper(), texture.getReflectivity());
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getTextureID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, texturedModel.getTexture().getNormalMapID());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotationX(), entity.getRotationY(), entity.getRotationZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureOffsetX(), entity.getTextureOffsetY());
	}
}