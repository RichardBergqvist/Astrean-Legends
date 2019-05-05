#version 400 core

const int MAXIMUM_LIGHTS = 4;

in vec3 position;
in vec2 textureCoordinates;
in vec3 normals;

out vec2 pass_textureCoordinates;
out vec3 surfaceNormals;
out vec3 toLightVector[MAXIMUM_LIGHTS];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[MAXIMUM_LIGHTS];
uniform vec4 plane;

const float density = 0;
const float gradient = 5.0;

void main() {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_ClipDistance[0] = dot(worldPosition, plane); 
	vec4 positionRelativeToCamera = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCamera;
	pass_textureCoordinates = textureCoordinates;
	
	surfaceNormals = (transformationMatrix * vec4(normals, 0.0)).xyz;
	for(int i = 0; i < MAXIMUM_LIGHTS; i++)
		toLightVector[i] = lightPosition[i] - worldPosition.xyz;
	toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;

	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}