package Util;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import Engine.Engine;

public class Camera {

	private Vector3f position;
	private Vector3f rotation;	
	private float fieldOfView;
	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f projectionViewMatrix;
	public float NEAR_PLANE = 0.1f;
	public float FAR_PLANE = 300f;  ///// ALSO CHANGE WATERFRAGMENT VALUE OR DO UNIFORM
	public Camera(float aspectRatio, float fov) {
		this.fieldOfView = fov;
		position = new Vector3f();
		rotation = new  Vector3f();
		viewMatrix = new Matrix4f();
		projectionViewMatrix = new Matrix4f();
		projectionMatrix = new Matrix4f();
		updateProjectionMatrix(aspectRatio);
		updateViewAndProjViewMatrix();
	}
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	public Matrix4f getviewMatrix() {
		return viewMatrix;
	}
	public Matrix4f getprojectionViewMatrix() {
		return projectionViewMatrix;
	}
	public float getNearPlane() {
		return NEAR_PLANE;
	}
	public float getFarPlane() {
		return FAR_PLANE;
	}
	public float getFOV() {
		return fieldOfView;
	}
	public void setFOV(float setTo) {
		this.fieldOfView = setTo;
	}
	public void update() {
		float walkSpeed = 10f;
		float turnSpeed = 110f;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			moveForward(walkSpeed*WindowHandler.getFrameTimeSeconds());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			moveForward(-walkSpeed*WindowHandler.getFrameTimeSeconds());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			increaseRotation(new Vector3f(0, -turnSpeed*WindowHandler.getFrameTimeSeconds(), 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			increaseRotation(new Vector3f(0, turnSpeed*WindowHandler.getFrameTimeSeconds(), 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			increaseRotation(new Vector3f(-turnSpeed*WindowHandler.getFrameTimeSeconds(), 0, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
			increaseRotation(new Vector3f(turnSpeed*WindowHandler.getFrameTimeSeconds(), 0, 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			increasePosition(new Vector3f(0, -walkSpeed*0.5f*WindowHandler.getFrameTimeSeconds(), 0));
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
			increasePosition(new Vector3f(0, walkSpeed*0.5f*WindowHandler.getFrameTimeSeconds(), 0));
		}
	}
	public void updateViewAndProjViewMatrix() {
		viewMatrix.setIdentity();
		Matrix4f.rotate((float)Math.toRadians(getRoll()), new Vector3f(0,0,1), viewMatrix, viewMatrix);
		Matrix4f.rotate((float)Math.toRadians(getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float)Math.toRadians(getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-getPosition().x, -getPosition().y, -getPosition().z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);

		Matrix4f.mul(projectionMatrix, viewMatrix, projectionViewMatrix);
	}
	public void updateProjectionMatrix(float aspectRatio){
		//float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fieldOfView / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		projectionMatrix.m33 = 0;
    }
	public Vector3f getPosition() {
		return position;
		
	}	
	public Vector3f getRotation() {
		return rotation;
	}	
	public void invertPitch() {
		rotation.x = -rotation.x;
	}
	public float getPitch() {
		return rotation.x;
	}
	public float getYaw() {
		return rotation.y;
	}
	public float getRoll() {
		return rotation.z;
	}
	public void setPitch(float setTo) {
		rotation.x = setTo;
	}
	public void setYaw(float setTo) {
		rotation.y = setTo;
	}
	public void setRoll(float setTo) {
		rotation.z = setTo;
	}
	public void increasePosition(Vector3f increase) {
		position.x += increase.x;
		position.y += increase.y;
		position.z += increase.z;
	}
	public void increaseRotation(Vector3f increase) {
		rotation.x += increase.x;
		rotation.y += increase.y;
		rotation.z += increase.z;
	}
	public void moveForward(float amount) {
		position.x += amount*(float)Math.sin(Math.toRadians(rotation.y));
		position.z -= amount*(float)Math.cos(Math.toRadians(rotation.y));
	}
	public void setRotation(Vector3f newRot) {
		rotation = newRot;
	}
	public void setPosition(Vector3f newPos) {
		position = newPos;
	}
}
