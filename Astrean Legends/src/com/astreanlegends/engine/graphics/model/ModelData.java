package com.astreanlegends.engine.graphics.model;

public class ModelData {

	private float[] vertices;
	private int[] indices;
	private float[] textureCoordinates;
	private float[] normals;
	private float[] tangents;
	private float furthestPoint;
	
	public ModelData(float[] vertices, int[] indices, float[] textureCoordinates, float[] normals, float[] tangents, float furthestPoint) {
		this.vertices = vertices;
		this.indices = indices;
		this.textureCoordinates = textureCoordinates;
		this.normals = normals;
		this.tangents = tangents;
		this.furthestPoint = furthestPoint;
	}

	public float[] getVertices() {
		return vertices;
	}

	public int[] getIndices() {
		return indices;
	}

	public float[] getTextureCoordinates() {
		return textureCoordinates;
	}

	public float[] getNormals() {
		return normals;
	}
	
	public float[] getTangents() {
		return tangents;
	}

	public float getFurthestPoint() {
		return furthestPoint;
	}
}