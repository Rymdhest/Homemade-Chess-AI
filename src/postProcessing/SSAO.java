package postProcessing;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Random;
import java.util.Vector;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Image;

import Engine.ImageRenderer;
import Engine.MasterRenderer;
import Util.Camera;
import Util.WindowHandler;

public class SSAO {
	public ImageRenderer renderer;
	public SSAOShader shader;
	public SSAOBlurShader shaderBlur;
	private Vector3f kernelSamples[];
	private final int kernelSize =32;
	private int noiseTexture;
	public static float strength = 2.2f;
	public static float radius = 0.55f;
	public static float bias = 0.015f;
	public static boolean ssaoBoost = true;
	public SSAO() {
		renderer = new ImageRenderer(Display.getWidth(), Display.getHeight());
		kernelSamples = new Vector3f[kernelSize];
		Random rand = new Random();
		for (int i = 0 ; i<kernelSize ; i++) {
			Vector3f sample = new Vector3f(rand.nextFloat()*2f-1f, rand.nextFloat()*2f-1f, rand.nextFloat());
			sample.normalise();
			sample.scale(rand.nextFloat());
			float scale = (float)i/kernelSize;
			scale = lerp(0.1f, 1f, scale*scale);
			sample.scale(scale);
			kernelSamples[i] = new Vector3f(sample.x, sample.y, sample.z);
		}
		
		int noiseSize = 4;
		FloatBuffer pixels = BufferUtils.createFloatBuffer(noiseSize*noiseSize*3);
		float f = rand.nextFloat();
		for (int i = 0 ; i<noiseSize*noiseSize ; i++) {
			pixels.put(rand.nextFloat()*2.0f-1.0f);
			pixels.put(rand.nextFloat()*2.0f-1.0f);
			pixels.put(0f);


			//pixels.put(1f);
			//pixels.put(1f);
			//pixels.put(0.0f);
		}
		((Buffer)pixels).flip();
		
		noiseTexture = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, noiseTexture);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB16F, noiseSize, noiseSize, 0, GL11.GL_RGB, GL11.GL_FLOAT, pixels);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);	
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		shader = new SSAOShader();
		shader.start();
		shader.connectTextureUnits();
		shader.stop();
		
		shaderBlur = new SSAOBlurShader();
		//renderer = new ImageRenderer(Display.getWidth(), Display.getHeight());
		//renderer = new ImageRenderer();
	}
	private float lerp(float a, float b, float f) {
		return a+f*(b-a);
	}
	
	public void render(int gPosition, int gNormal, Camera camera) {
		float strengthSpeed = 10.0f;
		if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
			strength += strengthSpeed*WindowHandler.getFrameTimeSeconds();
			System.out.println(strength);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_J)) {
			strength -= strengthSpeed*WindowHandler.getFrameTimeSeconds();
			System.out.println(strength);
		}
		
		float radiusSpeed = 0.5f;
		if (Keyboard.isKeyDown(Keyboard.KEY_T)) {
			radius += radiusSpeed*WindowHandler.getFrameTimeSeconds();
			System.out.println(radius);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_G)) {
			radius -= radiusSpeed*WindowHandler.getFrameTimeSeconds();
			System.out.println(radius);
		}
		
		float biasSpeed = 0.25f;
		if (Keyboard.isKeyDown(Keyboard.KEY_Y)) {
			bias += biasSpeed*WindowHandler.getFrameTimeSeconds();
			System.out.println(bias);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
			bias -= biasSpeed*WindowHandler.getFrameTimeSeconds();
			System.out.println(bias);
		}
		
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL30.glBindVertexArray(ImageRenderer.quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		shader.start();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadSamples(kernelSamples);
		shader.loadSettings(strength, bias, radius);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gPosition);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gNormal);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, noiseTexture);

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		shader.stop();

	}
	
	public void renderBlur(int ssaoText) {
		shaderBlur.start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, ssaoText);
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		
		shaderBlur.stop();
		
	}
	
	public void cleanUp() {
		//renderer.cleanUp();
		shader.cleanUp();
	}
	

	
	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}

}
