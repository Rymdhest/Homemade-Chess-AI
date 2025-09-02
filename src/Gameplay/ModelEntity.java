package Gameplay;

import org.lwjgl.util.vector.Vector3f;

import Util.Maths;
import Models.Colour;
import Models.Model;
import Util.Transformation;
import Util.WindowHandler;
import cubeMap.EnviroMapRenderer;


public class ModelEntity extends Entity{

	private Model texturedModel;
	private boolean isHightligted = false;
	private Colour colour;
	private Vector3f jumpTarget = null;
	private Vector3f startJumpPos = null;
	private float reflectivity = 0;

	private float jumpTime = 1f;
	public ModelEntity(Transformation transformation, Model model) {
		super(transformation);
		this.texturedModel = model;
		this.colour = new Colour(Maths.rand.nextFloat(), Maths.rand.nextFloat(), Maths.rand.nextFloat());
	}
	public float getReflectivity() {
		return reflectivity;
	}
	public void setReflectivity(float setTo) {
		this.reflectivity = setTo;
	}
	@Override
	public void update() {
		super.update();
		if (jumpTarget != null) {
			float jump = Maths.CosineInterpolate(startJumpPos.y, jumpTarget.y, jumpTime);
			Float jumpHeight =0.40f;
			float jumpSpeedSeconds = 0.9f;
			jump -= (Maths.cos(jumpTime*Maths.PI*2f)-1f)*jumpHeight;
			setPosition(Maths.CosineInterpolate(startJumpPos.x, jumpTarget.x, jumpTime),
					jump,
					Maths.CosineInterpolate(startJumpPos.z, jumpTarget.z, jumpTime));
			
			jumpTime += WindowHandler.getFrameTimeSeconds()/jumpSpeedSeconds;
			if (jumpTime > 1.0f) {
				setPosition(jumpTarget.x, jumpTarget.y, jumpTarget.z);
				jumpTarget = null;
				EnviroMapRenderer.needToUpdate();
			}
		}
	}
	public void setColour (Colour colour) {
		this.colour = colour;
	}
	public Colour getColour() {
		return colour;
	}
	public void setTransformation(Transformation transformation) {
		setPosition(transformation.pos);
		setRotX(transformation.rot.x);
		setRotY(transformation.rot.y);
		setRotZ(transformation.rot.z);
		setScale(transformation.scale);
	}
	
	
	public Model getModel() {
		return texturedModel;
	}
	public void setModel(Model setTo) {
		this.texturedModel = setTo;
	}

	public boolean isHighlightedd() {
		return isHightligted;
	}
	public void setHighlted(boolean setTo) {
		this.isHightligted = setTo;
	}
	public void jumpTo(Vector3f pos) {
		if (jumpTarget != null) {
			//position.x = jumpTarget.x;
			//position.z = jumpTarget.y;
			jumpTime = 0.0f;
			startJumpPos = new Vector3f(this.jumpTarget.x, this.jumpTarget.y, this.jumpTarget.z);
		} else {
			jumpTime = 0.0f;
			startJumpPos = new Vector3f(getPosition().x, getPosition().y, getPosition().z);
		}
		jumpTarget = new Vector3f(pos.x, pos.y, pos.z);;
			
	}
}
