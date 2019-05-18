package com.astreanlegends.engine.graphics.font;

import java.io.File;

import com.astreanlegends.engine.graphics.gui.GuiText;

public class Font {

	private int textureAtlas;
	private FontProperties properties;
	private TextMeshCreator creator;
	
	public Font(int textureAtlas, File fontFile) {
		this.textureAtlas = textureAtlas;
		this.properties = new FontProperties();
		this.creator = new TextMeshCreator(fontFile);
	}
	
	public Font(int textureAtlas, File fontFile, FontProperties properties) {
		this.textureAtlas = textureAtlas;
		this.properties = properties;
		this.creator = new TextMeshCreator(fontFile);
	}
	
	public TextMeshData loadText(GuiText text) {
		return creator.createTextMesh(text);
	}
	
	public int getTextureAtlas() {
		return textureAtlas;
	}
	
	public FontProperties getProperties() {
		return properties;
	}
}