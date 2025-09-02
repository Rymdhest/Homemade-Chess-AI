package Engine;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Gameplay.ModelEntity;
import Models.Colour;
import Models.Model;
import Util.Camera;
import Util.WindowHandler;
import cubeMap.EnviroMapRenderer;
import cubeMap.SkyboxRenderer;
import cubeMap.Texture;
import Gameplay.Entity;
import postProcessing.FXAA;
import postProcessing.SSAO;
import postProcessing.SSAOBlur;
import shadows.ShadowMapMasterRenderer;
import Util.Maths;

import java.nio.Buffer;



//import shadows.ShadowMapMasterRenderer;



public class MasterRenderer {

	private static float shadowDistance = 30;
	//public static Colour skyColour = new Colour(0.6f, 0.7f, 0.9f);
	public static Colour skyColour = new Colour(0.0f, 0.0f, 0.0f);
	public static SSAO ssao;
	public static FXAA fxaa;
	public static SSAOBlur ssaoBlur;
	public static GBuffer gBuffer;
	public static ShadowMapMasterRenderer shadowMapRenderer;
	//public static ShadowMapMasterRenderer shadowMapRenderer;
	private ModelShader modelShader;
	public ImageRenderer renderer;
	public GlobalLightShader globalLightShader;
	private EnviroMapRenderer enviroMapRenderer;
	private SkyboxRenderer skyBoxRenderer;
	private Map<Model, List<ModelEntity>> modelEntities = new HashMap<Model, List<ModelEntity>>();

	private static Camera camera;
	public MasterRenderer() {
		camera = new Camera((float) Display.getWidth() / (float) Display.getHeight(), 45f);
		
		camera.setPosition(new Vector3f(3.5f, 11, 3f));
		camera.setRotation(new Vector3f(60f, 0, 0));
	//	MasterRenderer.shadowMapRenderer = new ShadowMapMasterRenderer(Engine.getCamera());
		globalLightShader = new GlobalLightShader();
		globalLightShader.start();
		globalLightShader.connectTextureUnits();
		globalLightShader.stop();
		skyBoxRenderer = new SkyboxRenderer();
		enviroMapRenderer = new EnviroMapRenderer();
		shadowMapRenderer = new ShadowMapMasterRenderer(camera);
		ssao = new SSAO();
		fxaa = new FXAA();
		ssaoBlur = new SSAOBlur();
		gBuffer = new GBuffer(WindowHandler.getWidth(), WindowHandler.getHeight());
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		renderer = new ImageRenderer(WindowHandler.getWidth(), WindowHandler.getHeight());
		
		
		modelShader = new ModelShader();
		modelShader.start();
		modelShader.connectTextureUnits();
		modelShader.stop();
		
		
	}	

	public void renderWorld(World world, Vector4f clipPlane, Camera camera) {
		render(world, clipPlane, camera);
	}

	
	
