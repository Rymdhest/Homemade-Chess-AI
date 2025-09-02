#version 400 core

in vec3 fragColour;
in vec3 fragPos;
in vec3 fragNormal;
in vec4 shadowCoords;
in vec3 reflectedVector;
in vec3 pass_viewVector;
in vec3 worldNormal;

uniform vec3 modelColour;
uniform float shadowmapSize;
uniform int pcfCount;
uniform float shadowBias;
uniform sampler2D shadowMap;
uniform samplerCube enviroMap;
uniform float modelReflectivity;

layout (location = 0) out vec4 gDiffuse;
layout (location = 1) out vec4 gNormal;
layout (location = 2) out vec4 gPosition;


void main(void) {

	float totalTexels = (pcfCount*2.0+1.0) * (pcfCount*2.0+1.0);
	float texelSize = 1.0 / shadowmapSize;
	float total = 0.0;
	for (int x=-pcfCount ; x<=pcfCount ; x++) {
		for (int y=-pcfCount ; y<=pcfCount ; y++) {
			float objectNearestList = texture(shadowMap, shadowCoords.xy + vec2(x, y)*texelSize).r;
			if (shadowCoords.z > objectNearestList+shadowBias) {
				total += 1.0;
			}
		}
	}
	total /= totalTexels;
	float lightFactor = 1.0 - (total * shadowCoords.w);

	vec3 reflectVector = reflect(pass_viewVector, worldNormal);
	vec3 reflectColour = texture(enviroMap, reflectVector).rgb;
	vec3 diffuse = fragColour*modelColour;

	gDiffuse.rgb = mix(diffuse, reflectColour, modelReflectivity);
	//gDiffuse.rgb = fragNormal;
	gDiffuse.a = 1.0;

	gNormal.xyz = normalize(fragNormal);
	gNormal.a = lightFactor;

	gPosition.xyz = fragPos.xyz;
	gPosition.a = 1.0f;

}
