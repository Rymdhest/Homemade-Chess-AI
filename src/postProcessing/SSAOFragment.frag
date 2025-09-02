#version 330 core
out vec3 FragColor;
in vec2 TexCoords;

uniform sampler2D gPositionDepth;
uniform sampler2D gNormal;
uniform sampler2D texNoise;

uniform vec3 samples[32];

// parameters (you'd probably want to use them as uniforms to more easily tweak the effect)
int kernelSize = 32;
uniform float radius;
uniform float strength;
uniform float bias;

// tile noise texture over screen based on screen dimensions divided by noise size
const float noiseSize = 4.0;
uniform vec2 winSize;

uniform mat4 projection;


void main()
{
	vec2 noiseScale = vec2(winSize.x/noiseSize, winSize.y/noiseSize);
    // Get input for SSAO algorithm
    vec3 fragPos = texture(gPositionDepth, TexCoords).xyz;
    vec3 normal = texture(gNormal, TexCoords).rgb;
	//normal = normalize(normal);
   vec3 randomVec = texture(texNoise, TexCoords*noiseScale).xyz;
   //vec3 randomVec = vec3(1.0, 1.0, 0.0);
    //randomVec = vec3(1, 1, 0);
   
    // Create TBN change-of-basis matrix: from tangent-space to view-space
    vec3 tangent = normalize(randomVec - normal * dot(randomVec, normal));
    vec3 bitangent = cross(normal, tangent);
    mat3 TBN = mat3(tangent, bitangent, normal);
    // Iterate over the sample kernel and calculate occlusion factor
    float occlusion = 0.0;
    for(int i = 0; i < kernelSize; ++i)
    {
        // get sample position
        vec3 sample = TBN * samples[i]; // From tangent to view-space
        sample = fragPos + sample * radius; 
        
        // project sample position (to sample texture) (to get position on screen/texture)
        vec4 offset = vec4(sample, 1.0);
        offset = projection * offset; // from view to clip-space
        offset.xyz /= offset.w; // perspective divide
        offset.xyz = offset.xyz * 0.5 + 0.5; // transform to range 0.0 - 1.0
        
        // get sample depth
        float sampleDepth =texture(gPositionDepth, offset.xy).z; // Get depth value of kernel sample
        
        // range check & accumulate
        //float rangeCheck = smoothstep(0.0, 1.0, radius / abs(fragPos.z - sampleDepth ));
	  float rangeCheck = abs(fragPos.z - sampleDepth) < radius ? 1.0 : 0.0;
        occlusion += (sampleDepth >= sample.z + bias ? 1.0 : 0.0)*rangeCheck;
    }
    occlusion = 1.0 - (occlusion / kernelSize);
    //occlusion =(occlusion / kernelSize);

   // occlusion = texture(gPositionDepth, TexCoords).w;
    FragColor = vec3(pow (occlusion, strength));
    //FragColor = vec3(occlusion);
	//FragColor.x = 0.5;
}
