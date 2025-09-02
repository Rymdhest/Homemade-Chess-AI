package Engine;


import java.awt.geom.FlatteningPathIterator;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Gameplay.ModelEntity;
import Models.BoxSettings;
import Models.Colour;
import Models.MeshGenerator;
import Util.Transformation;
import cubeMap.Skybox;
import cubeMap.Texture;
import cubeMap.TextureUtils;
import Util.Maths;
import Util.MyFile;
public class World {

	private Sun sun;
	private Skybox skyBox;
	private WorldClock worldClock;
	private ArrayList<ModelEntity> entities;
	ModelEntity sunEntity;
	public World() {
		System.out.println("Making World");
		worldClock = new WorldClock();
		sun = new Sun();
		entities = new ArrayList<ModelEntity>();
		
		MyFile[] skyboxFiles = new MyFile[6];
		skyboxFiles[0] = new MyFile("res/posx.png");
		skyboxFiles[1] = new MyFile("res/negx.png");
		skyboxFiles[2] = new MyFile("res/posy.png");
		skyboxFiles[3] = new MyFile("res/negy.png");
		skyboxFiles[4] = new MyFile("res/posz.png");
		skyboxFiles[5] = new MyFile("res/negz.png");
		skyBox = new Skybox(new Texture(TextureUtils.loadCubeMap(skyboxFiles), GL13.GL_TEXTURE_CUBE_MAP, 2048), 120f);

		float tableSize = 7.5f;
		ModelEntity table = new ModelEntity(new Transformation(new Vector3f(4f-0.5f, 0f, -4f+0.5f), new Vector3f(), 1f), Models.Loader.loadToVAO( MeshGenerator.generateBox(new BoxSettings(new Vector3f(tableSize, 0.5f, tableSize), new Vector3f(tableSize*0.9f, 0.0f, tableSize*0.9f)))));
		table.setColour(new Colour(0.5f, 0.4f, 0.2f));
		entities.add(table);
		sunEntity = new ModelEntity(new Transformation(),Models.Loader.loadToVAO( MeshGenerator.generateBox(new BoxSettings())));
		//entities.add(sunEntity);
	}
	public Skybox getSkyBox() {
		return skyBox;
	}
	public void update() {
		worldClock.update();
		sun.update();
		sunEntity.setPosition(sun.getPosition());
		for (ModelEntity entity : entities) {
			entity.update();
		}
		
		
	}
	public void clearEntities() {
	}
	public void addEntity(ModelEntity entity) {
		entities.add(entity);
	}
	public void render(Vector4f clipPlane) {

		
		/*
		masterRenderer.waterRenderer.waterFrameBuffers.bindRefractionFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		masterRenderer.renderWorld(this, new Vector4f(0, -1, 0, WaterTile.height+0.025f));
		
		masterRenderer.waterRenderer.waterFrameBuffers.bindReflectionFrameBuffer();
		Camera camera = Engine.getCamera();
		float distance = 2*(camera.getPosition().y-WaterTile.height);
		camera.increasePosition(new Vector3f(0, -distance, 0));
		camera.invertPitch();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		masterRenderer.renderWorld(this, new Vector4f(0, 1, 0, -WaterTile.height+0.025f));
		camera.increasePosition(new Vector3f(0, distance, 0));
		camera.invertPitch();
		
		masterRenderer.waterRenderer.waterFrameBuffers.bindRefractionFrameBuffer();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		masterRenderer.renderWorld(this, new Vector4f(0, -1, 0, WaterTile.height+0.025f));
		
		masterRenderer.waterRenderer.waterFrameBuffers.unbindCurrentFrameBuffer();
		*/

		

		Engine.masterRenderer.renderWorld(this, clipPlane, Engine.masterRenderer.getCamera());
		//masterRenderer.waterRenderer.renderTiles();

	}
	public ArrayList<ModelEntity> getEntities() {
		return entities;
	}
		
	public Sun getSun() {
		return sun;
	}
		
	public void cleanUp() {
		System.out.println("Cleaning Up World");
	}
	
	public void removeEntity(ModelEntity entity) {
		if (entity != null) {
		}
	}
}
