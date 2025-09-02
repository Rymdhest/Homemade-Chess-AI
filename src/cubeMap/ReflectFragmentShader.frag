#version 400 core

in vec3 fragColour;
in vec3 FragPos;
in vec3 fragNormal;
in vec4 shadowCoords;
in vec3 reflectedVector;
in vec3 pass_viewVector;
in vec3 worldNormal;

uniform vec3 lightPosition;
uniform vec3 lightColour;
uniform vec3 viewPos;
uniform float ambient;
uniform float globalSpecFactor;
uniform float specularDampening;
uniform mat4 viewMatrix;
uniform vec3 skyColour;
uniform float modelReflectivity;

uniform vec3 modelColour;
uniform samplerCube skyBox;
layout (location = 0) out vec4 out_colour;

void main(void) {
	// Diffuse
	//vec3 lightDir = normalize((vec4(lightPosition, 1.0f)*viewMatrix).xyz - FragPos.xyz);
	vec3 lightDir = normalize(lightPosition - FragPos.xyz);
	vec3 diffuse = max(dot(worldNormal, lightDir), 0.0) * lightColour*fragColour*modelColour;

	// Specular
	//vec3 halfwayDir = normalize(lightDir + viewDir);
	//vec3 spec = clamp(pow(max(dot(Normal, halfwayDir), 0.0), specularDampening), 0.0, 1.0)*lightColour;;
	//vec3 specular =  spec * Specular*shadowFactor;

	// Attenuation
	//float attenuation = 1.0 / (1.0 + attenuation.x + attenuation.y * distance+ attenuation.z * distance * distance);

	vec3 reflectVector = reflect(pass_viewVector, worldNormal);
	vec3 reflectColour = texture(skyBox, reflectVector).rgb;

	vec3 amb = vec3(ambient)*fragColour*modelColour;

	vec3 totalColour = (diffuse+amb);
	//lighting *= AmbientOcclusion;

	out_colour.rgb = mix(totalColour, reflectColour, modelReflectivity);
	out_colour.a = 1.0;

}
