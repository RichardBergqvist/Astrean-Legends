package com.astreanlegends.engine.graphics.font;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.astreanlegends.engine.graphics.gui.GuiText;

public class TextMeshCreator {

	protected static final double LINE_HEIGHT = 0.03F;
	protected static final int SPACE_ASCII = 32;
	
	private MetaFile metaData;
	
	protected TextMeshCreator(File metaFile) {
		metaData = new MetaFile(metaFile);
	}
	
	protected TextMeshData createTextMesh(GuiText text) {
		List<Line> lines = createStructure(text);
		TextMeshData data = createQuadVertices(text, lines);
		return data;
	}
	
	private List<Line> createStructure(GuiText text) {
		char[] chars = text.getText().toCharArray();
		List<Line> lines = new ArrayList<Line>();
		Line currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getLineMaxSize());
		Word currentWord = new Word(text.getFontSize());
		for(char c : chars) {
			int ascii = (int) c;
			if(ascii == SPACE_ASCII) {
				boolean added = currentLine.attemptToAddWord(currentWord);
				if(!added) {
					lines.add(currentLine);
					currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getLineMaxSize());
					currentLine.attemptToAddWord(currentWord);
				}
				currentWord = new Word(text.getFontSize());
				continue;
			}
			Character character = metaData.getCharacter(ascii);
			currentWord.addCharacter(character);
		}
		completeStructure(lines, currentLine, currentWord, text);
		return lines;
	}
	
	private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, GuiText text) {
		boolean added = currentLine.attemptToAddWord(currentWord);
		if(!added) {
			lines.add(currentLine);
			currentLine = new Line(metaData.getSpaceWidth(), text.getFontSize(), text.getLineMaxSize());
			currentLine.attemptToAddWord(currentWord);
		}
		lines.add(currentLine);
	}
	
	private TextMeshData createQuadVertices(GuiText text, List<Line> lines) {
		text.setNumberOfLines(lines.size());
		double curserX = 0F;
		double curserY = 0F;
		List<Float> vertices = new ArrayList<Float>();
		List<Float> textureCoordinates = new ArrayList<Float>();
		for(Line line : lines) {
			if(text.isCentered())
				curserX = (line.getMaxLength()-line.getCurrentLineLength())/2;
			for(Word word : line.getWords()) {
				for(Character letter : word.getCharacters()) {
					addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
					addTextureCoordinates(textureCoordinates, letter.getXTextureCoordinate(), letter.getYTextureCoordinate(), letter.getXMaxTextureCoordinate(), letter.getYMaxTextureCoordinate());
					curserX += letter.getXAdvance()*text.getFontSize();
				}
				curserX += metaData.getSpaceWidth()*text.getFontSize();
			}
			curserX = 0;
			curserY += LINE_HEIGHT*text.getFontSize();
		}
		return new TextMeshData(listToArray(vertices), listToArray(textureCoordinates));
	}
	
	private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize, List<Float> vertices) {
		double x = curserX+(character.getXOffset()*fontSize);
		double y = curserY+(character.getYOffset()*fontSize);
		double xMax = x+(character.getXSize()*fontSize);
		double yMax = y+(character.getYSize()*fontSize);
		double properX = (2*x)-1;
		double properY = (-2*y)+1;
		double properXMax = (2*xMax)-1;
		double properYMax = (-2*yMax)+1;
		addVertices(vertices, properX, properY, properXMax, properYMax);
	}
	
	private static void addVertices(List<Float> vertices, double x, double y, double xMax, double yMax) {
		vertices.add((float)x);
        vertices.add((float)y);
        vertices.add((float)x);
        vertices.add((float)yMax);
        vertices.add((float)xMax);
        vertices.add((float)yMax);
        vertices.add((float)xMax);
        vertices.add((float)yMax);
        vertices.add((float)xMax);
        vertices.add((float)y);
        vertices.add((float)x);
        vertices.add((float)y);
	}
	
	private static void addTextureCoordinates(List<Float> textureCoordinates, double x, double y, double xMax, double yMax) {
		textureCoordinates.add((float)x);
		textureCoordinates.add((float)y);
		textureCoordinates.add((float)x);
		textureCoordinates.add((float)yMax);
		textureCoordinates.add((float)xMax);
		textureCoordinates.add((float)yMax);
		textureCoordinates.add((float)xMax);
		textureCoordinates.add((float)yMax);
		textureCoordinates.add((float)xMax);
		textureCoordinates.add((float)y);
		textureCoordinates.add((float)x);
		textureCoordinates.add((float)y);
	}
	
	private static float[] listToArray(List<Float> floats) {
		float[] array = new float[floats.size()];
		for(int i = 0; i<array.length; i++)
			array[i] = floats.get(i);
		return array;
	}
}