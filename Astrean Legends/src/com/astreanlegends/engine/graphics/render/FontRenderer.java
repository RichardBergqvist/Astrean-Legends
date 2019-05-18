package com.astreanlegends.engine.graphics.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.astreanlegends.engine.graphics.ResourceLoader;
import com.astreanlegends.engine.graphics.font.Font;
import com.astreanlegends.engine.graphics.font.TextMeshData;
import com.astreanlegends.engine.graphics.gui.GuiText;
import com.astreanlegends.engine.graphics.shader.FontShader;

public class FontRenderer {

	private FontShader shader;
	private ResourceLoader loader;

	private Map<Font, List<GuiText>> texts = new HashMap<Font, List<GuiText>>();
	
	public FontRenderer(FontShader shader, ResourceLoader loader) {
		this.shader = shader;
		this.loader = loader;
	}
	
	public void render(Map<Font, List<GuiText>> texts) {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		for(Font font : texts.keySet()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
			for(GuiText text : texts.get(font)) {
				addText(text);
				glBindVertexArray(text.getTextMeshVAO());
				glEnableVertexAttribArray(0);
				glEnableVertexAttribArray(1);
				shader.loadTranslation(text.getPosition());
				shader.loadFontProperties(text.getFontColor(), font.getProperties());
				glDrawArrays(GL_TRIANGLES, 0, text.getVertexCount());
				glDisableVertexAttribArray(0);
				glDisableVertexAttribArray(1);
				glBindVertexArray(0);
				removeText(text);
			}
		}
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
	}
	
	public void addText(GuiText text) {
		Font font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vao = loader.loadToVAO(data.getVertices(), data.getTextureCoordinates());
		text.setMeshInfo(vao, data.getVertexCount());
		List<GuiText> batch = texts.get(font);
		if(batch == null) {
			batch = new ArrayList<GuiText>();
			texts.put(font, batch);
		}
		batch.add(text);
	}
	
	public void removeText(GuiText text) {
		List<GuiText> batch = texts.get(text.getFont());
		batch.remove(text);
		if(batch.isEmpty())
			texts.remove(text.getFont());
	}
	
	public void clean() {
		shader.clean();
	}
}