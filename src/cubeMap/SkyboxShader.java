

package cubeMap;


import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Engine.Light;
import Engine.ShaderProgram;
import Models.Colour;
import shadows.ShadowMapMasterRenderer;


public class SkyboxShader extends ShaderProgram{

	private static final String VERTEX_FILE = "/cubeMap/SkyboxVertex.vert";
	private static final String FRAGMENT_FILE = "/cubeMap/SkyboxFragment.frag";
	private int location_projectionViewMatrix;
	private int location_skyboxTexture;
	public SkyboxShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionViewMatrix = super.getUniformLocation("projectionViewMatrix");
		location_skyboxTexture = super.getUniformLocation("cubeMap");
		
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	public void connectTextureUnits() {
		super.loadInt(location_skyboxTexture, 0);
	}
	public void loadProjectionViewMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionViewMatrix, matrix);
	}
}

