package Gameplay;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import Engine.World;
import Engine.WorldClock;
import Gameplay.Piece.Faction;
import Gameplay.Piece.Type;
import Models.BoxSettings;
import Models.Colour;
import Models.MeshGenerator;
import Models.Model;
import Util.Maths;
import Util.Transformation;

public class ModeledBoard extends BoardState{

	private ModelEntity[][] squareModels;
	private ModelEntity[][] piecesEntities;
	private World world;
	private Square highLightedSquare = null;
	public ModeledBoard(World world) {
		super();
		this.world = world;
		float squareSize = 0.45f;
		float squareHeight = 0.2f;
		Model model = Models.Loader.loadToVAO(MeshGenerator.generateBox(new BoxSettings(new Vector3f(squareSize,0.0f,squareSize), new Vector3f(squareSize,squareHeight,squareSize))));
		squareModels = new ModelEntity[BOARD_SIZE][BOARD_SIZE];
		piecesEntities = new ModelEntity[BOARD_SIZE][BOARD_SIZE];
		for (int y = 0 ; y<BOARD_SIZE ; y++) {
			for (int x = 0 ; x<BOARD_SIZE ; x++) {
				ModelEntity worldModel = new ModelEntity(new Transformation(new Vector3f(1*x, 0, -1f*y), new Vector3f(0, 0, 0), 1f), model);
				worldModel.setColour(evaluateSquareColour(x,y));
				worldModel.setReflectivity(0.05f);
				world.addEntity(worldModel);
				squareModels[x][y] = worldModel;
			}
		}



	}
	@Override
	public void setStartPositions() {
		super.setStartPositions();
		for (int i = 0 ; i<getPieces().size() ; i++) {
			Model pieceModel = Piece.getModel(getPieces().get(i).getType());
			ModelEntity piecEntity = new ModelEntity(new Transformation(new Vector3f(getPieces().get(i).getSquare().getX(), 0.2f, -getPieces().get(i).getSquare().getY()), new Vector3f(0f,0f,0f), 1f), pieceModel);
			if (getPieces().get(i).isBlack()) {
				piecEntity.setColour(new Colour(52, 33, 29));
				piecEntity.setReflectivity(0.05f);
			} else {
				piecEntity.setColour(new Colour(207, 186, 133));
				piecEntity.setReflectivity(0.09f);
				
			}
			if (getPieces().get(i).getColour() == Faction.White) piecEntity.increaseRotation(0, 180, 0);
			world.addEntity(piecEntity);
			piecesEntities[getPieces().get(i).getSquare().getX()][getPieces().get(i).getSquare().getY()] = piecEntity;
			piecEntity.jumpTo(new Vector3f(getPieces().get(i).getSquare().getX(), 0.2f, -getPieces().get(i).getSquare().getY()));
		}
	}
	public ModelEntity getHighlightModel(Square square) {
		return squareModels[square.getX()][square.getY()];
	}
	public void setHighlightSquare(Square setTo) {
		resetAllSquareColours();
		highLightedSquare = setTo;
		if (setTo !=null) {
			//highLightedSquare.getModelEntity().setColour(new Colour(0.3f, 0.85f, 0.3f));
			getHighlightModel(setTo).getColour().blend(new Colour(0.3f, 0.85f, 0.3f), 0.85f);
			if (highLightedSquare.hasPiece()) {
				for (Move move : highLightedSquare.getPiece().getAvaialableMoves()) {
					if (isMoveCheckForFaction(move, getCurrentTurn())) {
						continue;
					}
					//square.getModelEntity().setColour(new Colour(0.1f, 0.1f, 0.9f));
					Square moveToSquare = squares[move.toX][move.toY];
					if (moveToSquare.hasPiece()) {
						getHighlightModel(moveToSquare).getColour().blend(new Colour(0.9f, 0.1f, 0.1f), 0.75f);
					} else {
						getHighlightModel(moveToSquare).getColour().blend(new Colour(0.1f, 0.1f, 0.9f), 0.75f);
					}
					
				}
			}
		}
	}
	@Override
	public void takePiece(Piece piece) {
		if (piece.isBlack()) {
			float x = (float) Math.floor((getTakenBlackPices().size())/8f);
			float z = 7-(getTakenBlackPices().size())%8;
			piecesEntities[piece.getSquare().getX()][piece.getSquare().getY()].jumpTo(new Vector3f(-2f-x, 0.f, -z));
		}
		else {
			float x = (float) Math.floor((getTakenWhitePices().size())/8f);
			float z = (getTakenWhitePices().size())%8;
			piecesEntities[piece.getSquare().getX()][piece.getSquare().getY()].jumpTo(new Vector3f(9f+x, 0.f, -z));
		}
		super.takePiece(piece);
	}
	@Override
	public void makeMove(Move move, boolean stepTurn) {
		System.out.println(getCurrentTurn()+" moves "+move.toString());
		super.makeMove(move, stepTurn);
		if (squares[move.toX][move.toY] != null) {
			piecesEntities[move.fromX][move.fromY].jumpTo(new Vector3f(move.toX, 0.2f, -move.toY));
			piecesEntities[move.toX][move.toY] = piecesEntities[move.fromX][move.fromY];
			piecesEntities[move.fromX][move.fromY] = null;
		}
		resetAllSquareColours();
		squareModels[move.fromX][move.fromY].getColour().blend(new Colour(0.9f, 0.9f, 0.1f), 0.65f);
		squareModels[move.toX][move.toY].getColour().blend(new Colour(0.9f, 0.9f, 0.1f), 0.65f);
		
		if (isCheckMateForFaction(getCurrentTurn())) {
			setWinner(getCurrentNOTTurn());
		}
	}
	public void setCheckColours() {
		ArrayList<Move> allMoves = new ArrayList<>();
		allMoves.addAll(getAllAvailableMovesForFaction(getCurrentNOTTurn()));
		allMoves.addAll(getAllAvailableMovesForFaction(getCurrentTurn()));
		for (Move m : allMoves) {
			Square toSquare = getSquare(m.toX, m.toY);
			if (toSquare.hasPiece()) {
				if (toSquare.getPiece().getType() == Type.King) {
					squareModels[m.toX][m.toY].getColour().blend(new Colour(1f, 0f, 0f), 0.85f);
					System.out.println("setting check colour");
				}
			}
		}
	}
	@Override
	public void promote(Piece piece) {
		super.promote(piece);
		System.out.println("Promoting");
		piecesEntities[piece.getSquare().getX()][piece.getSquare().getY()].setModel(Piece.getModel(piece.getType()));
	}
	public Square getHighlightSquare() {
		return highLightedSquare;
	}
	public void setHighlightSquare(int x, int y) {
		
		if (x >= 0 && x <BOARD_SIZE) {
			if (y >= 0 && y<BOARD_SIZE) {
				setHighlightSquare(getSquare(x, y));	
			}
		}
	}
	@Override
	public void update() {
		super.update();
		if (hasWinner()) {
			for (int y = 0 ; y<BOARD_SIZE ; y++) {
				for (int x = 0 ; x<BOARD_SIZE ; x++) {
					if (squares[y][x].hasPiece()) {
						if (squares[y][x].getPiece().getColour() == getWinner()) {
							piecesEntities[y][x].setY(0.2f+Maths.sin(WorldClock.getTime()*30f+y*x)*0.5f+0.5f);
						} else {

						}
					}
				}
			}
		}
	}
	public Colour evaluateSquareColour(int x, int y) {
		int black = (x+y)%2;
		float col = 0.9f;
		if (black==0) col = 0.1f;
		return new Colour(col, col, col);
	}
	public void resetAllSquareColours() {
		for (int y = 0 ; y<BOARD_SIZE ; y++) {
			for (int x = 0 ; x<BOARD_SIZE ; x++) {
				squareModels[x][y].setColour(evaluateSquareColour(x,y));
			}
		}
		setCheckColours();
	}
	
}