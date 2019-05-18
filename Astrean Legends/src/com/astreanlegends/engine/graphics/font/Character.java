package com.astreanlegends.engine.graphics.font;

public class Character {

	private int id;
	private double xTextureCoordinate;
	private double yTextureCoordinate;
	private double xOffset;
	private double yOffset;
	private double xSize;
	private double ySize;
	private double xAdvance;
	private double xMaxTextureCoordinate;
	private double yMaxTextureCoordinate;
	
	public Character(int id, double xTextureCoordinate, double yTextureCoordinate, double xTextureSize, double yTextureSize, double xOffset, double yOffset, double xSize, double ySize, double xAdvance) {
		super();
		this.id = id;
		this.xTextureCoordinate = xTextureCoordinate;
		this.yTextureCoordinate = yTextureCoordinate;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xSize = xSize;
		this.ySize = ySize;
		this.xAdvance = xAdvance;
		this.xMaxTextureCoordinate = xTextureSize + xTextureCoordinate;
		this.yMaxTextureCoordinate = yTextureSize + yTextureCoordinate;
	}

	public int getID() {
		return id;
	}

	public double getXTextureCoordinate() {
		return xTextureCoordinate;
	}

	public double getYTextureCoordinate() {
		return yTextureCoordinate;
	}

	public double getXOffset() {
		return xOffset;
	}

	public double getYOffset() {
		return yOffset;
	}

	public double getXSize() {
		return xSize;
	}

	public double getYSize() {
		return ySize;
	}

	public double getXAdvance() {
		return xAdvance;
	}

	public double getXMaxTextureCoordinate() {
		return xMaxTextureCoordinate;
	}

	public double getYMaxTextureCoordinate() {
		return yMaxTextureCoordinate;
	}
}