package Models;
import org.lwjgl.util.vector.Vector3f;
import Util.Maths;
public class Colour {
	public float r;
	public float g;
	public float b;
	
	public Colour(float r, float g, float b) {
		set(r, g, b);
	}
	public Colour(int r, int g, int b) {
		set(r, g, b);
	}
	
	public Colour makeVarianceOfThis(float variance) {
		float newR = r*(1f+Maths.rand.nextFloat()*variance-variance/2f);
		float newG = g*(1f+Maths.rand.nextFloat()*variance-variance/2f);
		float newB = b*(1f+Maths.rand.nextFloat()*variance-variance/2f);
		return new Colour(newR, newG, newB);
	}
	public Vector3f toVector3f() {
		return new Vector3f(r, g, b);
	}
	public Colour scaleNew(float scale) {
		return new Colour(r*scale, g*scale, b*scale);
	}
	public Colour clampByHighest1() {
		float max = Math.max(Math.max(r, g), b);
		return this.scaleNew(1f/max);
	}
	public void set(int r, int g, int b) {
		this.r = r/255f;
		this.g = g/255f;
		this.b = b/255f;
	}
	public void set(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public void scale(Vector3f scale) {
		scale(scale.x, scale.y, scale.z);
	}
	public void scale(float r, float g, float b) {
		this.r *= r;
		this.g *= g;
		this.b *= b;
	}
	public void scale(float scale) {
		scale(scale, scale, scale);
	}
	/*
	 * Approaches the target colour with a weight of 0-1
	 */
	public void blend(Colour target, float weight) {
		Colour differenc = new Colour(target.r-r, target.g-g, target.b-b);
		this.r = r+differenc.r*weight;
		this.g = g+differenc.g*weight;
		this.b = b+differenc.b*weight;
	}
	public Colour makeCopy() {
		return new Colour(r, g, b);
	}
	public void add(float r, float g, float b) {
		this.r += r;
		this.g += g;
		this.b += b;
	}
	public Colour add(float c) {
		this.r += c;
		this.g += c;
		this.b += c;
		return this;
	}
	public void print() {
		System.out.println("Colour: r="+r+" g="+g+" b="+b);
	}
}
