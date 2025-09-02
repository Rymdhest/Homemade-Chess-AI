package Models;


import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class Model {
	private int vaoID;
	private int[] VBOS;
	private int vertexCount;
	private String name = "no model name";
	private int polyCount = 0;
	private Colour colour = new Colour(1f, 0, 0);
	public Model(int vaoID, int[] VBOS, int vertexCount) {
		this(vaoID, VBOS, vertexCount, 0f);
	}
	public Model(int vaoID, int[] VBOS, int vertexCount, float spec) {
		this.vaoID = vaoID;
		this.VBOS = VBOS;
		this.vertexCount = vertexCount;
	}

	public void setName(String setTo) {
		this.name = setTo;
	}
	
	public String getName() {
		return name;
	}
	
	public int getVaoID() {
		return vaoID;
	}
	
	public int getVertexCount() {
		return vertexCount;
	}
	public int getPolyCount() {
		return polyCount;
	}
	public void setPolyCount(int setTo) {
		this.polyCount = setTo;
	}
	public void cleanUp() {
		GL30.glDeleteVertexArrays(vaoID);
		for (int i = 0 ; i<VBOS.length ; i++) {
			GL15.glDeleteBuffers(VBOS[i]);
		}
	}
}
