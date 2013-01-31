package misc;

import java.util.ArrayList;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class InputHandler {
	
	private static ArrayList<InputObserver> observers = new ArrayList<InputObserver>();
		
	public static void addObserver(InputObserver o) {
		observers.add(o);
	}
	
	public static void removeObserver(InputObserver o) {
		observers.remove(o);
	}
	
	public static void checkInput() {

		while (Keyboard.next()) {
			
			int key = Keyboard.getEventKey();
			boolean keyState = Keyboard.getEventKeyState();

			
			for (InputObserver o : observers) {
				if (keyState) {
					o.keyDown(key);
				}
				else {
					o.keyUp(key);
				}
			}
		}
		
		int dx = Mouse.getDX();
		int dy = Mouse.getDY();
		
		if (dx != 0 || dy != 0) {

			int x = Mouse.getX();
			int y = Mouse.getY();
	
			for (InputObserver o : observers) {
					o.mouseMoved(x, y, dx, dy);
			}
		}
		
		int dScroll = Mouse.getDWheel();
		
		if (dScroll != 0) {
			for (InputObserver o : observers) {
				o.scroll(dScroll);
			}
		}
		
		while (Mouse.next()) {

			
			int mouseButton = Mouse.getEventButton();
			boolean buttonState = Mouse.getEventButtonState();
			
			if (mouseButton != -1) {
				for (InputObserver o : observers) {
					
					if (buttonState) {
						o.mouseDown(mouseButton);
					}
					else {
						o.mouseUp(mouseButton);
					}
				}
			}
		}
	}
}
