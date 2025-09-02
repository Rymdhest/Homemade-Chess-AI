package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.newdawn.slick.Image;
import Engine.ImageRenderer;

public class FXAA {
	private ImageRenderer renderer;
	private FXAAShader shader;
	
	public FXAA() {
		shader = new FXAAShader();
		renderer = new ImageRenderer(Display.getWidth(), Display.getHeight());
	}
	
	public void render(int texture) {
		shader.start();
		shader.loadSettings();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		renderer.renderQuad();
		shader.stop();
	}
	
	public void cleanUp() {
		renderer.cleanUp();
		shader.cleanUp();
	}
	
	public int getOutputTexture() {
		return renderer.getOutputTexture();
	}
}
