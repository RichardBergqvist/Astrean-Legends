package com.astreanlegends.engine.graphics.font;

import java.util.ArrayList;
import java.util.List;

public class Line {

	private double spaceSize;
	private double maxLength;
	
	private List<Word> words = new ArrayList<Word>();
	private double currentLineLength = 0;
	
	protected Line(double spaceWidth, double fontSize, double maxLength) {
		this.spaceSize = spaceWidth*fontSize;
		this.maxLength = maxLength;
	}
	
	protected boolean attemptToAddWord(Word word) {
		double additionalLength = word.getWidth();
		additionalLength += !words.isEmpty() ? spaceSize : 0;
		if(currentLineLength + additionalLength <= maxLength) {
			words.add(word);
			currentLineLength += additionalLength;
			return true;
		} else
			return false;
	}

	public double getMaxLength() {
		return maxLength;
	}

	public List<Word> getWords() {
		return words;
	}

	public double getCurrentLineLength() {
		return currentLineLength;
	}
}