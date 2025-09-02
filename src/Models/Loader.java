package Models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class Loader {


	/*
	public static Model loadToVAO(float[] positions,float[] colours, float[] normals, int[] indices) {
		int vaoID = createVAO();
		int[] VBOS = new int[4];
		VBOS[4] =bindIndicesBuffer(indices);

		VBOS[0] = storeDataInAttributeList(0,3, positions);
		VBOS[1] = storeDataInAttributeList(1,3, colours);
		VBOS[2] = storeDataInAttributeList(2,3, normals);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}
	*/
	
	public static Model loadToVAO(float[] positions,float[] colours, float[]materialProps, int[] indices) {
		int vaoID = createVAO();
		int[] VBOS = new int[4];
		VBOS[3] = bindIndicesBuffer(indices);

		VBOS[0] = storeDataInAttributeList(0,3, positions);
		VBOS[1] = storeDataInAttributeList(1,3, colours);
		VBOS[2] = storeDataInAttributeList(2,3, materialProps);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}

	
	
	public static Model loadToVAO(RawModel rawModel) {
		int vaoID = createVAO();
		int[] VBOS = new int[4];
		VBOS[3] = bindIndicesBuffer(rawModel.indices);

		VBOS[0] = storeDataInAttributeList(0,3, rawModel.positions);
		VBOS[1] = storeDataInAttributeList(1,3, rawModel.normals);
		VBOS[2] = storeDataInAttributeList(2,3, rawModel.colours);
		unbindVAO();
		return new Model(vaoID, VBOS, rawModel.indices.length);
	}
	public static Model loadTerrainToVAO(float[] positions,float[] textureCoords, float[] normals, float[] material, int[] indices) {
		int vaoID = createVAO();
		int[] VBOS = new int[5];
		VBOS[4] = bindIndicesBuffer(indices);

		VBOS[0] = storeDataInAttributeList(0,3, positions);
		VBOS[1] = storeDataInAttributeList(1,2, textureCoords);
		VBOS[2] = storeDataInAttributeList(2,3, normals);
		VBOS[3] = storeDataInAttributeList(3,4, material);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}
	public static Model loadToVAO(float[] positions,float[] textureCoords, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		int[] VBOS = new int[5];
		VBOS[4] = bindIndicesBuffer(indices);

		VBOS[0] = storeDataInAttributeList(0,3, positions);
		VBOS[1] = storeDataInAttributeList(1,2, textureCoords);
		VBOS[2] = storeDataInAttributeList(2,3, normals);
		VBOS[3] = storeDataInAttributeList(3,1, tangents);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}

	public static Model loadToVAO(float[] positions, int[] indices) {
		int vaoID = createVAO();
		int[] VBOS = new int[2];
		VBOS[1] = bindIndicesBuffer(indices);

		VBOS[0] = storeDataInAttributeList(0,3, positions);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}
	public static Model loadToVAO(float[] positions, int[] indices, int dimensions) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		int[] VBOS = new int[1];
		VBOS[0] = storeDataInAttributeList(0,dimensions, positions);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}
	
	public int[] loadToVAO(float[] positions,float[] textureCoords) {
		int vaoID[] = new int[3];
		vaoID[0] = createVAO();
		vaoID[1]  = storeDataInAttributeList(0,2, positions);
		vaoID[2] =	storeDataInAttributeList(1,2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	
	public static Model loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		int[] VBOS = new int[1];
		VBOS[0] = storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new Model(vaoID, VBOS, positions.length/dimensions);
	}
	
	public static Model loadToVAO(float[] positions, float[] colours, int dimensions) {
		int vaoID = createVAO();
		int[] VBOS = new int[2];
		VBOS[0] = storeDataInAttributeList(0, dimensions, positions);
		VBOS[1] = storeDataInAttributeList(1, 4, colours);
		unbindVAO();
		return new Model(vaoID, VBOS, positions.length/dimensions);
	}
	/*
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);	
		
		for (int i = 0 ; i<textureFiles.length ; i++) {
			TextureData data = decodeTextureFile("res/"+textureFiles[i]+".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(), 0 , GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		return texID;
	}
	
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			((Buffer)buffer).flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work");
			System.exit(-1);
		}
		return new TextureData(buffer, width, height);
	}
	*/
	private static int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	
	public void updateVbo(int vbo, float[] data, FloatBuffer buffer) {
		((Buffer)buffer).clear();
		buffer.put(data);
		((Buffer)buffer).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity()*4, GL15.GL_STREAM_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL30.glBindVertexArray(vao);
		GL20.glVertexAttribPointer(attribute, dataSize, GL11.GL_FLOAT, false, instancedDataLength*4, offset*4);
		GL33.glVertexAttribDivisor(attribute, 1);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
	}
	
	public int createEmptyVbo(int floatCount) {
		int vbo = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount*4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vbo;
	}
	
	public static int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/"+fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
			
			if (GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				float amount = (float)Math.min(4f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
				GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, amount);
				//System.out.println(amount+" x Anisotropic Filtering");
			} else {
				System.out.println("Anisotropic Filtering not supported ");
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		return textureID;
		
	}
	
	private static int bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		return vboID;
	}
	
	private static IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		((Buffer)buffer).flip();
		return buffer;
	}
	
	
	private static int storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0,0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return  vboID;
	}

	
	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}
	public static void cleanUp() {
		/*
		for (int vao:vaos) {
			GL30.glDeleteVertexArrays(vao);
		}
		for (int vbo:vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		*/
	}
	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		((Buffer)buffer).flip();
		return buffer;
		
	}
	
}
