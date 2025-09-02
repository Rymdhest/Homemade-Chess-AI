package Gameplay;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.state.transition.RotateTransition;


import Engine.World;
import Models.BoxSettings;
import Models.Colour;
import Models.CylinderSettings;
import Models.IcosahedronSettings;
import Models.Model;
import Models.ModelGenerator;
import Util.Transformation;
import Models.MeshGenerator;
public class Piece {

	public static enum Faction{White, Black};
	public static enum Type{King, Queen, Rook, Bishop, Pawn, Knight};
	

	private Faction colour;
	private Type type;
	private Square square;
	private boolean hasMoved;
	private BoardState board;
	public Piece(Faction colour, Type type, BoardState board) {
		this.colour = colour;
		this.type = type;
		this.board = board;
		board.getPieces().add(this);
		this.hasMoved = false;
		this.square = null;

	}
	public boolean isBlack() {
		if (this.colour == Faction.Black) return true;
		else return false;
	}
	
	public void setSquare(Square square) {
		//if (this.square !=null)this.square.setPiece(null);
		if (this.square !=null)this.square.piece = null;
		this.square = square;
		if (square == null) {
			

		} else {
			//worldModel.setPosition(square.getX(), 0.2f, -square.getY());
			
			//square.setPiece(this);
			
			square.piece = this;
			
		}
		//board.getSquare(square.getX(), square.getY()).setPiece(this);
	}
	public void setHasMoved(boolean setTo) {
		hasMoved = setTo;
	}
	public Faction getColour() {
		return this.colour;
	}
	public void promote() {
		type = Type.Queen;
		//worldModel.setModel(getModel(type));
	}
	public Square getSquare() {
		return square;
	}
	public boolean isAtLastRow() {
		if (isBlack()) {
			if (getSquare().getY() == 7) return true;
		} else {
			if (getSquare().getY() == 0) return true;
		}
		return false;
	}
	public boolean hasMoved() {
		return hasMoved;
	}
	public static Model getModel(Type type) {
		Model model = null;
		if (type == Type.Pawn) {
			model = ModelGenerator.generatePawn();			
		}
		else if (type == Type.King) {
			model = ModelGenerator.generateKing();			
		}
		else if (type == Type.Bishop) {
			model = ModelGenerator.generateBishop();			
		}
		else if (type == Type.Knight) {
			model = ModelGenerator.generateKnight();
		}
		else if (type == Type.Queen) {
			model = ModelGenerator.generateQueen();
		}
		else if (type == Type.Rook) {
			model = ModelGenerator.generateRook();
		} else {
			model = ModelGenerator.generatePawn();
		}
		return model;
	}
	public ArrayList<Move> getAvaialableMoves() {
		ArrayList<Move> moves = new ArrayList<>();
		switch (getType()) {
		case Pawn: {
			getPossibleMovesPawn(moves);
			break;
		}
		case Knight: {
			getPossibleMovesknight(moves);
			break;
		}
		case King: {
			getPossibleMovesKing(moves);
			break;
		} case Rook: {
			getPossibleMovesRook(moves);
			break;
		} case Bishop: {
			getPossibleMovesBishop(moves);
			break;
		} case Queen: {
			getPossibleMovesBishop(moves);
			getPossibleMovesRook(moves);
			break;
		}
		default:
			
		}
		
		return moves;
	}
	
