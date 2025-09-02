#version 330 core
in vec2 textureCoords;

out vec4 out_Colour;

uniform sampler2D ssaoInput;
const int blurSize = 4; // use size of noise texture (4x4)

void main() 
{
   vec2 texelSize = 1.0 / vec2(textureSize(ssaoInput, 0));
   float result = 0.0;
   vec2 hlim = vec2(float(-blurSize) * 0.5 + 0.5);
   for (int x = 0; x < blurSize; ++x) 
   {
      for (int y = 0; y < blurSize; ++y) 
      {
         vec2 offset = (vec2(-2.0) + vec2(float(x), float(y))) * texelSize;
         result += texture(ssaoInput, textureCoords + offset).r;
      }
   }
 
	float final = result / float(blurSize * blurSize);
	out_Colour = vec4(final, final, final, 1.0f);
}
