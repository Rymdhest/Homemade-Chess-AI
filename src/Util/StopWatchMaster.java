package Util;

import java.util.ArrayList;

public class StopWatchMaster {
	private static ArrayList<StopWatch> timers;
	
	public StopWatchMaster() {
		timers = new ArrayList<>();
	}
	protected static void addStopWatch(StopWatch timer) {
		timers.add(timer);
	}
	public void update() {
		for (StopWatch timer : timers) {
			timer.update();
		}
	}
}
