package com.astreanlegends.engine.graphics.font;

import java.util.ArrayList;
import java.util.List;

public class Word {

	private List<Character> characters = new ArrayList<Character>();
	private double width = 0;
	private double fontSize;
	
	protected Word(double fontSize) {
		this.fontSize = fontSize;
	}
	
	protected void addCharacter(Character character) {
		characters.add(character);
		width += character.getXAdvance()*fontSize;
	}

	public List<Character> getCharacters() {
		return characters;
	}

	public double getWidth() {
		return width;
	}
}