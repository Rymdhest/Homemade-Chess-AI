package ai;

import java.util.ArrayList;

import org.newdawn.slick.state.transition.RotateTransition;

import Gameplay.BoardState;
import Gameplay.Move;
import Gameplay.Piece;
import Gameplay.Piece.Faction;
import Gameplay.Piece.Type;
import Util.Maths;

public class BoardStateTree implements Runnable{
	private ArrayList<BoardStateTree> children;
	private BoardState boardState;
	private Move moveToGetHere;
	private int depth;
	private float tipValue;
	public boolean best = false;
	private static Faction owner;
	private float boardValue;
	private static final int maxDepth = 5; 
	private static float[] pieceValues;
	public BoardStateTree(BoardState state, Move moveToGetHere, int depth) {
		this.boardState = state;
		this.depth = depth;
		this.moveToGetHere = moveToGetHere;

		children = new ArrayList<BoardStateTree>();
		Faction enemy = Faction.White;
		if (owner == Faction.White) enemy = Faction.Black;
		//boardValue = -evaluateBoardValue(state, state.getCurrentTurn())+evaluateBoardValue(state, state.getCurrentNOTTurn());
		boardValue = -evaluateBoardValue(state, enemy)+evaluateBoardValue(state, owner);
	}
	public float getBoardValue() {
		return boardValue;
	}
	public static void setOwner(Faction setTo) {
		owner = setTo;
	}
	
	public void addLeavesTo(ArrayList<BoardStateTree> leaves) {
		if (children.size() == 0) {
			leaves.add(this);
		} else {
			for (BoardStateTree child : children) {
				child.addLeavesTo(leaves);
			}
		}
	}
	@Override
	public void run() {
		deepDigTrim();
		
	}

	public static void deepDigAndTrimUntillLimit(BoardStateTree rootTree) {

		ArrayList<Move> moves = rootTree.boardState.getAllAvailableMovesForFaction(rootTree.boardState.getCurrentTurn());
		ArrayList<Thread> threads = new ArrayList<>();
		ArrayList<BoardState> boardStates = new ArrayList<BoardState>();
		for (Move move : moves) {
			BoardState movedState = rootTree.boardState.makeCopy();
			movedState.makeMove(move);
			boardStates.add(movedState);
			BoardStateTree child =new BoardStateTree(movedState, move, rootTree.depth+1);
			rootTree.children.add(child);
			Thread thread = new Thread(child);
			threads.add(thread);
			thread.start();
			
			
		}
		
		for (Thread thread: threads) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		BoardStateTree best = rootTree.getBestChild();
		rootTree.children.clear();
		rootTree.children.add(best);
		//rootTree.deepDigTrim();

	}
	public void deepDigTrim() {
		if (depth >= maxDepth) {
			tipValue = boardValue;
			return;
		}
		ArrayList<Move> moves = boardState.getAllAvailableMovesForFaction(boardState.getCurrentTurn());
		for (Move move : moves) {
			BoardState movedState = boardState.makeCopy();
			movedState.makeMove(move);
			BoardStateTree child =new BoardStateTree(movedState, move, depth+1);
			children.add(child);
			child.deepDigTrim();
		}
		if (children.size() >0 ) {
			BoardStateTree best = getBestChild();
			tipValue = best.tipValue;
			children.clear();
			if (depth == 0)children.add(best);
		}

	}
	/*
	public static void broadDigUntillLimit(BoardStateTree rootTree) {
		ArrayList<BoardStateTree> treesOnMaxDepth = new ArrayList<>();
		
		while (statesCount < maxSates && deepestDepth < maxDepth) {
			rootTree.addLeavesTo(treesOnMaxDepth);
			for (BoardStateTree tree : treesOnMaxDepth) {
				tree.dig();
			}
			treesOnMaxDepth.clear();
		}

	}
	*/
	/*
	public void dig() {
		ArrayList<Move> moves = boardState.getAllAvailableMovesForFaction(boardState.getCurrentTurn());
		for (Move move : moves) {
			if (statesCount < maxSates) {
				BoardState movedState = boardState.makeCopy();
				movedState.makeMove(move);
				children.add(new BoardStateTree(movedState, this, move, depth+1));
			}
		}
	}
	*/
	public ArrayList<BoardStateTree> getChildren() {
		return children;
	}
	public Move getMoveToGetHere() {
		return moveToGetHere;
	}
	private float evaluateBoardValue(BoardState board, Faction faction) {
		float value = 0;
		for (Piece piece : board.getAlivePiecesOfFaction(faction)) {
			value += getValueOfPiece(piece);
		}
		return value;
	}
	public static void resetStaticVars(Faction newOwner, float[] newPieceValues) {
		owner = newOwner;
		pieceValues = newPieceValues;
	}
	public void trim() {
		if (children.size() == 0) {
			tipValue = boardValue;
			return;
		}
		for (int i = 0 ; i<children.size() ; i++) {
			children.get(i).trim();
			

		}
		//if (children.size()==0) return;
		BoardStateTree best = getBestChild();
		tipValue = best.tipValue;
		children.clear();
		if (depth == 0)children.add(best);
		
		
	}

	public BoardStateTree getBestChild() {
		ArrayList<BoardStateTree> besTrees = new ArrayList<>();
		besTrees.add(children.get(0));
		for (int i = 1 ; i<children.size() ; i++) {
			if (boardState.getCurrentTurn() == owner) {
				if (children.get(i).tipValue > besTrees.get(0).tipValue) {
					besTrees.clear();
					besTrees.add(children.get(i));
				}	else if (children.get(i).tipValue == besTrees.get(0).tipValue) {
					besTrees.add(children.get(i));
				}
			} else {
				if (children.get(i).tipValue < besTrees.get(0).tipValue) {
					besTrees.clear();
					besTrees.add(children.get(i));
				}	else if (children.get(i).tipValue == besTrees.get(0).tipValue) {
					besTrees.add(children.get(i));
				}
			}
			
		}
		return besTrees.get(Maths.rand.nextInt(besTrees.size()));
	}
	public void printTree() {
		for (BoardStateTree child : children) {
			if (moveToGetHere != null)System.out.println(moveToGetHere.toString());
			boardState.printBoard();
			child.printTree();
		}
	}
	private float getValueOfPiece(Piece piece) {
		float value;
		Type type = piece.getType();
		switch (type) {
		case Pawn: 
			value = pieceValues[0];
			break;
		case Knight:
			value = pieceValues[1];
			break;
		case Bishop:
			value = pieceValues[2];
			break;
		case Rook:
			value = pieceValues[3];
			break;
		case Queen:
			value = pieceValues[4];
			break;
		case King:
			value = pieceValues[5];
			break;
		default:
			throw new IllegalArgumentException("Unexpected value: " + type);
		}
		
		return value;
	}

}
