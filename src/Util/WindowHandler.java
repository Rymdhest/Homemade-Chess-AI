package Util;

import org.lwjgl.opengl.ContextAttribs;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;
import org.newdawn.slick.opengl.ImageIOImageData;

public class WindowHandler {
	private static int width  = 1920;
	private static int height = 1080;
	private static boolean resizable = true;
	private static String name = "Chess Mate";
	private static int version = 35;
	private static boolean useVSync = true;
	private static int max_FPS = 60;
	private static float delta;
	private static int framesLastSecond;
	private static long timeLastSecond;
	private static int framesCurrentSecond;
	private static long lastFrameTime;
	public static void createDisplay() {
		ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat().withDepthBits(24), attribs);
			Display.setTitle(name+" (Version "+version+")");
			/*
			try {
				Display.setIcon(new ByteBuffer[] {
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/GUI/Game_Icon_16.png")), false, false, null),
				        new ImageIOImageData().imageToByteBuffer(ImageIO.read(new File("res/GUI/Game_Icon_32.png")), false, false, null)
				        });
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			*/
			timeLastSecond = getCurrentTime();
			framesCurrentSecond = 0;
			framesLastSecond = 0;
			
			Display.setResizable(resizable);
			Display.setVSyncEnabled(useVSync);
			GL11.glEnable(GL13.GL_MULTISAMPLE);
			
		}catch (LWJGLException e) {
			
		}
		lastFrameTime = getCurrentTime();
		GL11.glViewport(0, 0, width, height);
		updateWindow();
		
	}
	public static boolean getVSync() {
		return useVSync;
	}
	public static void setVSync(boolean setTo) {
		useVSync = setTo;
		Display.setVSyncEnabled(useVSync);
	}
	
	public static void updateWindow() {
		Display.sync(max_FPS);
		Display.update();
		
		long currentFrameTime = getCurrentTime();

		delta = (currentFrameTime-lastFrameTime)/1000f;
		lastFrameTime = currentFrameTime;
		framesCurrentSecond++;
		if (currentFrameTime-timeLastSecond > 1000) {
			timeLastSecond = currentFrameTime;
			framesLastSecond = framesCurrentSecond;
			framesCurrentSecond = 0;
		}
	}
	public static void setSize(int newWidth, int newHeight) {
		width = newWidth;
		height = newHeight;
	}
	public static float getFrameTimeSeconds() {
		return delta;
	}
	public static int getWidth() {
		return width;
	}
	public static int getHeight() {
		return height;
	}
	public static int getFPS() {
		return framesLastSecond;
	}
	public static long getCurrentTime() {
		return Sys.getTime()*1000/Sys.getTimerResolution();
	}
}
