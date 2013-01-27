
import org.lwjgl.input.Keyboard;
import vecmath.Vector3f;

public class Main implements InputObserver {
	
	private static World world;
	private static VisibleObject o;
	private static VisibleObject p;
	public static void main(String[] args) {
		
		world = new World();
		world.getViewPoint().setPosition(new Vector3f(0,0,0));
		world.getViewPoint().setOrientation(new Vector3f(0,MathF.degreesToRadians(0),0));
		o = new VisibleObject("banana");
		o.setPosition(new Vector3f(0,0,-50));
		world.setViewPoint(new TargetedViewPoint(o, -50));
		p = new VisibleObject("banana");
		p.setPosition(new Vector3f(0,.5f,-50));
		Main mainObject = new Main();
		InputHandler.addObserver(mainObject);
		world.addObject(o);
		world.addObject(p);
		world.start();
	}
	
	public void keyUp(int key) {
		
	}
	
	public void keyDown(int key) {
		if (key == Keyboard.KEY_ESCAPE) {
			world.setPaused(!world.isPaused());
		}
		else {
			/*
			if (key == Keyboard.KEY_1) {
				world.getViewPoint().move(new Vector3f(-1,0,0));
			}
			else if (key == Keyboard.KEY_2) {
				world.getViewPoint().move(new Vector3f(1,0,0));
			}
			else if (key == Keyboard.KEY_3) {
				world.getViewPoint().move(new Vector3f(0,-1,0));
			}
			else if (key == Keyboard.KEY_4) {
				world.getViewPoint().move(new Vector3f(0,1,0));
			}
			else if (key == Keyboard.KEY_5) {
				world.getViewPoint().move(new Vector3f(0,0,-1));
			}
			else if (key == Keyboard.KEY_6) {
				world.getViewPoint().move(new Vector3f(0,0,1));
			}
			/*
			if (key == Keyboard.KEY_1) {
				o.move(new Vector3f(-1,0,0));
			}
			else if (key == Keyboard.KEY_2) {
				o.move(new Vector3f(1,0,0));
			}
			else if (key == Keyboard.KEY_3) {
				o.move(new Vector3f(0,-1,0));
			}
			else if (key == Keyboard.KEY_4) {
				o.move(new Vector3f(0,1,0));
			}
			else if (key == Keyboard.KEY_5) {
				o.move(new Vector3f(0,0,-1));
			}
			else if (key == Keyboard.KEY_6) {
				o.move(new Vector3f(0,0,1));
			}
			*/
			if (key == Keyboard.KEY_1) {
				world.getViewPoint().rotate(new Vector3f(MathF.degreesToRadians(-90),0,0));
			}
			else if (key == Keyboard.KEY_2) {
				world.getViewPoint().rotate(new Vector3f(MathF.degreesToRadians(90),0,0));
			}
			else if (key == Keyboard.KEY_3) {
				world.getViewPoint().rotate(new Vector3f(0,MathF.degreesToRadians(-90),0));
			}
			else if (key == Keyboard.KEY_4) {
				world.getViewPoint().rotate(new Vector3f(0,MathF.degreesToRadians(90),0));
			}
			else if (key == Keyboard.KEY_5) {
				world.getViewPoint().rotate(new Vector3f(0,0,MathF.degreesToRadians(-90)));
			}
			else if (key == Keyboard.KEY_6) {
				world.getViewPoint().rotate(new Vector3f(0,0,MathF.degreesToRadians(90)));
			}
			/*
			if (key == Keyboard.KEY_1) {
				o.rotate(new Vector3f(-.3f,0,0));
			}
			else if (key == Keyboard.KEY_2) {
				o.rotate(new Vector3f(.3f,0,0));
			}
			else if (key == Keyboard.KEY_3) {
				o.rotate(new Vector3f(0,-.3f,0));
			}
			else if (key == Keyboard.KEY_4) {
				o.rotate(new Vector3f(0,.3f,0));
			}
			else if (key == Keyboard.KEY_5) {
				o.rotate(new Vector3f(0,0,-.3f));
			}
			else if (key == Keyboard.KEY_6) {
				o.rotate(new Vector3f(0,0,.3f));
			}
			*/
			//System.out.println(world.getViewPoint().getTransformationMatrix());
			System.out.println(o.getTransformationMatrix());
		}
	}
	
	public void mouseMoved(int x, int y, int dx, int dy) {
		System.out.println("Mouse: X=" + dx + " Y=" + dy);
	}
	
	public void mouseDown(int button) {
		System.out.println("Mouse button " + button + " down");
	}
	
	public void mouseUp(int button) {
		System.out.println("Mouse button " + button + " up");
	}
	
	public void scroll(int scrollDistance) {
		System.out.println("Scrolled: " + scrollDistance);
	}

}
