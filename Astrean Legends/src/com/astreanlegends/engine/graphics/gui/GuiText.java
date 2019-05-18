package com.astreanlegends.engine.graphics.gui;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.astreanlegends.engine.graphics.font.Font;

public class GuiText {

	private String text;
	private Font font;
	private float fontSize;
	private Vector3f fontColor;
	
	private int textMeshVAO;
	private int vertexCount;
	private Vector2f position;
	private float lineMaxSize;
	private int numberOfLines;
	
	private boolean centerText = false;
	
	public GuiText(String text, Font font, float fontSize, Vector2f position, float maxLineLength, boolean centered) {
		this.text = text;
		this.font = font;
		this.fontSize = fontSize;
		this.fontColor = new Vector3f(0, 0, 0);
		this.position = position;
		this.lineMaxSize = maxLineLength;
		this.centerText = centered;
	}
	
	public void setMeshInfo(int vao, int vertexCount) {
		this.textMeshVAO = vao;
		this.vertexCount = vertexCount;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public void setFont(Font font) {
		this.font = font;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	
	public void setFontColor(Vector3f color) {
		this.fontColor = color;
	}
	
	public void setFontColor(float r, float g, float b) {
		this.fontColor = new Vector3f(r, g, b);
	}

	public void setTextMeshVAO(int textMeshVAO) {
		this.textMeshVAO = textMeshVAO;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}

	public void setPosition(Vector2f position) {
		this.position = position;
	}

	public void setLineMaxSize(float lineMaxSize) {
		this.lineMaxSize = lineMaxSize;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	public void setCenterText(boolean centerText) {
		this.centerText = centerText;
	}

	public String getText() {
		return text;
	}

	public Font getFont() {
		return font;
	}

	public float getFontSize() {
		return fontSize;
	}
	
	public Vector3f getFontColor() {
		return fontColor;
	}

	public int getTextMeshVAO() {
		return textMeshVAO;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public Vector2f getPosition() {
		return position;
	}

	public float getLineMaxSize() {
		return lineMaxSize;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public boolean isCentered() {
		return centerText;
	}
}