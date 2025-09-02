package Gameplay;
import java.io.Console;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import Engine.World;
import Engine.WorldClock;
import Gameplay.Piece.Faction;
import Gameplay.Piece.Type;
import Models.Colour;
import Util.Maths;
import Util.WindowHandler;

public class BoardState {

	protected Square[][] squares;
	public final static int BOARD_SIZE = 8;
	private Piece.Faction currentTurn;
	private int moveCount = 0;
	private ArrayList<Piece> takenPieces;
	private Faction winner;
	private ArrayList<Piece> pieces;
	public BoardState() {
		winner = null;
		pieces = new ArrayList<>();
		takenPieces = new ArrayList<>();
		currentTurn = Piece.Faction.White;
		squares = new Square[BOARD_SIZE][BOARD_SIZE]; 
		for (int i = 0 ; i<BOARD_SIZE ; i++) {
			for (int j = 0 ; j<BOARD_SIZE ; j++) {
				squares[i][j] = new Square(i,j);
			}
		}
	}
	public int getMoveCount() {
		return moveCount;
	}
	public void setStartPositions() {
		squares[0][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Rook, this));
		squares[1][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Knight, this));
		squares[2][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Bishop, this));
		squares[3][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.King, this));
		squares[4][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Queen, this));
		squares[5][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Bishop, this));
		squares[6][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Knight, this));
		squares[7][0].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Rook, this));
		for (int p = 0 ; p<8 ; p++) {
			squares[p][1].setPiece(new Piece(Piece.Faction.Black, Piece.Type.Pawn, this));
		}
		
		squares[0][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.Rook, this));
		squares[1][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.Knight, this));
		squares[2][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.Bishop, this));
		squares[3][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.King, this));
		squares[4][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.Queen, this));
		squares[5][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.Bishop, this));
		squares[6][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.Knight, this));
		squares[7][7].setPiece(new Piece(Piece.Faction.White, Piece.Type.Rook, this));
		for (int p = 0 ; p<8 ; p++) {
			squares[p][6].setPiece(new Piece(Piece.Faction.White, Piece.Type.Pawn, this));
		}
	}
	public boolean isCheck(Faction faction) {
		boolean isCheck = false;
		for (Move move : getAllAvailableMovesForFaction(getOppositeFaction(faction))) {
			Square toSquare = squares[move.toX][move.toY];
			if (toSquare.hasPiece()) {
				if (toSquare.getPiece().getType() == Type.King) {
					isCheck = true;
				}
			}
		}
		return isCheck;
	}
	public static Faction getOppositeFaction(Faction faction) {
		if (faction == Faction.White) return Faction.Black;
		else return Faction.White;
	}
	public boolean isMoveCheckForFaction(Move move, Faction faction) {
		boolean isCheck = false;
		BoardState newState = this.makeCopy();
		newState.makeMove(move);
		if (newState.isCheck(faction)) isCheck = true;
		return isCheck;
	}
	public boolean isCheckMateForFaction(Faction faction) {
		boolean checkMate = true;
		for (Move move : getAllAvailableMovesForFaction(faction)) {
			if (!isMoveCheckForFaction(move, faction)) {
				checkMate = false;
				break;
			}
		}
		return checkMate;
	}
	public BoardState makeCopy() {
		BoardState copy = new BoardState();
		copy.currentTurn = getCurrentTurn();
		copy.winner = this.getWinner();
		copy.moveCount = this.moveCount;
		for (int i = 0 ; i<getPieces().size() ; i++) {
			Piece piece = new Piece(getPieces().get(i).getColour(), getPieces().get(i).getType(), copy);
			if (getPieces().get(i).getSquare() != null) {
				piece.setSquare(copy.squares[getPieces().get(i).getSquare().getX()][getPieces().get(i).getSquare().getY()]);
			}
			piece.setHasMoved(getPieces().get(i).hasMoved());
			if (this.takenPieces.contains(getPieces().get(i))) {
				copy.takenPieces.add(piece);
			}
		}
		return copy;
	}
	public Piece.Faction getCurrentTurn() {
		return currentTurn;
	}
	public Piece.Faction getCurrentNOTTurn() {
		if (getCurrentTurn() == Faction.White) return Faction.Black;
		else return Faction.White;
	}
	public Faction getWinner() {
		return winner;
	}
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	public void setLoser(Faction setTo) {
		if (setTo == Faction.Black) setWinner(Faction.White);
		else setWinner(Faction.Black);
	}
	public void setWinner(Faction setTo) {
		this.winner = setTo;
	}
	public boolean hasWinner() {
		if (winner != null) return true;
		return false;
	}
	public void update() {

	}
	public ArrayList<Move> getAllAvailableMovesForFaction(Faction faction) {
		ArrayList<Move> moves = new ArrayList<>();
		for (Piece piece : getAlivePiecesOfFaction(faction)) {
			moves.addAll(piece.getAvaialableMoves());
		}
		return moves;
	}
	public ArrayList<Piece> getAlivePiecesOfFaction(Faction faction) {
		ArrayList<Piece> alive = new ArrayList<>();
		for (Piece piece : pieces) {
			if (piece.getSquare() != null) {
				if (piece.getColour() == faction) alive.add(piece);
			}
		}
		return alive;
	}

	public ArrayList<Piece> getTakenPices() {
		return takenPieces;
	}
	public ArrayList<Piece> getTakenWhitePices() {
		ArrayList<Piece> takenWhite = new ArrayList<>();
		for (Piece piece : takenPieces) {
			if (!piece.isBlack()) takenWhite.add(piece);
		}
		return takenWhite;
	}
	public ArrayList<Piece> getTakenBlackPices() {
		ArrayList<Piece> takenBlack = new ArrayList<>();
		for (Piece piece : takenPieces) {
			if (piece.isBlack()) takenBlack.add(piece);
		}
		return takenBlack;
	}

	public void takePiece(Piece piece) {
		getTakenPices().add(piece);
		if (piece.getType() == Type.King) {
			setLoser(piece.getColour());
		}
	}

	public void makeMove(Move move) {
		makeMove(move, true);
	}
	public void makeMove(Move move, boolean stepTurn) {
		Piece piece = squares[move.fromX][move.fromY].getPiece();
		Square square = squares[move.toX][move.toY];
		moveCount += 1;
		if (piece.getType() == Type.King) {
			if (piece.getSquare().getX() - square.getX() == 2) {
				makeMove(new Move(getSquare(piece.getSquare().getX()-3, piece.getSquare().getY()), getSquare(piece.getSquare().getX()-1, piece.getSquare().getY())),false);	
			} else 	if (piece.getSquare().getX() - square.getX() == -2) {
				makeMove(new Move(getSquare(piece.getSquare().getX()+4, piece.getSquare().getY()), getSquare(piece.getSquare().getX()+1, piece.getSquare().getY())),false);
				
			}
		}
		if (piece.getType() == Type.Pawn && (move.toY==0 ||move.toY==BOARD_SIZE-1)) {
			promote(piece);
		}
		if (square.hasPiece()) {
			takePiece(square.getPiece());
			square.setPiece(null);
		}
		piece.setHasMoved(true);
		piece.setSquare(square);

		if (stepTurn) stepTurn();
		if (moveCount > 350) {
			if (Maths.rand.nextBoolean()) setWinner(getCurrentTurn());
			else setWinner(getCurrentNOTTurn());
		}
	}
	public void promote(Piece piece) {
		piece.promote();
	}
	public void stepTurn() {
		if (currentTurn == Faction.White) currentTurn = Faction.Black;
		else currentTurn = Faction.White;
	}
	public Square getSquare(int x, int y) {
		if (x < 0) return null;
		if (y < 0) return null;
		if (x>=BOARD_SIZE) return null;
		if (y>=BOARD_SIZE) return null;
		return squares[x][y];
	}
	public void printBoard() {
		Charset utf8 = Charset.forName("UTF-8");
		PrintStream printStream = null;
		try {
			printStream = new PrintStream(System.out, true, utf8.name());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int y = BOARD_SIZE-1 ; y>=0 ; y--) {
			for (int x = 0 ; x<BOARD_SIZE ; x++) {
				if (squares[x][y].hasPiece()) {
					printStream.print("["+ squares[x][y].getPiece().getUnicodeString()+"]\t");
				} else {
					printStream.print("["+" "+"]\t");
				}
			}
			printStream.print("\n");
		}
	}
}
