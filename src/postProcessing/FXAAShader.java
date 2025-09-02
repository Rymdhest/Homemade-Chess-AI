package postProcessing;

import org.lwjgl.util.vector.Vector2f;

import Engine.ShaderProgram;
import Util.WindowHandler;

public class FXAAShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/postProcessing/fxaaVertex.vert";
	private static final String FRAGMENT_FILE = "/postProcessing/fxaaFragment.frag";
	private int location_winSize;
	
	public FXAAShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {	
		location_winSize = super.getUniformLocation("win_size");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	public void loadSettings() {
		super.loadVector(location_winSize, new Vector2f(WindowHandler.getWidth(), WindowHandler.getHeight()));
	}

}
