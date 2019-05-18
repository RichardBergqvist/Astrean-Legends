package com.astreanlegends.engine.graphics.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.astreanlegends.engine.graphics.ResourceLoader;
import com.astreanlegends.engine.graphics.model.Model;
import com.astreanlegends.engine.graphics.shader.GuiShader;
import com.astreanlegends.engine.graphics.texture.GuiTexture;
import com.astreanlegends.engine.utility.Maths;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

public class GuiRenderer {

	private GuiShader shader;
	private final Model quad;
	
	public GuiRenderer(GuiShader shader, ResourceLoader loader) {
		this.shader = shader;
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		shader = new GuiShader();
		quad = loader.loadToVAO(positions, 2);
	}
	
	public void render(List<GuiTexture> guis) {
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		for(GuiTexture gui : guis) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, gui.getTextureID());
			loadModelMatrix(gui);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
	}
	
	private void loadModelMatrix(GuiTexture gui) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}