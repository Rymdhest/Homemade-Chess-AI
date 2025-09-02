package Util;


import org.lwjgl.util.vector.Vector3f;

public class Transformation {
	public Vector3f pos;
	public Vector3f rot;
	public float scale;
	
	public Transformation (Vector3f pos, Vector3f rot, float scale) {
		this.pos = pos;
		this.rot = rot;
		this.scale = scale;
	}
	public Transformation() {
		this(new Vector3f(0.0f, 0.0f, 0.0f), new Vector3f(0.0f, 0.0f, 0.0f), 1f);
	}
	public Transformation copy() {
		return new Transformation(new Vector3f(pos.x, pos.y, pos.z), new Vector3f(rot.x, rot.y, rot.z), 1f);
	
	}

}
