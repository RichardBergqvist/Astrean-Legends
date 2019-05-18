package com.astreanlegends.engine.graphics.font;

public class TextMeshData {

	private float[] vertices;
	private float[] textureCoordinates;
	
	protected TextMeshData(float[] vertices, float[] textureCoordinates) {
		this.vertices = vertices;
		this.textureCoordinates = textureCoordinates;
	}
	
	public int getVertexCount() {
		return vertices.length/2;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getTextureCoordinates() {
		return textureCoordinates;
	}
}