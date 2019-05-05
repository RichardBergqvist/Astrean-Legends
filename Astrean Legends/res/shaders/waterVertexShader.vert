#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoordinates;
out vec3 toCameraVector;
out vec3 toLightVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform vec3 lightPosition;

const float tiling = 4.0;

void main() {
	vec4 worldPosition = transformationMatrix * vec4(position.x, 0.0, position.y, 1.0);
	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
	textureCoordinates = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
	toLightVector = worldPosition.xyz - lightPosition;
	toCameraVector = cameraPosition - worldPosition.xyz;
}