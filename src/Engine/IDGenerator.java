package Engine;


public class IDGenerator {

	private int currentID;
	
	public IDGenerator() {
		currentID = 0;
	}
	
	public int generateNewID() {
		currentID++;
		if (currentID==Integer.MAX_VALUE) {
			System.out.println("WARNING REACAHED MAX ITEM IDS");
		}
		return (currentID-1);
	}
}
