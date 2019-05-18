package com.astreanlegends.engine.graphics.font;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.Display;

public class MetaFile {

	private static final int PADDING_TOP = 0;
	private static final int PADDING_LEFT = 1;
	private static final int PADDING_BOTTOM = 2;
	private static final int PADDING_RIGHT = 3;
	
	private static final int DESIRED_PADDING = 8;
	
	private static final String SPLITTER = " ";
	private static final String NUMBER_SEPARATOR = ",";
	
	private double aspectRatio;
	
	private double verticalPerPixelSize;
	private double horizontalPerPixelSize;
	private double spaceWidth;
	private int[] padding;
	private int paddingWidth;
	private int paddingHeight;
	
	private Map<Integer, Character> metaData = new HashMap<Integer, Character>();
	
	private BufferedReader reader;
	private Map<String, String> values = new HashMap<String, String>();
	
	protected MetaFile(File file) {
		this.aspectRatio = (double)Display.getWidth()/(double)Display.getHeight();
		openFile(file);
		loadPaddingData();
		loadLineSizes();
		int imageWidth = getValueOfVariable("scaleW");
		loadCharacterData(imageWidth);
		close();
	}
	
	private void openFile(File file) {
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch(Exception exception) {
			exception.printStackTrace();
			System.err.println("Could not read font meta data!");
		}
	}
	
	private boolean processNextLine() {
		values.clear();
		String line = null;
		try {
			line = reader.readLine();
		} catch(IOException exception) {
			exception.printStackTrace();
		}
		if(line == null)
			return false;
		for(String part : line.split(SPLITTER)) {
			String[] valuePairs = part.split("=");
			if(valuePairs.length == 2)
				values.put(valuePairs[0], valuePairs[1]);
		}
		return true;
	}
	
	private void loadPaddingData() {
		processNextLine();
		padding = getValuesOfVariable("padding");
		paddingWidth = padding[PADDING_LEFT]+padding[PADDING_RIGHT];
		paddingHeight = padding[PADDING_TOP]+padding[PADDING_BOTTOM];
	}
	
	private void loadLineSizes() {
		processNextLine();
		int lineHeightPixels = getValueOfVariable("lineHeight")-paddingHeight;
		verticalPerPixelSize = TextMeshCreator.LINE_HEIGHT/(double)lineHeightPixels;
		horizontalPerPixelSize = verticalPerPixelSize/aspectRatio;
	}
	
	private void loadCharacterData(int imageWidth) {
		processNextLine();
		processNextLine();
		while(processNextLine()) {
			Character character = loadCharacter(imageWidth);
			if(character != null)
				metaData.put(character.getID(), character);
		}
	}
	
	private Character loadCharacter(int imageSize) {
		int id = getValueOfVariable("id");
		if(id == TextMeshCreator.SPACE_ASCII) {
			spaceWidth = (getValueOfVariable("xadvance")-paddingWidth)*horizontalPerPixelSize;
			return null;
		}
		
		double xTextureCoordinate = ((double)getValueOfVariable("x")+(padding[PADDING_LEFT] - DESIRED_PADDING))/imageSize;
		double yTextureCoordinate = ((double)getValueOfVariable("y")+(padding[PADDING_TOP]-DESIRED_PADDING))/imageSize;
		int width = getValueOfVariable("width")-(paddingWidth-(2*DESIRED_PADDING));
		int height = getValueOfVariable("height")-((paddingHeight)-(2*DESIRED_PADDING));
		double quadWidth = width*horizontalPerPixelSize;
		double quadHeight = height*verticalPerPixelSize;
		double xTextureSize = (double)width/imageSize;
		double yTextureSize = (double)height/imageSize;
		double xOffset = (getValueOfVariable("xoffset")+padding[PADDING_LEFT]-DESIRED_PADDING)*horizontalPerPixelSize;
		double yOffset = (getValueOfVariable("yoffset")+(padding[PADDING_TOP]-DESIRED_PADDING))*verticalPerPixelSize;
		double xAdvance = (getValueOfVariable("xadvance")-paddingWidth)*horizontalPerPixelSize;
		return new Character(id, xTextureCoordinate, yTextureCoordinate, xTextureSize, yTextureSize, xOffset, yOffset, quadWidth, quadHeight, xAdvance);
	}
	
	private void close() {
		try {
			reader.close();
		} catch(IOException exception) {
			exception.printStackTrace();
		}
	}
	
	private int getValueOfVariable(String variableName) {
		return Integer.parseInt(values.get(variableName));
	}
	
	private int[] getValuesOfVariable(String variableName) {
		String[] numbers = values.get(variableName).split(NUMBER_SEPARATOR);
		int[] actualValues = new int[numbers.length];
		for(int i=0; i<actualValues.length; i++)
			actualValues[i] = Integer.parseInt(numbers[i]);
		return actualValues;
	}
	
	protected double getSpaceWidth() {
		return spaceWidth;
	}
	
	protected Character getCharacter(int ascii) {
		return metaData.get(ascii);
	}
}