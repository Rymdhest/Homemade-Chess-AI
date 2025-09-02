package postProcessing;

import Engine.ShaderProgram;

public class SSAOBlurShader extends ShaderProgram {

	private static final String VERTEX_FILE = "/postProcessing/contrastVertex.vert";
	private static final String FRAGMENT_FILE = "/postProcessing/SSAOBlurFragment.frag";
	//private int location_ssaoInput;
	
	public SSAOBlurShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		//connectTextureUnits();
	}

	@Override
	protected void getAllUniformLocations() {	
		//location_ssaoInput = super.getUniformLocation("ssaoInput");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
	public void connectTextureUnits() {
		//super.loadInt(location_ssaoInput, 0);
	}
}
