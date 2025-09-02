package cubeMap;

import org.lwjgl.opengl.GL11;

import Engine.MasterRenderer;
import Util.Camera;


public class SkyboxRenderer {
	
	private SkyboxShader shader;
	
	public SkyboxRenderer(){
		this.shader = new SkyboxShader();
	}
	
	public void render(Skybox skybox, Camera camera){
		prepare(skybox, camera);
		Vao model = skybox.getCubeVao();
		model.bind(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
		model.unbind(0);
		finish();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare(Skybox skybox, Camera camera){
		shader.start();
		shader.loadProjectionViewMatrix(camera.getprojectionViewMatrix());
		skybox.getTexture().bindToUnit(0);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true); //????
		MasterRenderer.enableCulling();
	}
	
	private void finish(){
		shader.stop();
	}	

}