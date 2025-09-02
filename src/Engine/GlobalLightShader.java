package Engine;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Util.Camera;
import Util.Maths;


public class GlobalLightShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/shaders/defferedVertex.vert";
	private static final String FRAGMENT_FILE = "/shaders/globalLightFragment.frag";
	private int location_viewPos;
	private int location_lightPosition;
	private int location_lightColour;
	private int location_gPosition;
	private int location_gNormal;
	private int location_gDiffuse;
	private int location_ssao;
	private int location_ambient;
	private int location_globalSpecFactor;
	private int location_ssaoBoost;
	private int location_specularDampening;
	private int location_skyColour;
	public GlobalLightShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);

	}

	@Override
	protected void getAllUniformLocations() {	
		location_viewPos = super.getUniformLocation("viewPos");
		location_gPosition = super.getUniformLocation("gPosition");
		location_gNormal = super.getUniformLocation("gNormal");
		location_gDiffuse = super.getUniformLocation("gDiffuse");
		location_ssao = super.getUniformLocation("ssao");
		location_lightPosition = super.getUniformLocation("lightPosition");
		location_lightColour= super.getUniformLocation("lightColour");
		location_ambient = super.getUniformLocation("ambient");
		location_globalSpecFactor= super.getUniformLocation("globalSpecFactor");
		location_specularDampening = super.getUniformLocation("specularDampening");
		location_ssaoBoost = super.getUniformLocation("ssaoBoost");
		location_skyColour = super.getUniformLocation("skyColour");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadViewPos(Vector3f pos, Camera camera) {
		Vector4f viewPos = new Vector4f(pos.x, pos.y, pos.z , 1.0f);
		viewPos = Matrix4f.transform(Maths.createViewMatrix(camera), viewPos, null);
		super.loadVector(location_viewPos, new Vector3f(viewPos.x, viewPos.y, viewPos.z));
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_gPosition, 0);
		super.loadInt(location_gNormal, 1);
		super.loadInt(location_gDiffuse, 2);
		super.loadInt(location_ssao, 3);
	}

	public void loadLight(Sun sunLight, Camera camera) {
		Vector4f viewPos = new Vector4f(sunLight.getPosition().x,sunLight.getPosition().y,sunLight.getPosition().z , 1.0f);
		viewPos = Matrix4f.transform(Maths.createViewMatrix(camera), viewPos, null);
		
		super.loadVector(location_lightPosition, new Vector3f(viewPos.x, viewPos.y, viewPos.z));
		//super.loadVector(location_lightPosition[i], new Vector3f(lights.get(i).getPosition().x, lights.get(i).getPosition().y, lights.get(i).getPosition().z));
		super.loadVector(location_lightColour, sunLight.getColour().toVector3f());
		
		super.loadFloat(location_globalSpecFactor, sunLight.globalSpecFactor);
		super.loadFloat(location_specularDampening, sunLight.specularDampener);;
	}
	public void loadAmbient(float ambient, Vector3f skyColour) {
		super.loadFloat(location_ambient, ambient);
		super.loadVector(location_skyColour, skyColour);
	}
}
