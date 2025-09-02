package shadows;

import org.lwjgl.util.vector.Matrix4f;

import Engine.ShaderProgram;

public class ShadowShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "/shadows/shadowVertexShader.vert";
	private static final String FRAGMENT_FILE = "/shadows/shadowFragmentShader.frag";
	
	
	private int location_hasAlpha;
	private int location_mvpMatrix;
	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		location_hasAlpha = super.getUniformLocation("hasAlpha");
		
	}
	
	protected void loadHasAlpha(boolean alpha) {
		super.loadBoolean(location_hasAlpha, alpha);
	}
	
	protected void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
	}
	
}
