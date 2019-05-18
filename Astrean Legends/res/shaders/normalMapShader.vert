#version 400 core

const int MAXIMUM_LIGHTS = 4;

in vec3 position;
in vec2 textureCoordinates;
in vec3 normals;
in vec3 tangents;

out vec2 pass_textureCoordinates;
out vec3 toLightVector[MAXIMUM_LIGHTS];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPositionEyeSpace[MAXIMUM_LIGHTS];
uniform vec4 plane;
uniform float numberOfRows;
uniform vec2 offset;

const float density = 0;
const float gradient = 5.0;

void main() {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	gl_ClipDistance[0] = dot(worldPosition, plane);
	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 positionRelativeToCamera = modelViewMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * positionRelativeToCamera;
	
	pass_textureCoordinates = (textureCoordinates / numberOfRows) + offset;
	
	vec3 surfaceNormals = (modelViewMatrix * vec4(normals, 0.0)).xyz;
	
	vec3 normal = normalize(surfaceNormals);
	vec3 tangent = normalize((modelViewMatrix * vec4(tangents, 0.0)).xyz);
	vec3 bitangent = normalize(cross(normal, tangent));
	
	mat3 toTangentSpace = mat3(
		tangent.x, bitangent.x, normal.x,
		tangent.y, bitangent.y, normal.y,
		tangent.z, bitangent.z, normal.z
	);
	
	for(int i = 0; i < MAXIMUM_LIGHTS; i++)
		toLightVector[i] = toTangentSpace * (lightPositionEyeSpace[i] - positionRelativeToCamera.xyz);
	toCameraVector = toTangentSpace * (-positionRelativeToCamera.xyz);
	
	float distance = length(positionRelativeToCamera.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}