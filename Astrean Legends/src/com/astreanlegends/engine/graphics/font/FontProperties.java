package com.astreanlegends.engine.graphics.font;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class FontProperties {
		
		private float width;
		private float edge;
		private float borderWidth;
		private float borderEdge;
		private Vector2f offset;
		private Vector3f outlineColor;
		
		public FontProperties() {
			this.width = 0.5F;
			this.edge = 0.1F;
			this.borderWidth = 0;
			this.borderEdge = 0.4F;
			this.offset = new Vector2f(0, 0);
			this.outlineColor = new Vector3f(0, 0, 0);
		}
		
		public FontProperties(float width, float edge, float borderWidth, float borderEdge, Vector2f offset, Vector3f outlineColor) {
			this.width = width;
			this.edge = edge;
			this.borderWidth = borderWidth;
			this.borderEdge = borderEdge;
			this.offset = offset;
			this.outlineColor = outlineColor;
		}
		
		public void setWidth(float width) {
			this.width = width;
		}

		public void setEdge(float edge) {
			this.edge = edge;
		}

		public void setBorderWidth(float borderWidth) {
			this.borderWidth = borderWidth;
		}

		public void setBorderEdge(float borderEdge) {
			this.borderEdge = borderEdge;
		}

		public void setOffset(Vector2f offset) {
			this.offset = offset;
		}

		public void setOutlineColor(Vector3f outlineColor) {
			this.outlineColor = outlineColor;
		}

		public float getWidth() {
			return width;
		}

		public float getEdge() {
			return edge;
		}

		public float getBorderWidth() {
			return borderWidth;
		}

		public float getBorderEdge() {
			return borderEdge;
		}

		public Vector2f getOffset() {
			return offset;
		}

		public Vector3f getOutlineColor() {
			return outlineColor;
		}
	}