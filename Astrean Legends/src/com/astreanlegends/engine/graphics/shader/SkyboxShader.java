package com.astreanlegends.engine.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.astreanlegends.engine.entity.Camera;
import com.astreanlegends.engine.graphics.DisplayManager;
import com.astreanlegends.engine.utility.Maths;

public class SkyboxShader extends Shader {

	private static final String VERTEX_SHADER_FILE = "res/shaders/skyboxVertexShader.vert";
	private static final String FRAGMENT_SHADER_FILE = "res/shaders/skyboxFragmentShader.frag";
	
	private static final float ROTATION_SPEED = 1F;
	
	private float rotation = 0;
	
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_blendFactor;
	private int location_cubeMap;
	private int location_cubeMap2;
	private int location_fogColor;
	
	public SkyboxShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_blendFactor = getUniformLocation("blendFactor");
		location_cubeMap = getUniformLocation("cubeMap");
		location_cubeMap2 = getUniformLocation("cubeMap2");
		location_fogColor = getUniformLocation("fogColor");
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		rotation += ROTATION_SPEED * DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float)Math.toRadians(rotation), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadBlendFactor(float blend) {
		loadFloat(location_blendFactor, blend);
	}
	
	public void loadTextureUnits() {
		loadInt(location_cubeMap, 0);
		loadInt(location_cubeMap2, 1);
	}
	
	public void loadFogColor(float r, float g, float b) {
		loadVector(location_fogColor, new Vector3f(r, g, b));
	}
}