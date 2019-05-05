#version 400 core

const int MAXIMUM_LIGHTS = 4;

in vec2 pass_textureCoordinates;
in vec3 surfaceNormals;
in vec3 toLightVector[MAXIMUM_LIGHTS];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor[MAXIMUM_LIGHTS];
uniform vec3 attenuation[MAXIMUM_LIGHTS];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main() {
	vec4 blendMapColor = texture(blendMap, pass_textureCoordinates);
	
	float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoordinates = pass_textureCoordinates * 30.0;
	vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoordinates) * backTextureAmount;
	vec4 rTextureColor = texture(rTexture, tiledCoordinates) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledCoordinates) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledCoordinates) * blendMapColor.b;

	vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;

	vec3 unitNormals = normalize(surfaceNormals);
	vec3 unitCameraVector = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i = 0; i < MAXIMUM_LIGHTS; i++) {
		float distance = length(toLightVector[i]);
		float attenuationFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDot1 = dot(unitNormals, unitLightVector);
		float brightness = max(nDot1, 0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormals);
		float specularFactor = dot(reflectedLightDirection, unitCameraVector);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attenuationFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / attenuationFactor;
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	out_Color = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
}