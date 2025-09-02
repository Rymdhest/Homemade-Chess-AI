package postProcessing;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.Image;

import Engine.ImageRenderer;

public class SSAOBlur {
	public ImageRenderer renderer;
	private SSAOBlurShader shader;
	
	public SSAOBlur() {
		shader = new SSAOBlurShader();
		renderer = new ImageRenderer(Display.getWidth(), Display.getHeight());
		//renderer = new ImageRenderer();
	}
	
	public void render(int texture) {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL30.glBindVertexArray(ImageRenderer.quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		shader.start();
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
