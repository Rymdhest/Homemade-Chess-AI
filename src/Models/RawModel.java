package Models;


import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Util.Maths;


public class RawModel {
	public float[] positions;
	public float[] colours;
	public float[] normals;
	public int[] indices;
	
	public RawModel(float[] positions, float[] normals, float[] colours, int[] indices) {
		this.positions = positions;
		this.colours = colours;
		this.normals = normals;
		this.indices = indices;
	}
	public void transform(Vector3f pos, float rx, float ry, float rz, float scale) {
		transform(pos.x, pos.y, pos.z,  rx,  ry,  rz, scale);
}
	public void transform(float x, float y, float z, float rx, float ry, float rz, float scale) {
		Matrix4f transform = Maths.createTransformationMatrix(new Vector3f(x, y, z), rx, ry, rz, scale);
		Vector4f newPos = new Vector4f();
		for (int i = 0 ; i<positions.length ; i+=3) {
			Matrix4f.transform(transform, new Vector4f(positions[i], positions[i+1], positions[i+2], 1), newPos);
			positions[i] = newPos.x;
			positions[i+1] = newPos.y;
			positions[i+2] = newPos.z;
		}
		Vector4f newNorm = new Vector4f();
		transform = Maths.createTransformationMatrix(new Vector3f(0, 0, 0), rx, ry, rz, 1f);
		for (int i = 0 ; i<normals.length ; i+=3) {
			Matrix4f.transform(transform, new Vector4f(normals[i], normals[i+1], normals[i+2], 1), newNorm);
			normals[i] = newNorm.x;
			normals[i+1] = newNorm.y;
			normals[i+2] = newNorm.z;
		}
	}
	public void mergeModel(RawModel m2) {
		float[] newPositions = new float[positions.length+m2.positions.length];
		float[] newColours = new float[colours.length+m2.colours.length];
		float[] newNormals = new float[normals.length+m2.normals.length];
		int[] newIndices = new int[indices.length+m2.indices.length];
		
		for (int i = 0 ; i<positions.length ; i++) {
			newPositions[i] = positions[i];
			newColours[i] = colours[i];
			newNormals[i] = normals[i];
		}
		for (int i = 0 ; i<indices.length ; i++) {
			newIndices[i] = indices[i];
		}
		for (int i = 0 ; i<m2.positions.length ; i++) {
			newPositions[positions.length+i] = m2.positions[i];
			newColours[positions.length+i] = m2.colours[i];
			newNormals[positions.length+i] = m2.normals[i];
		}
		for (int i =0 ; i<m2.indices.length ; i++) {
			newIndices[indices.length+i] = m2.indices[i]+positions.length/3;
		}
		positions = newPositions;
		colours = newColours;
		normals = newNormals;
		indices = newIndices;
	}
}
