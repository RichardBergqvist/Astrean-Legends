package com.astreanlegends.engine.graphics.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.astreanlegends.engine.entity.Camera;
import com.astreanlegends.engine.graphics.lighting.Light;
import com.astreanlegends.engine.utility.Maths;

public class WaterShader extends Shader {

	private static final String VERTEX_SHADER_FILE = "res/shaders/waterVertexShader.vert";
	private static final String FRAGMENT_SHADER_FILE = "res/shaders/waterFragmentShader.frag";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_reflectionTexture;
	private int location_refractionTexture;
	private int location_dudvMap;
	private int location_normalMap;
	private int location_depthMap;
	private int location_cameraPosition;
	private int location_lightPosition;
	private int location_lightColor;
	private int location_moveFactor;
	
	public WaterShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_reflectionTexture = getUniformLocation("reflectionTexture");
		location_refractionTexture = getUniformLocation("refractionTexture");
		location_dudvMap = getUniformLocation("dudvMap");
		location_normalMap = getUniformLocation("normalMap");
		location_depthMap = getUniformLocation("depthMap");
		location_cameraPosition = getUniformLocation("cameraPosition");
		location_lightPosition = getUniformLocation("lightPosition");
		location_lightColor = getUniformLocation("lightColor");
		location_moveFactor = getUniformLocation("moveFactor");
	}
	
	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		loadMatrix(location_transformationMatrix, transformationMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		loadMatrix(location_projectionMatrix, projectionMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		loadVector(location_cameraPosition, camera.getPosition());
	}
	
	public void loadLight(Light light) {
		loadVector(location_lightPosition, light.getPosition());
		loadVector(location_lightColor, light.getColor());
	}
	
	public void loadMoveFactor(float factor) {
		loadFloat(location_moveFactor, factor);
	}
	
	public void loadTextureUnits() {
		loadInt(location_reflectionTexture, 0);
		loadInt(location_refractionTexture, 1);
		loadInt(location_dudvMap, 2);
		loadInt(location_normalMap, 3);
		loadInt(location_depthMap, 4);
	}
}