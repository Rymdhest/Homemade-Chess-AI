#version 150

in vec3 pass_textureCoords;
out vec4 out_colour;

uniform samplerCube cubeMap;



void main(void){
	out_colour = texture(cubeMap, pass_textureCoords);
	//out_colour = vec4(1.0f, 0.0f, 0.0f, 0.5f);

}
