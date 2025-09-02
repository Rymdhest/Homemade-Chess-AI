package Util;

import java.util.Timer;

public class StopWatch {
	private float time;
	
	public StopWatch(float time) {
		StopWatchMaster.addStopWatch(this);
		this.time = time;
	}
	
	public void update() {
		increaseTime(-WindowHandler.getFrameTimeSeconds());
	}
	
	public void increaseTime(float amount) {
		time += amount;
	}
	public float getTime() {
		return time;
	}
	
	public void setTime(float setTo) {
		this.time = setTo;
	}
}
