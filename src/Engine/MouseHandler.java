package Engine;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;


public abstract class MouseHandler {

	public static int x, y, dx, dy;
	public static boolean leftWasClicked, rightWasClicked, leftWasReleased, rightWasReleased, rightIsDown, leftIsDown;
	public static int dWheel;
	public static boolean isGrabbed = false;
	public static void update() {
		dWheel = Mouse.getDWheel();
		x = Mouse.getX();
		y = Mouse.getY();
		dx = Mouse.getDX();
		dy = Mouse.getDY();
		leftWasClicked = false;
		leftWasReleased = false;
		rightWasClicked = false;
		rightWasReleased = false;
		while (Mouse.next()) {
			if (Mouse.getEventButton() == 0) {
				if (Mouse.getEventButtonState()) {
					leftWasClicked = true;
				} else {
					leftWasReleased = true;
				}
			}
			if (Mouse.getEventButton() == 1) {
				if (Mouse.getEventButtonState()) {
					rightWasClicked = true;
				} else {
					rightWasReleased = true;
				}
			}
		}

		if (Mouse.isButtonDown(0)) {
			leftIsDown = true;
		} else {
			leftIsDown = false;
		}
		if (Mouse.isButtonDown(1)) {
			rightIsDown = true;
		} else {
			rightIsDown = false;
		}
	}
	public static boolean getLeftOrRightClicked() {
		if (leftWasClicked || rightWasClicked) return true;
		else return false;
	}
	public static boolean getLeftOrRightReleased() {
		if (leftWasReleased || rightWasReleased) return true;
		else return false;
	}
}
