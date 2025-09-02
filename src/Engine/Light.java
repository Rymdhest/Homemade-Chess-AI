package Engine;


import org.lwjgl.util.vector.Vector3f;

import Gameplay.Entity;
import Models.Colour;
import Util.Transformation;


public class Light extends Entity{
	
	private Colour colour;

	public Light(Vector3f position, Colour colour) {
		super(new Transformation(position, new Vector3f(), 1f));
		this.colour = colour;
	}
	
	public Colour getColour() {
		return colour;
	}
	public void setColour(Colour setTo) {
		this.colour = setTo;
	}

}
