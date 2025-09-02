package Util;


import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class Maths {
	
	public static final float PI = (float)Math.PI;
	public static final Random rand = new Random();
	
	public static void transform(Matrix4f transform, Vector3f right, Vector3f dest) {
		Vector4f temp = new Vector4f(dest.x, dest.y, dest.z, 1.0f);
		Matrix4f.transform(transform, new Vector4f(right.x, right.y, right.z, 1.0f), temp);
		dest.x = temp.x;
		dest.y = temp.y;
		dest.z = temp.z;
		temp = null;
	}
	
	public static Vector2f rotate2fDegrees(Vector2f point, float angle) {
		Vector2f rotatedPoint = new Vector2f();
		rotatedPoint.x = point.x*cos(angle)-point.y*sin(angle);
		rotatedPoint.y = point.y*cos(angle)+point.x*sin(angle);
		return rotatedPoint;
	}
	
	public static float getDistanceXZ(Vector2f v1, Vector2f v2) {
		return (float)Math.sqrt( (v2.x-v1.x)*(v2.x-v1.x) + (v2.y-v1.y)*(v2.y-v1.y) );
	}
	public static float atan2(float y, float x) {
		return (float) Math.atan2(y, x);
	}
	public static float toDegrees(float angrad) {
		return (float) Math.toDegrees(angrad);
	}
	public static float getDistanceXZ(Vector3f v1, Vector3f v2) {
		return getDistanceXZ(new Vector2f(v1.x, v1.z), new Vector2f(v2.x, v2.z));
	}
	public static float getRotYAngleBetweenTwoPoints(Vector2f p1, Vector2f p2) {
		return getRotYAngleBetweenTwoPoints(new Vector3f(p1.x, 0, p1.y), new Vector3f(p2.x, 0, p2.y));
	}
	public static float getRotYAngleBetweenTwoPoints(Vector3f p1, Vector3f p2) {
		float angle = ((float)Math.atan2(p2.x-p1.x,p2.z-p1.z)*180f/(float)Math.PI+180f);
		float dx = p2.x-p1.x;
		float dz = p2.z-p1.z;
		 angle = ((float)Math.atan2(dx,dz));
		 angle =(float) Math.toDegrees(angle);
		return angle;
	}
	public static int notUnderZeroInterger(int number) {
		if (number <0) return 0;
		else return number;
	}
	public static int notUnderZeroInterger(float number) {
		return notUnderZeroInterger((int)number);
	}
	
	public static boolean quickRoll100(float chance) {
		if (rand.nextFloat()*100.0f < chance) return true;
		else return false;
	}
	
	public static Matrix4f createTransformationMatrix(Transformation transform) {
		return createTransformationMatrix(transform.pos, transform.rot.x, transform.rot.y, transform.rot.z, transform.scale);
	}
	
	public static Matrix4f createInvertTransformationMatrix(Transformation transform) {
		return createInvertTransformationMatrix(transform.pos, transform.rot.x, transform.rot.y, transform.rot.z, transform.scale);
	}
	public static float sin(float f) {
		return (float)Math.sin(f);
	}
	public static float cos(float f) {
		return (float)Math.cos(f);
	}
	public static float tan(float f) {
		return (float)Math.tan(f);
	}
	public static Matrix4f createInvertTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Vector3f invertTranslation = new Vector3f(-translation.x, translation.y, -translation.z);
		Matrix4f.rotate((float)Math.toRadians(-rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(-ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(-rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.translate(invertTranslation, matrix, matrix);


		Matrix4f.scale(new Vector3f(1f/scale, 1f/scale, 1f/scale),matrix, matrix);
		return matrix;
	}
	public static Matrix4f createTransformationMatrix(float x, float y, float z, float rx, float ry, float rz, float scale) {
		return createTransformationMatrix(new Vector3f(x, y, z), rx, ry, rz, scale);
	}
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale),matrix, matrix);
		return matrix;
	}
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rot, Vector3f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, scale.z),matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();

		Matrix4f.rotate((float)Math.toRadians(camera.getPitch()), new Vector3f(1,0,0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float)Math.toRadians(camera.getYaw()), new Vector3f(0,1,0), viewMatrix, viewMatrix);
		Vector3f negativeCameraPos = new Vector3f(-camera.getPosition().x, -camera.getPosition().y, -camera.getPosition().z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	}
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector3f rot, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.x), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.y), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float)Math.toRadians(rot.z), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	public static float LinearInterpolate(float y0,float y1, float t)
	{
	   return (y0+t*(y1-y0));
	}
	public static float CosineInterpolate(float y0,float y1, float t)
	{
		t = (float)-Math.cos(t*(float)Math.PI)/2f+0.5f;
		return LinearInterpolate(y0, y1, t);
	}
	public static float clamp(float number, float min, float max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}
	public static float clamp01(float number)  {
		return clamp(number, 0.0f, 1.0f);
	}
	public static int clampInt(int number, int min, int max) {
		if (number < min) return min;
		if (number > max) return max;
		return number;
	}
	public static Vector4f v4f(Vector3f v1, float w) {
		return new Vector4f(v1.x, v1.y, v1.z, w);
	}
	public static int[] randomUniqueNumbers(int amount, int bound) {
		Set<Integer>set = new LinkedHashSet<Integer>();
	      while (set.size() < amount) {
	         set.add(rand.nextInt(bound));
	      }
		int[] list= new int[set.size()];
		Iterator<Integer> iterator = set.iterator();
		for (int i = 0 ; i<set.size() ; i++) {
			list[i] = iterator.next();
		}
		return list;
	}
	/**
	 * @param start start of range (inclusive)
	 * @param end end of range (inclusive)
	 * @return the random number within start-end but not one of excludes
	 */
	public static int nextIntInRangeInclusive(int start, int end){
	    int rangeLength = end+1 - start;
	    int randomInt = rand.nextInt(rangeLength) + start;
	    return randomInt;
	}
	
	/**
	 * @param start start of range (inclusive)
	 * @param end end of range (exclusive)
	 * @param excludes numbers to exclude (= numbers you do not want)
	 * @return the random number within start-end but not one of excludes
	 */
	public static int nextIntInRangeButExclude(int start, int end, int... excludes){
	    int rangeLength = end - start - excludes.length;
	    int randomInt = rand.nextInt(rangeLength) + start;

	    for(int i = 0; i < excludes.length; i++) {
	        if(excludes[i] > randomInt) {
	            return randomInt;
	        }

	        randomInt++;
	    }
	    return randomInt;
	}
	public static PixelV2 fromWorldSpaceToScreenSpace(Vector3f worldPos, Camera camera, Matrix4f projectionMatrix) {
		
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		
		Vector4f barPosition = new Vector4f();
		barPosition.x = worldPos.x;
		barPosition.y = worldPos.y;
		barPosition.z = worldPos.z;
		barPosition.w = 1.0f;
		
		Vector4f clipSpacePos = Matrix4f.transform(projectionMatrix, Matrix4f.transform(viewMatrix, barPosition, null), null);
		Vector3f ndcSpacePos = new Vector3f();
		ndcSpacePos.x = clipSpacePos.x/clipSpacePos.w;
		ndcSpacePos.y = clipSpacePos.y/clipSpacePos.w;
		ndcSpacePos.z = clipSpacePos.z/clipSpacePos.w;

		PixelV2 windowSpacePos = new PixelV2();
		windowSpacePos.x = (int)((ndcSpacePos.x+1)/2f*WindowHandler.getWidth());
		windowSpacePos.y = (int)((ndcSpacePos.y+1)/2f*WindowHandler.getHeight());
		
		return windowSpacePos;
	}
}
