package com.astreanlegends.engine.world.water;

public class WaterTile {

	public static final float TILE_SIZE = 60;
	
	private float x, z;
	private float height;
	
	public WaterTile(float x, float z, float height) {
		this.x = x;
		this.z = z;
		this.height = height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public float getHeight() {
		return height;
	}
}