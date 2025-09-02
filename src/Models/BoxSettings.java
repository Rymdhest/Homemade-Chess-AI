package Models;
import org.lwjgl.util.vector.Vector3f;

public class BoxSettings {
	public Vector3f botSize;
	public Vector3f topSize;
	
	public BoxSettings(Vector3f botSize, Vector3f topSize) {
		this.botSize = botSize;
		this.topSize = topSize;
	}
	public BoxSettings() {
		this(new Vector3f(0.5f,0.5f,0.5f), new Vector3f(0.5f,0.5f,0.5f));
	}
	
}
