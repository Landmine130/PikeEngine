
public interface InputObserver {
	
	public void keyUp(int key);
	public void keyDown(int key);
	public void mouseMoved(int x, int y, int dx, int dy);
	public void mouseDown(int button);
	public void mouseUp(int button);
	public void scroll(int scrollDistance);
}
