package com.astreanlegends.engine.graphics.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public abstract class Shader {

	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public Shader(String vertexShaderFileName, String fragmentShaderFileName) {
		vertexShaderID = loadShader(vertexShaderFileName, GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentShaderFileName, GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		glLinkProgram(programID);
		glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	public void start() {
		glUseProgram(programID);
	}
	
	public void stop() {
		glUseProgram(0);
	}
	
	public void clean() {
		stop();
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		glDeleteProgram(programID);
	}
	
	protected void bindAttribute(int attributeNumber, String attributeName) {
		glBindAttribLocation(programID, attributeNumber, attributeName);
	}
	
	protected int getUniformLocation(String uniformName) {
		return glGetUniformLocation(programID, uniformName);
	}
	
	protected abstract void bindAttributes();
	protected abstract void getAllUniformLocations();
	
	protected void loadFloat(int uniformLocation, float value) {
		glUniform1f(uniformLocation, value);
	}
	
	protected void loadInt(int uniformLocation, int value) {
		glUniform1i(uniformLocation, value);
	}
	
	protected void loadVector(int uniformLocation, Vector2f vector) {
		glUniform2f(uniformLocation, vector.x, vector.y);
	}
	
	protected void loadVector(int uniformLocation, Vector3f vector) {
		glUniform3f(uniformLocation, vector.x, vector.y, vector.z);
	}
	
	protected void loadVector(int uniformLocation, Vector4f vector) {
		glUniform4f(uniformLocation, vector.x, vector.y, vector.z, vector.w);
	}
	
	protected void loadBoolean(int uniformLocation, boolean value) {
		float toLoad = 0;
		if(value)
			toLoad = 1;
		glUniform1f(uniformLocation, toLoad);
	}
	
	protected void loadMatrix(int uniformLocation, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4(uniformLocation, false, matrixBuffer);
	}
	
	private static int loadShader(String fileName, int type) {
		StringBuilder shaderSource = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = reader.readLine()) != null)
				shaderSource.append(line).append("\n");
			reader.close();
		} catch (IOException exception) {
			System.err.println("Could not read the following shader file: " + fileName);
			exception.printStackTrace();
			System.exit(-1);
		}
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			System.out.println(glGetShaderInfoLog(shaderID, 500));
			System.err.println("Could not compile shader: " + fileName);
			System.exit(-1);
		}
		return shaderID;
	}
	
}