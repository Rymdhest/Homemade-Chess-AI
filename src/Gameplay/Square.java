package Gameplay;
import org.lwjgl.util.vector.Vector3f;

import Engine.World;
import Models.BoxSettings;
import Models.Colour;
import Models.MeshGenerator;
import Models.Model;
import Util.Transformation;
public class Square {

	public Piece piece;
	private int x;
	private int y;
	public Square(int x, int y) {
		piece = null;
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public void setPiece(Piece piece) {
		if (this.piece != null) {
			this.piece.setSquare(null);
		}
		if (piece != null) {
			piece.setSquare(this);
		}
		this.piece = piece;
	}
	public Piece getPiece() {
		return piece;
	}
	public void clearPiece() {
		this.piece = null;
	}
	
	public String print() {
		if (hasPiece()) {
			return piece.getUnicodeString();
		} else {
			return " ";
		}
	}
	public boolean hasPiece() {
		if(piece != null) return true;
		else return false;
	}
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
}
