package ai;

import java.util.ArrayList;
import java.util.Timer;

import org.lwjgl.Sys;

import Gameplay.BoardState;
import Gameplay.ModeledBoard;
import Gameplay.Move;
import Gameplay.Piece;
import Gameplay.Piece.Faction;
import Gameplay.Piece.Type;
import Util.Maths;
import Util.StopWatch;
import Util.WindowHandler;

public class AIBrain extends Brain implements Runnable{
	private float delay;
	private StopWatch delayTimer;
	private boolean thinking;
	public float[] pieceValues = {1f, 2f, 2f, 4f, 8f, 200f};
	public float[] oldPieceValues = {1f, 2f, 2f, 4f, 8f, 200f};
	public boolean selfAdapt = false;
	private BoardState currentBoard;
	public AIBrain() {
		delay = 1.65f;
		//delay = 0f;
		thinking = false;
		delayTimer = new StopWatch(delay);
	}
	public void revertValuesToOld() {
		for (int i = 0 ; i<6 ; i++) {
			pieceValues[i]=oldPieceValues[i];
		}
	}

	public void shakeValues() {
		for (int i = 0 ; i<6 ; i++) {
			oldPieceValues[i]=pieceValues[i];
		}
		float value = 0.5f+Maths.rand.nextFloat()*0.5f;
		if (Maths.rand.nextBoolean())value *= -1;
		pieceValues[Maths.rand.nextInt(6)] += value;
		
		System.out.println("Trying new values: ");
		for (int i = 0 ; i<6 ; i++) {
			System.out.print(pieceValues[i]+", ");
		}
		System.out.println("");
	}
	@Override
	public void run() {
		delayTimer.setTime(delay);
		BoardStateTree.resetStaticVars(currentBoard.getCurrentTurn(), pieceValues);
		BoardStateTree tree = new BoardStateTree(currentBoard.makeCopy(), null, 0);
		//BoardStateTree.broadDigUntillLimit(tree);
		//tree.trim();
		long startTime = Sys.getTime();
		BoardStateTree.deepDigAndTrimUntillLimit(tree);
//		System.out.println(tree.getStatesCount()+" tree states with a deepest depth of "+tree.getDeepestDepth());
		long endTime = Sys.getTime();
		double delta = ((double)endTime-(double)startTime)/(double)Sys.getTimerResolution();
		System.out.println("The aiBrain took "+delta+" seconds to calculate");
		Move move = tree.getChildren().get(0).getMoveToGetHere();
		setNextMove(move);
		
	}
	@Override
	public void thinkUpNextMove(BoardState board) {
		if (thinking) {
			return;
		} else {
			thinking = true;
			this.currentBoard = board;
			Thread thinkerThread = new Thread(this);
			thinkerThread.start();
		}
	}
	@Override
	public Move extractNextMove() {
		thinking = false;
		return super.extractNextMove();
	}
	@Override
	public boolean hasMoveReady() {
		if (delayTimer.getTime() >= 0) return false;
		return super.hasMoveReady();
	}




}
