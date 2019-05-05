package com.astreanlegends.engine.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.astreanlegends.engine.graphics.model.ModelData;
import com.astreanlegends.engine.graphics.model.Vertex;

public class OBJFileLoader {

	private static final String RESOURCE_LOCATION = "res/models/";
	
	public static ModelData loadOBJ(String fileName) {
		FileReader fr = null;
		File objFile = new File(RESOURCE_LOCATION + fileName + ".obj");
		try {
			fr = new FileReader(objFile);
		} catch(FileNotFoundException exception) {
			System.err.println("OBJ-model file not found: " + fileName);
		}
		BufferedReader reader = new BufferedReader(fr);
		String line;
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Integer> indices = new ArrayList<Integer>();
		List<Vector2f> textureCoordinates = new ArrayList<Vector2f>();
		List<Vector3f> normals = new ArrayList<Vector3f>();
		try {
			while(true) {
				line = reader.readLine();
				if(line.startsWith("v ")) {
					String[] currentLine = line.split(" ");
					Vector3f vertex = new Vector3f((float)Float.valueOf(currentLine[1]), (float)Float.valueOf(currentLine[2]), (float)Float.valueOf(currentLine[3]));
					Vertex newVertex = new Vertex(vertex, vertices.size());
					vertices.add(newVertex);
				} else if(line.startsWith("vt ")) {
					String[] currentLine = line.split(" ");
					Vector2f textureCoordinate = new Vector2f((float)Float.valueOf(currentLine[1]), (float)Float.valueOf(currentLine[2]));
					textureCoordinates.add(textureCoordinate);
				} else if(line.startsWith("vn ")) {
					String[] currentLine = line.split(" ");
					Vector3f normal = new Vector3f((float)Float.valueOf(currentLine[1]), (float)Float.valueOf(currentLine[2]), (float)Float.valueOf(currentLine[3]));
					normals.add(normal);
				} else if(line.startsWith("f "))
					break;
			}
			while(line != null && line.startsWith("f ")) {
				String[] currentLine = line.split(" ");
				String[] vertex1 = currentLine[1].split("/");
				String[] vertex2 = currentLine[2].split("/");
				String[] vertex3 = currentLine[3].split("/");
				Vertex v0 = processVertex(vertex1, vertices, indices);
				Vertex v1 = processVertex(vertex2, vertices, indices);
				Vertex v2 = processVertex(vertex3, vertices, indices);
				calculateTangents(v0, v1, v2, textureCoordinates);
				line = reader.readLine();
			}
			reader.close();
		} catch(IOException exception) {
			System.err.print("Error reading file: " + fileName);
		}
		removeUnusedVertices(vertices);
		float[] verticesArray = new float[vertices.size() * 3];
		int[] indicesArray = convertIndicesListToArray(indices);
		float[] textureCoordinatesArray = new float[vertices.size() * 2];
		float[] normalsArray = new float[vertices.size() * 3];
		float[] tangentsArray = new float[vertices.size() * 3];
		float furthestPoint = convertDataToArrays(vertices, textureCoordinates, normals, verticesArray, textureCoordinatesArray, normalsArray, tangentsArray);
		ModelData data = new ModelData(verticesArray, indicesArray, textureCoordinatesArray, normalsArray, tangentsArray, furthestPoint);
		return data;
	}
	
	private static Vertex processVertex(String[] vertex, List<Vertex> vertices, List<Integer> indices) {
		int index = Integer.parseInt(vertex[0]) - 1;
		Vertex currentVertex = vertices.get(index);
		int textureIndex = Integer.parseInt(vertex[1]) - 1;
		int normalIndex = Integer.parseInt(vertex[2]) - 1;
		if(!currentVertex.isSet()) {
			currentVertex.setTextureIndex(textureIndex);
			currentVertex.setNormalIndex(normalIndex);
			indices.add(index);
			return currentVertex;
		} else
			return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, vertices, indices);
	}
	
	private static void calculateTangents(Vertex v0, Vertex v1, Vertex v2, List<Vector2f> textureCoordinates) {
		Vector3f deltaPosition1 = Vector3f.sub(v1.getPosition(), v0.getPosition(), null);
		Vector3f deltaPosition2 = Vector3f.sub(v2.getPosition(), v0.getPosition(), null);
		Vector2f uv0 = textureCoordinates.get(v0.getTextureIndex());
		Vector2f uv1 = textureCoordinates.get(v1.getTextureIndex());
		Vector2f uv2 = textureCoordinates.get(v2.getTextureIndex());
		Vector2f deltaUV1 = Vector2f.sub(uv1, uv0, null);
		Vector2f deltaUV2 = Vector2f.sub(uv2, uv0, null);
		float r = 1.0F / (deltaUV1.x * deltaUV2.y - deltaUV1.y * deltaUV2.x);
		deltaPosition1.scale(deltaUV2.y);
		deltaPosition2.scale(deltaUV1.y);
		Vector3f tangent = Vector3f.sub(deltaPosition1, deltaPosition2, null);
		tangent.scale(r);
		v0.addTangent(tangent);
		v1.addTangent(tangent);
		v2.addTangent(tangent);
	}
	
	private static int[] convertIndicesListToArray(List<Integer> indices) {
		int[] indicesArray = new int[indices.size()];
		for(int i = 0; i < indicesArray.length; i++)
			indicesArray[i] = indices.get(i);
		return indicesArray;
	}
	
	private static float convertDataToArrays(List<Vertex> vertices, List<Vector2f> textureCoordinates, List<Vector3f> normals, float[] verticesArray, float[] textureCoordinatesArray, float[] normalsArray, float[] tangentsArray) {
		float furthestPoint = 0;
		for(int i = 0; i < vertices.size(); i++) {
			Vertex currentVertex = vertices.get(i);
			if(currentVertex.getLength() > furthestPoint)
				furthestPoint = currentVertex.getLength();
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoordinate = textureCoordinates.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			Vector3f tangent = currentVertex.getTangent();
			verticesArray[i * 3] = position.x;
			verticesArray[i * 3 + 1] = position.y;
			verticesArray[i * 3 + 2] = position.z;
			textureCoordinatesArray[i * 2] = textureCoordinate.x;
			textureCoordinatesArray[i * 2 + 1] = 1- textureCoordinate.y;
			normalsArray[i * 3] = normalVector.x;
			normalsArray[i * 3 + 1] = normalVector.y;
			normalsArray[i * 3 + 2] = normalVector.z;
			tangentsArray[i * 3] = tangent.x;
			tangentsArray[i * 3 + 1] = tangent.y;
			tangentsArray[i * 3 + 2] = tangent.z;
		}
		return furthestPoint;
	}
	
	private static Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex, List<Vertex> vertices, List<Integer> indices) {
		if(previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
			indices.add(previousVertex.getIndex());
			return previousVertex;
		} else {
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if(anotherVertex != null)
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, vertices, indices);
			else {
				Vertex duplicateVertex = new Vertex(previousVertex.getPosition(), vertices.size());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}
		} 
	}
	
	private static void removeUnusedVertices(List<Vertex> vertices) {
		for(Vertex vertex : vertices) {
			vertex.averageTangents();
			if(!vertex.isSet()) {
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
}