package Gameplay;


import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Engine.IDGenerator;
import Util.Transformation;

public class Entity {
	private static IDGenerator idGenerator = new IDGenerator();
	private final int ID;
	private boolean isDirtyPos;
	private Transformation transformation;
	public Entity(Transformation transformation) {
		this.transformation = transformation;
		ID = idGenerator.generateNewID();
	}
	public void increasePosition(Vector3f increase) {
		increasePosition(increase.x, increase.y, increase.z);
		setDirtyPos(true);
	}
	public int getID() {
		return ID;
	}
	public void increasePosition(float dx, float dy, float dz) {
		this.transformation.pos.x += dx;
		this.transformation.pos.y += dy;
		this.transformation.pos.z += dz;
		setDirtyPos(true);
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.transformation.rot.x += dx;
		this.transformation.rot.y += dy;
		this.transformation.rot.z += dz;
		transformation.rot.x = modAngle(transformation.rot.x);
		transformation.rot.y = modAngle(transformation.rot.y);
		transformation.rot.z = modAngle(transformation.rot.z);
	}
	public void setY(float y) {
		transformation.pos.y = y;
	}
	public boolean isDirtyPos() {
		return isDirtyPos;
	}
	public void setDirtyPos(boolean setTo) {
		this.isDirtyPos = setTo;
	}
	
	public Vector2f getPositionXZ () {
		return new Vector2f(getPosition().x, getPosition().z);
	}
	public Transformation getTransformation() {
		return transformation;
	}

	public Vector3f getPosition() {
		return transformation.pos;
	}
	
	public Vector3f getPositionForward(float distanceForward) {
		float dx = distanceForward*(float)Math.sin(Math.toRadians(getRotY()));
		float dz = distanceForward*(float)Math.cos(Math.toRadians(getRotY()));
		return new Vector3f(getPosition().x+dx, getPosition().y, getPosition().z+dz);
	}
	public void setPosition(float x, float y, float z) {
		getPosition().x = x;
		getPosition().y = y;
		getPosition().z = z;
		setDirtyPos(true);
	}
	public void setPosition(Vector3f position) {
		this.transformation.pos = position;
		setDirtyPos(true);
	}
	public float getRotX() {
		return transformation.rot.x;
	}
	public void setRotX(float rotX) {
		this.transformation.rot.x = rotX;
		transformation.rot.x = modAngle(transformation.rot.x);
	}
	public float getRotY() {
		return transformation.rot.y;
	}
	public void setRotY(float rotY) {
		this.transformation.rot.y = rotY;
		transformation.rot.y = modAngle(transformation.rot.y);
	}
	public float getRotZ() {
		return transformation.rot.z;
	}
	public void setRotZ(float rotZ) {
		this.transformation.rot.z = rotZ;
		transformation.rot.z = modAngle(transformation.rot.z);
		
	}
	public float getScale() {
		return transformation.scale;
	}
	public void setScale(float scale) {
		this.transformation.scale = scale;
	}
	public void increaseScale(float amount) {
		transformation.scale += amount;
	}
	public void update() {

	}
	public float modAngle(float inAngle) {
		if (inAngle>360) {
			inAngle = inAngle%360;
		} else if (inAngle < 0) {
			inAngle = 360+(inAngle % -360);
		}
		return inAngle;
	}
	public void cleanUp() {
		//PointLightMaster.removePointLightsFromEntity(this);
		//ParticleEmitterMaster.removePointLightsFromEntity(this);
	}

}
