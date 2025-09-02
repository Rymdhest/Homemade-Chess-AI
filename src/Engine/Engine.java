package Engine;



import java.util.ArrayList;
import java.util.Timer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import Gameplay.Move;
import Gameplay.Piece;
import Gameplay.Square;
import Gameplay.Piece.Faction;
import Util.Camera;
import Util.WindowHandler;
import ai.AIBrain;
import ai.Brain;
import ai.HumanBrain;
import postProcessing.FXAA;
import Util.Maths;
import Util.MousePicker;
import Util.StopWatch;
import Util.StopWatchMaster;;


public class Engine {
	private static Game game;
	public static MasterRenderer masterRenderer;
	private StopWatchMaster timermaster;
	private static Fbo outputFBO;
	private Fbo finalSceeneFBO;
	public static MousePicker mousePicker;
	private static boolean run = false;
	private HumanBrain humanBrain;
	private AIBrain aiBrain;
	private AIBrain aiBrain2;
	private int player1wins = 0;
	private int player2wins = 0;
	private int gamesInTrainingSession = 0;
	private float bestWinRate = 0.5f;
	private boolean render = true;
	private boolean training = false;
	public Engine() {
		WindowHandler.createDisplay();
		mousePicker = new MousePicker();
		
		timermaster = new StopWatchMaster();
		humanBrain = new HumanBrain();
		aiBrain = new AIBrain();
		aiBrain2 = new AIBrain();
		if (training) {
			aiBrain2.shakeValues();
			aiBrain2.selfAdapt = true;
		}

		masterRenderer = new MasterRenderer();
		finalSceeneFBO = new Fbo(WindowHandler.getWidth(), WindowHandler.getHeight(), Fbo.DEPTH_TEXTURE);
		outputFBO = new Fbo(WindowHandler.getWidth(), WindowHandler.getHeight(), Fbo.DEPTH_RENDER_BUFFER);
	}

	public void run() {
		run = true;
		game = new Game(render);
		while (!Display.isCloseRequested() && run) {
			if (Display.wasResized()) {
				handleResize();
			}
			update();
			checkInput();
			render();
			
			
			WindowHandler.updateWindow();
		
			
			
		}
		cleanUp();
	}
	
	public static Game getGame() {
		return game;
	}
	
