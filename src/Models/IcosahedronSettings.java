package Models;

import org.lwjgl.util.vector.Vector3f;

public class IcosahedronSettings {
	
	public Vector3f size;
	public IcosahedronSettings(Vector3f scale) {
		size = scale;
				
	}
	public IcosahedronSettings(float scale) {
		size = new Vector3f(scale, scale, scale);
				
	}
	
}
