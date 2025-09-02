package Engine;


import org.lwjgl.util.vector.Vector3f;

import Models.Colour;
import Util.WindowHandler;
import Util.Maths;

public class Sun extends Light{
	private float ambientLight;
	private float sunDistance;
	private float sunlightMultiplier;
	public float sunHeightRation = 0.3f;
	public float globalSpecFactor = 1.0f;;
	public float specularDampener = 256;
	float time = 0.5f;
	public Sun() {
		super(new Vector3f(63f, 4f,-75f), new Colour(0.9f, 0.8f, 1.0f));
		ambientLight = 0.55f;
		sunDistance = 10f;
		sunlightMultiplier = 1.2f;
	}
	
	public void update() {
		super.update();
		//float newY =sunDistance*sunHeightRation -1*Maths.cos((WorldClock.getTime()/24f)*Maths.PI*2f)*(sunDistance*(sunHeightRation*1f));
		time += WindowHandler.getFrameTimeSeconds()*0.0f;
		float newX = Maths.sin(time)*35f+15f;
		float newZ = Maths.cos(time)*35f-15f;
		setPosition(newX, 35f, newZ);
	}
	public float getAmbientLight() {
		return ambientLight;
	}
	public void setAmbientLight(float setTo) {
		this.ambientLight = setTo;
	}
	public void setSunlightMultiplier(float setTo) {
		this.sunlightMultiplier = setTo;
	}
	public float getSunlightMultiplier() {
		return sunlightMultiplier;
	}
	@Override
	public Colour getColour() {
		return super.getColour().scaleNew(sunlightMultiplier);
	}
}