	public void renderGlobalLight(GBuffer gBuffer, int ssao, Sun sunLight, float ambient) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL30.glBindVertexArray(ImageRenderer.quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		globalLightShader.start();

		globalLightShader.loadLight(sunLight, camera);
		globalLightShader.loadViewPos(camera.getPosition(), camera);
		globalLightShader.loadAmbient(ambient, skyColour.toVector3f());

		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gBuffer.gPositionDepth);

		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gBuffer.gNormal);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gBuffer.gAlbedo);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ssao);

		renderer.renderQuad();
		globalLightShader.stop();
				
		GL11.glDepthMask(true);
		
	}
	public SkyboxRenderer getSkyboxRenderer() {
		return skyBoxRenderer;
	}
	
	public EnviroMapRenderer getEnviroMapRenderer() {
		return enviroMapRenderer;
	}
	private void render(World world, Vector4f clipPlane, Camera camera) {
		ArrayList<ModelEntity> renderEntities = world.getEntities();
		for (ModelEntity entity :renderEntities) {
			processEntity(entity);
		}
		prepareWorldRender();
		renderEntities(modelEntities, shadowMapRenderer.getToShadowMapSpaceMatrix(), camera);
		modelEntities.clear();
		
	}
	
	private void prepareModelEntity(ModelEntity modelEntity, Matrix4f toShadowSpace) {
		GL30.glBindVertexArray(modelEntity.getModel().getVaoID());
		modelShader.loadTransformationMatrix(Maths.createTransformationMatrix(modelEntity.getTransformation()));
		modelShader.loadColour(modelEntity.getColour());
		modelShader.loadLight(Engine.getGame().getWorld().getSun());
		modelShader.loadIsHighlighted(modelEntity.isHighlightedd());
	}
	public static int getShadowMaptexture() {
		return shadowMapRenderer.getShadowMap();
	}
	private void bindEnviroMap(Vector3f pos) {
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, enviroMapRenderer.getCubeMapForPosition(pos).textureId);
	}
	private void renderEntities(Map<Model, List<ModelEntity>> entities, Matrix4f toShadowSpace, Camera camera) {
		modelShader.start();

		modelShader.loadProjectionMatrix(camera.getProjectionMatrix());
		modelShader.loadViewMatrix(camera.getviewMatrix());
		modelShader.loadToShadowSpaceMatrix(toShadowSpace);
		modelShader.loadShadowmapSize(ShadowMapMasterRenderer.SHADOW_MAP_SIZE);
		modelShader.loadLight(Engine.getGame().getWorld().getSun());
		modelShader.loadAmbient(Engine.getGame().getWorld().getSun().getAmbientLight());
		modelShader.loadCameraPosition(camera.getPosition());
		GL13.glActiveTexture(GL13.GL_TEXTURE5);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, MasterRenderer.getShadowMaptexture());
		
		for (Model model : entities.keySet()) {
			prepareTexturedModel(model);
			List<ModelEntity> batch = entities.get(model);
			for (ModelEntity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		modelShader.stop();
		
		/*
		for (ModelEntity modelEntity : modelEntities) {

			prepareModelEntity(modelEntity, toShadowSpace);
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL20.glEnableVertexAttribArray(3);
			GL11.glDrawElements(GL11.GL_TRIANGLES, modelEntity.getTexturedModel().getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		modelShader.stop();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
		*/
	}
	private void prepareInstance(ModelEntity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		modelShader.loadTransformationMatrix(transformationMatrix);
		modelShader.loadIsHighlighted(entity.isHighlightedd());
		modelShader.loadColour(entity.getColour());
		modelShader.loadModelReflectivity(entity.getReflectivity());
		bindEnviroMap(entity.getPosition());
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
		GL11.glClearColor(skyColour.r, skyColour.g, skyColour.b, 1.0f);
		camera.updateViewAndProjViewMatrix();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		//GL11.glEnable(GL11.GL_BLEND);
		disableCulling();
		enableCulling();
		//GL13.glActiveTexture(GL13.GL_TEXTURE5);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, getShadowMaptexture());
	}
	
	public void handleResize() {
		camera.updateProjectionMatrix((float) Display.getWidth() / (float) Display.getHeight());
		gBuffer.cleanUp();
		gBuffer = new GBuffer(WindowHandler.getWidth(), WindowHandler.getHeight());
	}
	
	public void update(World world) {
		shadowMapRenderer.update(camera);
		camera.update();
		enviroMapRenderer.update(this, world);
	}
	public void prepare() {
		GL11.glViewport(0, 0, WindowHandler.getWidth(), WindowHandler.getHeight());
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	public Camera getCamera() {
		return camera;
	}
	public void renderShadowMap(List<ModelEntity> entityList, Entity sun) {
		for (ModelEntity entity : entityList) {
			processEntity(entity);
		}
		shadowMapRenderer.render(modelEntities,  sun);
		modelEntities.clear();
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
	public void cleanUp() {
		enviroMapRenderer.cleanUp();
	}

	public static float getShadowDistance() {
		return shadowDistance;
	}
}
