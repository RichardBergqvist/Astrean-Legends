package com.astreanlegends.engine.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends Shader {

	private static final String SHADER_FILE = "res/shaders/guiShader";
		
	private int location_transformationMatrix;
	
	public GuiShader() {
		super(SHADER_FILE);
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