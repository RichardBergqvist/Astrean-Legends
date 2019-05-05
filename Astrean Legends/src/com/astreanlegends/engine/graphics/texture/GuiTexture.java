package com.astreanlegends.engine.graphics.texture;

import org.lwjgl.util.vector.Vector2f;

public class GuiTexture extends Texture {

	private Vector2f position;
	private Vector2f scale;
	
	public GuiTexture(int textureID, Vector2f position, Vector2f scale) {
		super(textureID);
		this.position = position;
		this.scale = scale;
	}

	public Vector2f getPosition() {
		return position;
	}

	public Vector2f getScale() {
		return scale;
	}
}