package com.astreanlegends.engine.graphics.texture;

public class ModelTexture extends Texture {

	private int normalMapID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean isTransparent = false;
	private boolean useFakeLighting = false;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int textureID) {
		super(textureID);
	}
	
	public void setNormalMap(int normalMapID) {
		this.normalMapID = normalMapID;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	public void setHasTransparency() {
		this.isTransparent = true;
	}
	
	public void setUseFakeLighting() {
		this.useFakeLighting = true;
	}
	
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}
	
	public int getNormalMapID() {
		return normalMapID;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public boolean isTransparent() {
		return isTransparent;
	}

	public boolean isUsingFakeLighting() {
		return useFakeLighting;
	}

	public int getNumberOfRows() {
		return numberOfRows;
	}
}