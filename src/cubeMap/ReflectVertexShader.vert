#version 400 core

in vec3 position;
in vec3 colour;
in vec3 normal;
out vec3 fragNormal;
out vec3 fragPos;
out vec3 fragColour;
out vec3 reflectedVector;
out vec3 pass_viewVector;
out vec3 worldNormal;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;

uniform float shadowDistance;
const float transitionDistance = 3;

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 postionRelativeToCam = modelViewMatrix * vec4(position,1.0);
	postionRelativeToCam = viewMatrix * worldPosition;

	gl_Position = projectionMatrix * postionRelativeToCam;
	fragPos = postionRelativeToCam.xyz;
	fragColour = colour;
	fragNormal = mat3(transpose(inverse(modelViewMatrix))) * normal;

	vec3 viewVector =normalize( worldPosition.xyz - cameraPosition);

	worldNormal = (vec4 (normal, 1.0f)*transformationMatrix).xyz;
	reflectedVector = reflect(viewVector, normalize(worldNormal));
	pass_viewVector = viewVector;
}
