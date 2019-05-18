#version 400 core

in vec2 textureCoordinates;

out vec4 out_Color;

uniform sampler2D guiTexture;

void main() {
	out_Color = texture(guiTexture, textureCoordinates);
}