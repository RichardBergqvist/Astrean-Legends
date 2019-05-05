package com.astreanlegends.engine.graphics.model;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {

	private static final int NO_INDEX = -1;
	
	private Vector3f position;
	private int textureIndex = NO_INDEX;
	private int normalIndex = NO_INDEX;
	private Vertex duplicateVertex = null;
	private int index;
	private float length;
	private List<Vector3f> tangents = new ArrayList<Vector3f>();
	private Vector3f tangent = new Vector3f(0, 0, 0);
	
	public Vertex(Vector3f position, int index) {
		this.position = position;
		this.index = index;
		this.length = position.length();
	}
	
	public boolean isSet() {
		return textureIndex != NO_INDEX && normalIndex != NO_INDEX;
	}
	
	public boolean hasSameTextureAndNormal(int textureIndexOther, int normalIndexOther) {
		return textureIndexOther == textureIndex && normalIndexOther == normalIndex;
	}
	
	public void addTangent(Vector3f tangent) {
		tangents.add(tangent);
	}
	
	public void averageTangents() {
		if(tangents.isEmpty())
			return;
		for(Vector3f tangentRef : tangents)
			Vector3f.add(tangent, tangentRef, tangent);
		tangent.normalise();
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public void setNormalIndex(int normalIndex) {
		this.normalIndex = normalIndex;
	}

	public void setDuplicateVertex(Vertex duplicateVertex) {
		this.duplicateVertex = duplicateVertex;
	}

	public Vector3f getPosition() {
		return position;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public int getNormalIndex() {
		return normalIndex;
	}

	public Vertex getDuplicateVertex() {
		return duplicateVertex;
	}

	public int getIndex() {
		return index;
	}

	public float getLength() {
		return length;
	}
	
	public Vector3f getTangent() {
		return tangent;
	}
}