	private void update() {
		//fpsField.setNewText(""+WindowHandler.getFPS()+" FPS");
		Display.setTitle(WindowHandler.getFPS()+"");
		MouseHandler.update();
		timermaster.update();

		mousePicker.update(masterRenderer.getCamera());
		if (game!=null)game.update();	
		masterRenderer.update(game.getWorld());
		
		Brain player1Brain = aiBrain;
		Brain player2Brain = aiBrain2;
		player2Brain = humanBrain;
		//player1Brain = humanBrain;
		if (!game.getBoard().hasWinner()) {
			if (game.getBoard().getCurrentTurn() == Faction.White) {
				player1Brain.thinkUpNextMove(game.getBoard());
				if (player1Brain.hasMoveReady()) game.getBoard().makeMove(player1Brain.extractNextMove());
			} else {
				player2Brain.thinkUpNextMove(game.getBoard());
				if (player2Brain.hasMoveReady()) game.getBoard().makeMove(player2Brain.extractNextMove());
			}
		}
		
		else {
			System.out.println("Congratulations "+game.getBoard().getWinner()+" you have won the game after "+game.getBoard().getMoveCount()+" moves.");
			if (training) {
				if (game.getBoard().getWinner() == Faction.White) player1wins++;
				else player2wins++;
				gamesInTrainingSession ++;

				
				float winRate = player2wins/(float)(player1wins+player2wins);
				System.out.println("Player 2 winrate is: "+winRate*100f+"% after "+gamesInTrainingSession+" games played.");
				run = true;
				game = new Game(render);
				if (gamesInTrainingSession >= 50 ) {
					System.out.println("The values ");
					for (int i = 0 ; i<6 ; i++) {
						System.out.print(aiBrain2.pieceValues[i]+", ");
					}
					gamesInTrainingSession = 0;
					player1wins = 0;
					player2wins = 0;
					if (winRate > bestWinRate) {
						System.out.print(" were succesfull\n");
						System.out.print(winRate+" beat the best winrate of "+bestWinRate);
						bestWinRate = winRate;
						aiBrain2.shakeValues();

					} else {
						System.out.print(" were not succesfull\n");
						System.out.print(winRate+" did not beat the best winrate of "+bestWinRate);
						aiBrain2.revertValuesToOld();
						aiBrain2.shakeValues();

					}
				}
			}
			
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
				run = false;
			}
			if (Keyboard.getEventKey() == Keyboard.KEY_V && Keyboard.getEventKeyState()) {
				masterRenderer.getEnviroMapRenderer().updateAllEnviroMaps(masterRenderer, game.getWorld());
			}
		}
		

	}
	public void randomMove() {
		
	}
	private void render() {

		masterRenderer.prepare();
		
		if (game!=null && render) {
			masterRenderer.renderShadowMap(game.getWorld().getEntities(), game.getWorld().getSun());
			MasterRenderer.gBuffer.bind();
			//GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);

			GL11.glDepthMask(true);
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			//GL11.glClearColor(0, 0, 0, 1);
			Engine.masterRenderer.renderWorld(game.getWorld(), new Vector4f(0, -1, 0, 9999), masterRenderer.getCamera());
			
		
			masterRenderer.ssao.renderer.fbo.bindFrameBuffer();
			masterRenderer.ssao.render(MasterRenderer.gBuffer.gPositionDepth, MasterRenderer.gBuffer.gNormal, masterRenderer.getCamera());
			masterRenderer.ssaoBlur.renderer.fbo.bindFrameBuffer();
			masterRenderer.ssaoBlur.render(masterRenderer.ssao.getOutputTexture());

			//GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			outputFBO.bindFrameBuffer();
			
			finalSceeneFBO.bindFrameBuffer();
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0f, 0, 0, 1);

//			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
			masterRenderer.renderGlobalLight(MasterRenderer.gBuffer, masterRenderer.ssaoBlur.getOutputTexture(), game.getWorld().getSun(), game.getWorld().getSun().getAmbientLight());						
	
			//GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, MasterRenderer.gBuffer.gBuffer);
			//GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, outputFBO.getFrameBuffer());

			GL30.glBlitFramebuffer(0, 0, Display.getWidth(), Display.getHeight(), 0, 0,Display.getWidth(), Display.getHeight(), GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
			GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
			masterRenderer.fxaa.render(finalSceeneFBO.getColourTexture());
			//finalSceeneFBO.resolveToScreen();

			//GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, MasterRenderer.gBuffer.gBuffer);
			GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, 0);
			GL30.glBlitFramebuffer(0, 0, Display.getWidth(), Display.getHeight(), 0, 0,Display.getWidth(), Display.getHeight(), GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		
			Engine.masterRenderer.getSkyboxRenderer().render(game.getWorld().getSkyBox(), masterRenderer.getCamera());
			
			//outputFBO.bindFrameBuffer();
			//GL11.glEnable(GL11.GL_DEPTH_TEST);
			
			
		
			
		}
		
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
	}

	private void checkInput() {

	}
	public static void close() {
		run = false;
	}
	public static void cleanUp() {
		if (game != null )game.cleanUp();

		masterRenderer.cleanUp();
	}
	private void handleResize() {
		WindowHandler.setSize(Display.getWidth(), Display.getHeight());
		
		outputFBO.resize(WindowHandler.getWidth(), WindowHandler.getHeight());
		masterRenderer.handleResize();
		MasterRenderer.fxaa = new FXAA();
		
	}
	public static void removeGame() {
		if (game != null)game.cleanUp();
		game = null;
	}
}
