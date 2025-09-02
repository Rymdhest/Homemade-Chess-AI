package Engine;


import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import Models.Model;


public class ImageRenderer {
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	public static final Model quad = VAOLoader.loadToVAO(POSITIONS, 2);
	public Fbo fbo;
	public boolean renderToScreen = false;
	public ImageRenderer(int width, int height) {
		this.fbo = new Fbo(width, height, fbo.DEPTH_TEXTURE);
	}

	public void renderQuad() {
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL14.glBlendEquation(GL14.GL_FUNC_ADD);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);

		if (fbo != null) {
			//fbo.bindFrameBuffer();
		}
		
		if (renderToScreen) {
			


		} else {
			

			
		}

	
		if (fbo != null) {
			//fbo.unbindFrameBuffer();
		}
	}

	public int getOutputTexture() {
		return fbo.getColourTexture();
	}

	public void cleanUp() {
		if (fbo != null) {
			fbo.cleanUp();
		}
	}

}
