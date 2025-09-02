package Engine;


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

import Models.Model;
import Models.RawModel;

public class VAOLoader {

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
	public static Model loadToVAO(float[] positions, int[] indices, int dimensions) {
		int vaoID = createVAO();
		int[] VBOS = new int[2];
		VBOS[1] = bindIndicesBuffer(indices);
		VBOS[0] = storeDataInAttributeList(0,dimensions, positions);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}
	public static Model loadToVAO(float[] positions, float[] normals, int[] indices) {
		int vaoID = createVAO();
		int[] VBOS = new int[3];
		VBOS[2] = bindIndicesBuffer(indices);
		VBOS[0] = storeDataInAttributeList(0,3, positions);
		VBOS[1] = storeDataInAttributeList(1,3, normals);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}
	public static int[] loadToVAO(float[] positions) {
		int vaoID[] = new int[2];
		vaoID[0] = createVAO();
		vaoID[1]  = storeDataInAttributeList(0,2, positions);
		unbindVAO();
		return vaoID;
	}
	public static int[] loadToVAO(float[] positions, float[] textureCoords) {
		int vaoID[] = new int[3];
		vaoID[0] = createVAO();
		vaoID[1]  = storeDataInAttributeList(0,2, positions);
		vaoID[2] = storeDataInAttributeList(1,2, textureCoords);
		unbindVAO();
		return vaoID;
	}
	public static Model loadToVAO(float[] positions,int dimensiotns) {
		int vaoID = createVAO();
		int[] VBOS = new int[1];
		VBOS[0]  = storeDataInAttributeList(0,dimensiotns, positions);
		unbindVAO();
		return new Model(vaoID, VBOS, positions.length);
	}
	public static Model loadToVAO(float[] positions, float[] normals, float[] textureCoords, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		int[] VBOS = new int[5];
		VBOS[4] = bindIndicesBuffer(indices);
		VBOS[0] = storeDataInAttributeList(0,3, positions);
		VBOS[1] = storeDataInAttributeList(1,3, normals);
		VBOS[2] = storeDataInAttributeList(2,2, textureCoords);
		VBOS[3] = storeDataInAttributeList(3,3, tangents);
		unbindVAO();
		return new Model(vaoID, VBOS, indices.length);
	}

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

	private static FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		((Buffer)buffer).flip();
		return buffer;
	}
	
}
