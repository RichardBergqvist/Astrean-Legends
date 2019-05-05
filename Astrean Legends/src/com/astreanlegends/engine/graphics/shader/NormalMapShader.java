package com.astreanlegends.engine.graphics.shader;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.astreanlegends.engine.entity.Camera;
import com.astreanlegends.engine.graphics.lighting.Light;
import com.astreanlegends.engine.utility.Maths;

public class NormalMapShader extends Shader {

	private static final String VERTEX_SHADER_FILE = "res/shaders/normalMapVertexShader.vert";
	private static final String FRAGMENT_SHADER_FILE = "res/shaders/normalMapFragmentShader.frag";
	
	private static final int MAXIMUM_LIGHTS = 4;
	
	private int location_modelTexture;
	private int location_normalMap;
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_lightPositionEyeSpace[];
	private int location_lightColor[];
	private int location_attenuation[];
	private int location_plane;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_skyColor;
	private int location_numberOfRows;
	private int location_offset;
	
	public NormalMapShader() {
		super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoordinates");
		bindAttribute(2, "normals");
		bindAttribute(3, "tangents");
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelTexture = getUniformLocation("modelTexture");
		location_normalMap = getUniformLocation("normalMap");
		location_transformationMatrix = getUniformLocation("transformationMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_lightPositionEyeSpace = new int[MAXIMUM_LIGHTS];
		location_lightColor = new int[MAXIMUM_LIGHTS];
		location_attenuation = new int[MAXIMUM_LIGHTS];
		for(int i = 0; i < MAXIMUM_LIGHTS; i++) {
			location_lightPositionEyeSpace[i] = getUniformLocation("lightPositionEyeSpace[" + i + "]");
			location_lightColor[i] = getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = getUniformLocation("attenuation[" + i + "]");
		}
		location_plane = getUniformLocation("plane");
		location_shineDamper = getUniformLocation("shineDamper");
		location_reflectivity = getUniformLocation("reflectivity");
		location_skyColor = getUniformLocation("skyColor");
		location_numberOfRows = getUniformLocation("numberOfRows");
		location_offset = getUniformLocation("offset");
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
	}
	
	public void loadLights(List<Light> lights, Camera camera) {
		for(int i = 0; i < MAXIMUM_LIGHTS; i++)
			if(i < lights.size()) {
				loadVector(location_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), camera));
				loadVector(location_lightColor[i], lights.get(i).getColor());
				loadVector(location_attenuation[i], lights.get(i).getAttenuation());
			} else {
				loadVector(location_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
				loadVector(location_lightColor[i], new Vector3f(0, 0, 0));
				loadVector(location_attenuation[i], new Vector3f(1, 0, 0));
			}
	}
	
	public void loadClipPlane(Vector4f plane) {
		loadVector(location_plane, plane);
	}
	
	public void loadReflectiveVariables(float shineDamper, float reflectivity) {
		loadFloat(location_shineDamper, shineDamper);
		loadFloat(location_reflectivity, reflectivity);
	}
	
	public void loadSkyColor(float red, float green, float blue) {
		loadVector(location_skyColor, new Vector3f(red, green, blue));
	}
	
	public void loadNumberOfRows(int numberOfRows) {
		loadFloat(location_numberOfRows, numberOfRows);
	}
	
	public void loadOffset(float x, float y) {
		loadVector(location_offset, new Vector2f(x, y));
	}
	
	public void loadTextureUnits() {
		loadInt(location_modelTexture, 0);
		loadInt(location_normalMap, 1);
	}
	
	public Vector3f getEyeSpacePosition(Light light, Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		Vector3f position = light.getPosition();
		Vector4f eyeSpacePosition = new Vector4f(position.x, position.y, position.z, 1F);
		Matrix4f.transform(viewMatrix, eyeSpacePosition, eyeSpacePosition);
		return new Vector3f(eyeSpacePosition);
	}
}