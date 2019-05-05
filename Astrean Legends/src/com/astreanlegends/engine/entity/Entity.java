package com.astreanlegends.engine.entity;

import org.lwjgl.util.vector.Vector3f;

import com.astreanlegends.engine.graphics.model.TexturedModel;


public class Entity {

	private TexturedModel model;
	private Vector3f position;
	private float rotationX;
	private float rotationY;
	private float rotationZ;
	private float scale;
	
	private int textureIndex = 0;
	
	public Entity(TexturedModel model, int textureIndex, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.scale = scale;
		this.textureIndex = textureIndex;
	}
	
	public Entity(TexturedModel model, Vector3f position, float rotationX, float rotationY, float rotationZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotationX = rotationX;
		this.rotationY = rotationY;
		this.rotationZ = rotationZ;
		this.scale = scale;
	}
	
	public void move(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void rotate(float dx, float dy, float dz) {
		this.rotationX += dx;
		this.rotationY += dy;
		this.rotationZ += dz;
	}
	
	public void setModel(TexturedModel model) {
		this.model = model;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setRotationX(float rotationX) {
		this.rotationX = rotationX;
	}

	public void setRotationY(float rotationY) {
		this.rotationY = rotationY;
	}

	public void setRotationZ(float rotationZ) {
		this.rotationZ = rotationZ;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public TexturedModel getModel() {
		return model;
	}
	
	public float getTextureOffsetX() {
		int column = textureIndex % model.getTexture().getNumberOfRows();
		return (float)column/(float)model.getTexture().getNumberOfRows();
	}

	public float getTextureOffsetY() {
		int row = textureIndex / model.getTexture().getNumberOfRows();
		return (float)row/(float)model.getTexture().getNumberOfRows();
	}

	public Vector3f getPosition() {
		return position;
	}

	public float getRotationX() {
		return rotationX;
	}

	public float getRotationY() {
		return rotationY;
	}

	public float getRotationZ() {
		return rotationZ;
	}

	public float getScale() {
		return scale;
	}
}