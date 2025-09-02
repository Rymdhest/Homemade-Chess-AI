package Engine;


import javax.swing.border.Border;

import org.lwjgl.input.Keyboard;

import Gameplay.BoardState;
import Gameplay.ModeledBoard;
import Gameplay.Piece;
import Util.WindowHandler;

public class Game {
	private BoardState board;
	private World world;
	public Game(boolean render) {
		if (render) {
			world = new World();
			board = new ModeledBoard(world);
		} else {
			board = new BoardState();
		}
		
		board.setStartPositions();
	}
	
	
	public BoardState getBoard() {
		return board;
	}
	public World getWorld() {
		return world;

	}
	public void update() {
		if (world != null)world.update();
		board.update();
	}
	public void cleanUp() {
		
	}
}
