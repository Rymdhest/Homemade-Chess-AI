package ai;

import org.lwjgl.util.vector.Vector3f;


import Engine.MouseHandler;
import Gameplay.BoardState;
import Gameplay.ModeledBoard;
import Gameplay.Move;
import Gameplay.Square;
import Util.MousePicker;

public class HumanBrain extends Brain{

	public HumanBrain() {
		super();
	}

	@Override
	public void thinkUpNextMove(BoardState board) {
		ModeledBoard modeledBoard = (ModeledBoard) board;
		Vector3f point = MousePicker.getCurrentTerrainPoint();
		if( point == null || !MouseHandler.leftWasClicked) return;
		point.x += 0.5f;
		point.z -= 0.5f;
		point.z *=-1;
		if (point.x >= 0 && point.z >= 0) {
			Boolean setNewBoolean = false;
			Square newHighlight = board.getSquare((int)point.x,(int) point.z);
			if (modeledBoard.getHighlightSquare() != null) {
				if (modeledBoard.getHighlightSquare().hasPiece()) {
					boolean contains = false;
					for (int i = 0 ; i<modeledBoard.getHighlightSquare().getPiece().getAvaialableMoves().size() ; i++) {
						Move moveTo = modeledBoard.getHighlightSquare().getPiece().getAvaialableMoves().get(i);

						if (board.isMoveCheckForFaction(moveTo, board.getCurrentTurn())) {
							continue;
						}
						
						Square toSquare = board.getSquare(moveTo.toX, moveTo.toY);
						if (toSquare.equals(newHighlight)) {
							contains = true;
							Move move =modeledBoard.getHighlightSquare().getPiece().getAvaialableMoves().get(i);
							this.setNextMove(move);
							modeledBoard.setHighlightSquare(null);
							break;
						}
					}
					if (contains) {

					} else {
						modeledBoard.setHighlightSquare(null);
						setNewBoolean = true;
					}
				} else {
					setNewBoolean = true;
				}
			} else {
				setNewBoolean = true;
			}
			if (setNewBoolean) {
				if (newHighlight == null) {
					
				} else if (newHighlight.hasPiece()) {
					if (newHighlight.getPiece().getColour() == board.getCurrentTurn()) {
						modeledBoard.setHighlightSquare(newHighlight);
						
					}	
				}
			}
		}

		
	}
	
}
