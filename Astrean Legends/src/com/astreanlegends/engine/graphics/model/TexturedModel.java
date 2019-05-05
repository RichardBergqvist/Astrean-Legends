package com.astreanlegends.engine.graphics.model;

import com.astreanlegends.engine.graphics.texture.ModelTexture;

public class TexturedModel {

	private Model model;
	private ModelTexture texture;
	
	public TexturedModel(Model model, ModelTexture texture) {
		this.model = model;
		this.texture = texture;
	}
	
	public Model getModel() {
		return model;
	}

	public ModelTexture getTexture() {
		return texture;
	}
}