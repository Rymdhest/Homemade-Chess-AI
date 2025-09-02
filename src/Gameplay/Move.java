package Gameplay;

public class Move {
	public int fromX;
	public int fromY;
	public int toX;
	public int toY;
	
	public Move(Square from, Square to) {
		this.fromX = from.getX();
		this.fromY = from.getY();
		this.toX = to.getX();
		this.toY = to.getY();
	}
	public int getFromX() {
		return fromX;
	}
	public int getFromY() {
		return fromY;
	}
	public int getToX() {
		return toX;
	}
	public int getToY() {
		return toY;
	}
	
	@Override
	public String toString() {
		return ""+fromX+","+fromY+" to "+toX+","+toY;
	}
}
