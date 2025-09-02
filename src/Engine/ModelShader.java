package Engine;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Engine.ShaderProgram;
import Models.Colour;
import shadows.ShadowMapMasterRenderer;


public class ModelShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/shaders/ModelVertexShader.vert";
	private static final String FRAGMENT_FILE = "/shaders/ModelFragmentShader.frag";
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	private int location_diffuseTexture;
	private int location_normalTexture;
	private int location_specularTexture;
	private int location_sunColour;
	private int location_sunPosition;
	private int location_isHighlighted;
	private int location_colour;
	private int location_ambientLightGlobal;
	
	private int location_shadowmapSize;
	private int location_shadowDistance;
	private int location_toShadowMapSpace;
	private int location_shadowMap;
	private int location_clipPlane;
	private int location_pcfCount;
	private int location_shadowBias;
	private int location_cameraPosition;
	private int location_enviroMap;
	private int location_modelReflectivity;
	public ModelShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_diffuseTexture = super.getUniformLocation("textureSampler");
		location_normalTexture = super.getUniformLocation("normalMap");
		location_specularTexture = super.getUniformLocation("specularMap");
		location_ambientLightGlobal = super.getUniformLocation("ambientLightGlobal");
		location_shadowmapSize = super.getUniformLocation("shadowmapSize");
		location_colour = super.getUniformLocation("modelColour");
		
		location_sunColour = super.getUniformLocation("sunColour");
		location_sunPosition = super.getUniformLocation("sunPositionEyeSpace");
		
		location_shadowMap = super.getUniformLocation("shadowMap");
		location_toShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
		location_clipPlane = super.getUniformLocation("clipPlane");
		location_isHighlighted = super.getUniformLocation("isHighlighted");
		location_shadowDistance= super.getUniformLocation("shadowDistance");
		
		location_pcfCount = super.getUniformLocation("pcfCount");
		location_shadowBias = super.getUniformLocation("shadowBias");
		location_cameraPosition = super.getUniformLocation("cameraPosition");
		location_enviroMap = super.getUniformLocation("enviroMap");
		location_modelReflectivity = super.getUniformLocation("modelReflectivity");
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "normal");
		super.bindAttribute(2, "colour");
		//super.bindAttribute(2, "textureCoord");
		//super.bindAttribute(3, "tangent");
	}
	public void connectTextureUnits() {
		super.loadInt(location_diffuseTexture, 0);
		super.loadInt(location_normalTexture, 1);
		super.loadInt(location_specularTexture, 2);
		super.loadInt(location_enviroMap, 4);
		
		super.loadInt(location_shadowMap, 5);
	}
	public void loadShadowmapSize(float size) {
		super.loadFloat(location_shadowmapSize, size);
		super.loadFloat(location_shadowDistance, MasterRenderer.getShadowDistance());
		super.loadFloat(location_shadowBias, ShadowMapMasterRenderer.SHADOW_BIAS);
		super.loadInt(location_pcfCount, ShadowMapMasterRenderer.pcfCount);
	}
	public void loadToShadowSpaceMatrix(Matrix4f matrix)  {
		super.loadMatrix(location_toShadowMapSpace, matrix);
	}
	public void loadAmbient(float ambient) {
		super.loadFloat(location_ambientLightGlobal, ambient);
	}
	public void loadIsHighlighted(boolean isHighlighted) {
		super.loadBoolean(location_isHighlighted, isHighlighted);
	}
	public void loadColour(Colour colour) {
		super.loadVector(location_colour, colour.toVector3f());
	}
	public void loadClipPlane(Vector4f clipPlane) {
		super.loadVector(location_clipPlane, clipPlane);
	}
	public void loadModelReflectivity(float modelReflectivity) {
		super.loadFloat(location_modelReflectivity, modelReflectivity);
	}
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	public void loadViewMatrix(Matrix4f viewMatrix) {
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	public void loadCameraPosition(Vector3f cameraPos) {
		super.loadVector(location_cameraPosition, cameraPos);
	}
	public void loadLight(Light light) {
		Vector4f viewPos = new Vector4f(light.getPosition().x,light.getPosition().y,light.getPosition().z , 1.0f);
		//viewPos = Matrix4f.transform(MasterRenderer.getViewMatrix(), viewPos, null);
		super.loadVector(location_sunPosition, new Vector3f(viewPos.x, viewPos.y, viewPos.z));
		super.loadVector(location_sunColour, light.getColour().toVector3f());
	}
}