package postProcessing;

import java.util.List;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Util.WindowHandler;
import Engine.MasterRenderer;
import Engine.ShaderProgram;

public class SSAOShader extends ShaderProgram {
	private static final String VERTEX_FILE = "/postProcessing/SSAOVertex.vert";
	private static final String FRAGMENT_FILE = "/postProcessing/SSAOFragment.frag";
	
	private int location_gPositionDepth;
	private int location_gNormal;
	private int location_texNoise;
	private int location_samples[];
	private int location_projection;
	private int location_strength;
	private int location_bias;
	private int location_radius;
	private int location_winSize;

	public SSAOShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);

	}

	@Override
	protected void getAllUniformLocations() {	
		location_gPositionDepth = super.getUniformLocation("gPositionDepth");
		location_texNoise = super.getUniformLocation("texNoise");
		location_samples = new int[32];
		for (int i = 0 ; i<location_samples.length ; i++) {
			location_samples[i] = super.getUniformLocation("samples["+i+"]");
		}
		location_gNormal = super.getUniformLocation("gNormal");
		location_projection = super.getUniformLocation("projection");
		location_strength = super.getUniformLocation("strength");
		location_bias = super.getUniformLocation("bias");
		location_radius = super.getUniformLocation("radius");
		location_winSize = super.getUniformLocation("winSize");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void connectTextureUnits() {
		super.loadInt(location_gPositionDepth, 0);
		super.loadInt(location_gNormal, 1);
		super.loadInt(location_texNoise, 2);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projection, matrix);
	}
	
	public void loadSamples(Vector3f samples[]) {
		for (int i = 0 ; i<samples.length ; i++) {
			super.loadVector(location_samples[i], samples[i]);
		}
	}
	public void loadSettings(float strength, float bias, float radius) {
		super.loadFloat(location_strength, strength);
		super.loadFloat(location_bias, bias);
		super.loadFloat(location_radius, radius);
		super.loadVector(location_winSize, new Vector2f(WindowHandler.getWidth(), WindowHandler.getHeight()));
	}
	

	
}
