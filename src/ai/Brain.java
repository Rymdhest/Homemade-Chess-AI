package ai;

import Gameplay.BoardState;
import Gameplay.ModeledBoard;
import Gameplay.Move;

public abstract class Brain{
	private Move nextMove;
	public Brain() {
		
	}
	
	public Move extractNextMove() {
		Move tempMove = nextMove;
		nextMove = null;
		return tempMove;
	}
	
	public boolean hasMoveReady() {
		if (nextMove != null) return true;
		return false;
	}
	public abstract void thinkUpNextMove(BoardState board);
	
	protected void setNextMove(Move move) {
		this.nextMove = move;
	}
}
