package Util;


import org.lwjgl.util.vector.Vector2f;

public class PixelV2 {
	public int x;
	public int y;
	
	public PixelV2(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public PixelV2() {
		this(0,0);
	}
	public PixelV2 add(PixelV2 other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}
	public PixelV2 addReturnNew(PixelV2 other) {
		int x = this.x+other.x;
		int y = this.y + other.y;
		return new PixelV2(x, y);
	}
	public PixelV2 subtract(PixelV2 other) {
		this.x -= other.x;
		this.y -= other.y;
		return this;
	}
	public Vector2f toFloatVector() {
		return new Vector2f(x, y);
	}
}
