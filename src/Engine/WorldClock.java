package Engine;
import Util.WindowHandler;

public class WorldClock {
	private static float time;
	private static boolean running;
	
	public WorldClock() {
		time = 9f;
		running = true;
	}
	
	public static float getTime() {
		return time;
	}
	public static void setTime(float setTo) {
		time = setTo;
		modulateTime();
	}
	public static void run() {
		running = true;
	}
	public static void stop() {
		running = false;
	}
	public void update() {
		if (running) {
			time+=WindowHandler.getFrameTimeSeconds()*0.2f;
			modulateTime();
		}
	}
	private static void modulateTime() {
		time = time%24;
	}

}
