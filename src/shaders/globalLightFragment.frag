#version 330
layout(location = 0) out vec4 FragColor;
in vec2 TexCoords;

uniform sampler2D gPosition;
uniform sampler2D gNormal;
uniform sampler2D gDiffuse;
uniform sampler2D ssao;

uniform vec3 lightPosition;
uniform vec3 lightColour;
uniform vec3 viewPos;
uniform float ambient;
uniform float globalSpecFactor;
uniform float specularDampening;

uniform vec3 skyColour;

void main()
{             
    // Retrieve data from gbuffer
    vec3 FragPos = texture(gPosition, TexCoords).rgb;
    vec3 Normal = texture(gNormal, TexCoords).rgb;
    vec3 Diffuse = texture(gDiffuse, TexCoords).rgb;
    float Specular = texture(gDiffuse, TexCoords).a*globalSpecFactor;
	float shadowFactor = texture(gNormal, TexCoords).a;
    float AmbientOcclusion = texture(ssao, TexCoords).r;
    // Then calculate lighting as usual

    vec3 viewDir  = normalize(viewPos-FragPos.rgb);
	float distance = length(lightPosition - FragPos.xyz);
	// Diffuse
	vec3 lightDir = normalize(lightPosition - FragPos.xyz);
	vec3 diffuse = max(dot(Normal, lightDir), 0.0) * lightColour*Diffuse*shadowFactor;
	
	// Specular
	vec3 halfwayDir = normalize(lightDir + viewDir);  
	vec3 spec = clamp(pow(max(dot(Normal, halfwayDir), 0.0), specularDampening), 0.0, 1.0)*lightColour;;
	vec3 specular =  spec * Specular*shadowFactor;
	
	// Attenuation
	//float attenuation = 1.0 / (1.0 + attenuation.x + attenuation.y * distance+ attenuation.z * distance * distance);

	vec3 amb = vec3(ambient)*Diffuse*AmbientOcclusion;

	vec3 totalColour = (diffuse+specular+amb);
	//lighting *= AmbientOcclusion;

	FragColor = vec4(totalColour, 1.0);
	//FragColor = vec4(Normal, 1.0);
	//FragColor = vec4(Normal, 1.0);
	//lighting *= 1.0-(pow(-FragPos.z, 1.02)/4024.0);
	float gradient =7.5;
	float density = 0.035;
	distance = length(FragPos.xyz);
	float visibility = exp(-pow((distance*density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
	FragColor = mix(vec4(skyColour, 1.0), FragColor, visibility);
	//FragColor.rgb = Normal.xyz;
	//FragColor = vec4(1.0f, 0.0f, 1.0f, 1.0f);
    //FragColor = vec4(AmbientOcclusion, AmbientOcclusion, AmbientOcclusion, 1.0);

	//FragColor = vec4(vec3(Specular), 1.0);
	//FragColor = vec4(-FragPos.z/900.0f, -FragPos.z/900.0f,-FragPos.z/900.0f, 1.0);
	//FragColor = vec4(Normal.rgb, 1.0);

}
