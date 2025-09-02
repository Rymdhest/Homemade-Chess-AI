package cubeMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Engine.Engine;
import Engine.MasterRenderer;
import Engine.World;
import Engine.WorldClock;
import Gameplay.ModelEntity;
import Models.Model;
import Util.Camera;
import Util.Maths;
import shadows.ShadowMapMasterRenderer;


public class EnviroMapRenderer {
	private Map<Model, List<ModelEntity>> modelEntities = new HashMap<Model, List<ModelEntity>>();
	private Texture[][] enviroMaps;
	private ReflectShader reflectShader;
	private int n_x_maps = 14;
	private int n_y_maps = 8;
	private static boolean needUpdate = true;
	public EnviroMapRenderer() {
		enviroMaps = new Texture[n_x_maps][n_y_maps];
		for (int y = 0 ; y < n_y_maps ; y++) {
			for (int x = 0 ; x < n_x_maps ; x++) {
				enviroMaps[x][y] = Texture.newEmptyCubeMap(1024);
			}
		}
		reflectShader = new ReflectShader();
	}
	public void updateAllEnviroMaps(MasterRenderer renderer, World worl) {
		for (int y = 0 ; y < n_y_maps ; y++) {
			for (int x = 0 ; x < n_x_maps ; x++) {
				Vector3f center = new Vector3f(x-3f, 1.25f, -y);
				renderEnvironmentMap( enviroMaps[x][y],center, worl, renderer);
			}
		}
	}
	
	public Texture getCubeMapForPosition(Vector3f pos) {
		int x = (int) pos.x;
		if (x < -3) x = -3;
		else if (x>10) x= 10;
		int z = (int) -pos.z;
		if (z < 0) z = 0;
		else if (z>7) z= 7;
		return enviroMaps[x+3][z];
	}

	public static void needToUpdate() {
		needUpdate = true;
	}
	public void update(MasterRenderer renderer, World world) {
		if (needUpdate) {
			updateAllEnviroMaps(renderer, world);
			needUpdate = false;
		}
	}
	public void renderEnvironmentMap(Texture cubeMap,Vector3f center, World world, MasterRenderer renderer) {

		Camera camera = new Camera(1f, 90f);
		camera.setPosition(center);
		camera.setRoll(180);
		//create fbo
		int fbo = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, fbo);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);

		//attach depth buffer
		int depthBuffer = GL30.glGenRenderbuffers();
		
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL14.GL_DEPTH_COMPONENT24, cubeMap.size, cubeMap.size);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER,
				depthBuffer);

		//indicate that we want to render to the entire face
		GL11.glViewport(0, 0, cubeMap.size, cubeMap.size);

		//loop faces
		for (int i = 0; i < 6; i++) {

			//attach face to fbo as color attachment 0
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0,
					GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, cubeMap.textureId, 0);
			
			//point camera in the right direction
			switchCameraToFace(i, camera);
			
			//render scene to fbo, and therefore to the current face of the cubemap
			renderWorld(world, camera);
			renderer.getSkyboxRenderer().render(world.getSkyBox(), camera);
		}
		
		//stop rendering to fbo
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		//delete fbo
		GL30.glDeleteRenderbuffers(depthBuffer);
		GL30.glDeleteFramebuffers(fbo);

	}
	private void renderWorld(World world, Camera camera ) {
		ArrayList<ModelEntity> renderEntities = world.getEntities();
		for (ModelEntity entity :renderEntities) {
			if (Maths.getDistanceXZ(camera.getPosition(), entity.getPosition()) > -0.25f) {
				processEntity(entity);
			}
			
		}
		prepareWorldRender();
		renderEntities(modelEntities, camera, world.getSkyBox());
		modelEntities.clear();
		
	}
	private void renderEntities(Map<Model, List<ModelEntity>> entities , Camera camera, Skybox skybox) {
		reflectShader.start();

		reflectShader.loadProjectionMatrix(camera.getProjectionMatrix());
		reflectShader.loadViewMatrix(camera.getviewMatrix());
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, skybox.getTexture().textureId);
		reflectShader.loadLight(Engine.getGame().getWorld().getSun());
		reflectShader.loadAmbient(Engine.getGame().getWorld().getSun().getAmbientLight());
		reflectShader.loadCameraPosition(camera.getPosition());
		
		for (Model model : entities.keySet()) {
			prepareTexturedModel(model);
			List<ModelEntity> batch = entities.get(model);
			for (ModelEntity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		reflectShader.stop();
	}
	private void prepareInstance(ModelEntity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		reflectShader.loadTransformationMatrix(transformationMatrix);
		reflectShader.loadColour(entity.getColour());
		reflectShader.loadModelReflectivity(entity.getReflectivity());
	}
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}
	private void prepareTexturedModel(Model model) {
		Model rawModel = model;;
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		//GL20.glEnableVertexAttribArray(3);

	}
	private void prepareWorldRender() {
		GL11.glClearColor(MasterRenderer.skyColour.r, MasterRenderer.skyColour.g, MasterRenderer.skyColour.b, 1.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		//GL11.glEnable(GL11.GL_BLEND);
		MasterRenderer.enableCulling();
		//GL13.glActiveTexture(GL13.GL_TEXTURE5);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMaptexture());
	}
	public void processEntity(ModelEntity modelEntity) {
		Model entityModel = modelEntity.getModel();
		List<ModelEntity> batch = modelEntities.get(entityModel);
		
		if (batch!=null) {
			batch.add(modelEntity);
		} else {
			List<ModelEntity> newBatch = new ArrayList<ModelEntity>();
			newBatch.add(modelEntity);
			modelEntities.put(entityModel, newBatch);
		}
	}
	private static void switchCameraToFace(int faceIndex, Camera camera) {
		switch (faceIndex) {
		case 0:
			camera.setPitch(0);
			camera.setYaw(90);
			break;
		case 1:
			camera.setPitch(0);
			camera.setYaw(-90);
			break;
		case 2:
			camera.setPitch(-90);
			camera.setYaw(180);
			break;
		case 3:
			camera.setPitch(90);
			camera.setYaw(180);
			break;
		case 4:
			camera.setPitch(0);
			camera.setYaw(180);
			break;
		case 5:
			camera.setPitch(0);
			camera.setYaw(0);
			break;
		}
		camera.updateViewAndProjViewMatrix();
	}
	public void cleanUp() {
		for (int y = 0 ; y < 8 ; y++) {
			for (int x = 0 ; x < 8 ; x++) {
				enviroMaps[x][y].delete();
			}
		}
	}
}