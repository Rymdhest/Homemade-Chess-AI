package Models;

import org.lwjgl.util.vector.Vector3f;

public class CylinderSettings {
	public int detail;
	public Vector3f botSize;
	public Vector3f topSize;
	public boolean sealTop = true;
	
	public CylinderSettings(Vector3f botSize, Vector3f topSize, int details) {
		this.botSize = botSize;
		this.topSize = topSize;
		this.detail = details;
	}
}