	private void getPossibleMovesKing(ArrayList<Move> moves) {
		tryAddSquareToList(moves, square.getX()+1, square.getY());
		tryAddSquareToList(moves, square.getX()-1, square.getY());
		tryAddSquareToList(moves, square.getX(), square.getY()+1);
		tryAddSquareToList(moves, square.getX(), square.getY()-1);
		
		tryAddSquareToList(moves, square.getX()+1, square.getY()+1);
		tryAddSquareToList(moves, square.getX()-1, square.getY()-1);
		tryAddSquareToList(moves, square.getX()-1, square.getY()+1);
		tryAddSquareToList(moves, square.getX()+1, square.getY()-1);
		
		if(!hasMoved()) {
			Square rookSquare = board.getSquare(getSquare().getX()-3, getSquare().getY());
			if (rookSquare.hasPiece()) {
				boolean clearPath = true;
				for (int i = 1 ; i<3 ; i++) {
					if (board.getSquare(getSquare().getX()-i, getSquare().getY()).hasPiece()) {
						clearPath = false;
					}
				}
				if (rookSquare.getPiece().getType() == Type.Rook && clearPath) {
					if (!rookSquare.getPiece().hasMoved()) {
						tryAddSquareToList(moves, getSquare().getX()-2, getSquare().getY());
					}
				}
			}
		}
		if(!hasMoved()) {
			Square rookSquare = board.getSquare(getSquare().getX()+4, getSquare().getY());
			if (rookSquare.hasPiece()) {
				boolean clearPath = true;
				for (int i = 1 ; i<4 ; i++) {
					if (board.getSquare(getSquare().getX()+i, getSquare().getY()).hasPiece()) {
						clearPath = false;
					}
				}
				if (rookSquare.getPiece().getType() == Type.Rook && clearPath) {
					if (!rookSquare.getPiece().hasMoved()) {
						tryAddSquareToList(moves, getSquare().getX()+2, getSquare().getY());
					}
				}
			}
		}
	}
	private void getPossibleMovesPawn(ArrayList<Move> moves) {
	int direction = 1;
	if (this.colour == Faction.White) direction = -1;
	if (board.getSquare(square.getX(), square.getY()+direction) != null) {
		if (!board.getSquare(square.getX(), square.getY()+direction).hasPiece()) {
			tryAddSquareToList(moves, square.getX(), square.getY()+direction);
			if (!hasMoved()) {
				if (!board.getSquare(square.getX(), square.getY()+direction*2).hasPiece()) {
					tryAddSquareToList(moves, square.getX(), square.getY()+direction*2);
				}	
			}
		}
	}
	if (board.getSquare(square.getX()-1 ,square.getY()+direction) != null) {
		if (board.getSquare(square.getX()-1 ,square.getY()+direction).hasPiece()) {
			if (board.getSquare(square.getX()-1, square.getY()+direction).getPiece().getColour() != getColour()) {
				tryAddSquareToList(moves, square.getX()-1,square.getY()+direction);
			}
		}
	}
	if (board.getSquare(square.getX()+1 ,square.getY()+direction) != null) {
		if (board.getSquare(square.getX()+1 ,square.getY()+direction).hasPiece()) {
			if (board.getSquare(square.getX()+1, square.getY()+direction).getPiece().getColour() != getColour()) {
				tryAddSquareToList(moves, square.getX()+1,square.getY()+direction);
			}
		}
	}
	
	
	
	}
	private void getPossibleMovesknight(ArrayList<Move> moves) {
		tryAddSquareToList(moves, square.getX()-2, square.getY()+1);
		tryAddSquareToList(moves, square.getX()-2, square.getY()-1);
		tryAddSquareToList(moves, square.getX()-1, square.getY()+2);
		tryAddSquareToList(moves, square.getX()-1, square.getY()-2);
		tryAddSquareToList(moves, square.getX()+2, square.getY()+1);
		tryAddSquareToList(moves, square.getX()+2, square.getY()-1);
		tryAddSquareToList(moves, square.getX()+1, square.getY()+2);
		tryAddSquareToList(moves, square.getX()+1, square.getY()-2);

	}
	private void getPossibleMovesBishop(ArrayList<Move> moves) {
		int i = 1;
		while (tryAddSquareToList(moves, square.getX()+i, square.getY()+i)) {
			i++;
		}
		i = 1;
		while (tryAddSquareToList(moves, square.getX()+i, square.getY()-i)) {
			i++;
		}
		i = 1;
		while (tryAddSquareToList(moves, square.getX()-i, square.getY()+i)) {
			i++;
		}
		i = 1;
		while (tryAddSquareToList(moves, square.getX()-i, square.getY()-i)) {
			i++;
		}
	}
	
	private void getPossibleMovesRook(ArrayList<Move> moves) {
		int i = 1;
		while (tryAddSquareToList(moves, square.getX()+i, square.getY())) {
			i++;
		}
		i = 1;
		while (tryAddSquareToList(moves, square.getX()-i, square.getY())) {
			i++;
		}
		i = 1;
		while (tryAddSquareToList(moves, square.getX(), square.getY()+i)) {
			i++;
		}
		i = 1;
		while (tryAddSquareToList(moves, square.getX(), square.getY()-i)) {
			i++;
		}
	}
	/*
	 *@return true if square is empty. else false.
	 * 
	 */
	private boolean tryAddSquareToList(ArrayList<Move> list, int x, int y) {
		Square moveSquare = board.getSquare(x, y);
		if (moveSquare == null) {
			return false;
		}
		else if (moveSquare.hasPiece()) {
			if (moveSquare.getPiece().colour != this.colour) {
				list.add(new Move(getSquare(), moveSquare));	
			}
			return false;
		}
		else {
			list.add(new Move(getSquare(), moveSquare));	
			return true;
		}
	}

	public String getUnicodeString() {
		if (colour == Faction.Black) {
			if (getType() == Type.King) return "\u2654";
			if (getType() == Type.Queen) return "\u2655";
			if (getType() == Type.Rook) return "\u2656";
			if (getType() == Type.Bishop) return "\u2657";
			if (getType() == Type.Knight) return "\u2658";
			if (getType() == Type.Pawn) return "\u2659";
		} else {
			if (getType() == Type.King) return "\u265A";
			if (getType() == Type.Queen) return "\u265B";
			if (getType() == Type.Rook) return "\u265C";
			if (getType() == Type.Bishop) return "\u265D";
			if (getType() == Type.Knight) return "\u265E";
			if (getType() == Type.Pawn) return "\u265F";
		}
		return "ERROR";
	}
	public Type getType() {
		return type;
	}
}