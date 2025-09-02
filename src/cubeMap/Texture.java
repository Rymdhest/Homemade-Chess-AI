package cubeMap;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;


public class Texture {

	public final int textureId;
	public final int size;
	private final int type;

	public Texture(int textureId, int size) {
		this.textureId = textureId;
		this.size = size;
		this.type = GL11.GL_TEXTURE_2D;
	}

	public Texture(int textureId, int type, int size) {
		this.textureId = textureId;
		this.size = size;
		this.type = type;
	}

	public void bindToUnit(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, textureId);
	}

	public void delete() {
		GL11.glDeleteTextures(textureId);
	}
	
	public static Texture newEmptyCubeMap(int size) {
		int cubeMapId = TextureUtils.createEmptyCubeMap(size);
		return new Texture(cubeMapId, GL13.GL_TEXTURE_CUBE_MAP, size);
	}

}