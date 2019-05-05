package com.astreanlegends.engine.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends Shader {

	private static final String VERTEX_SHADER_FILE = "res/shaders/guiVertexShader.vert";
	private static final String FRAGMENT_SHADER_FILE = "res/shaders/guiFragmentShader.frag";
	
	private int location_transformationMatrix;
	
	public GuiShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = getUniformLocation("transformationMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		loadMatrix(location_transformationMatrix, transformationMatrix);
	}
}