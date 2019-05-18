package com.astreanlegends.engine.graphics.shader;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.astreanlegends.engine.graphics.font.FontProperties;

public class FontShader extends Shader {

	private static final String SHADER_FILE = "res/shaders/fontShader";
	
	private int location_translation;
	private int location_color;
	private int location_width;
	private int location_edge;
	private int location_borderWidth;
	private int location_borderEdge;
	private int location_offset;
	private int location_outlineColor;
	
	public FontShader() {
		super(SHADER_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoordinates");
	}

	@Override
	protected void getAllUniformLocations() {
		location_translation = getUniformLocation("translation");
		location_color = getUniformLocation("color");
		location_width = getUniformLocation("width");
		location_edge = getUniformLocation("edge");
		location_borderWidth = getUniformLocation("borderWidth");
		location_borderEdge = getUniformLocation("borderEdge");
		location_offset = getUniformLocation("offset");
		location_outlineColor = getUniformLocation("outlineColor");
	}
	
	public void loadTranslation(Vector2f translation) {
		loadVector(location_translation, translation);
	}
	
	public void loadFontProperties(Vector3f color, FontProperties properties) {
		loadVector(location_color, color);
		loadFloat(location_width, properties.getWidth());
		loadFloat(location_edge, properties.getEdge());
		loadFloat(location_borderWidth, properties.getBorderWidth());
		loadFloat(location_borderEdge, properties.getBorderEdge());
		loadVector(location_offset, properties.getOffset());
		loadVector(location_outlineColor, properties.getOutlineColor());
	}
}