package misc;

import java.nio.FloatBuffer;

import world.terrain.*;
import world.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import vecmath.Vector3f;
import vecmath.Vector3i;

public class Main implements InputObserver {
		
	private static World world;
	private static VisibleObject o;
	private static VisibleObject p;
	private static Timer t;
	
	public static void main(String[] args) {
		
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Robots");
		// To change app icon on Mac, use -Xdock:icon option
		
		Display.setResizable(true);
		
		world = new World(System.nanoTime());
		world.setSkybox(new Skybox());
/*		terrain.get(new Vector3i(0,0,0));
		terrain.get(new Vector3i(0,0,-1));
		terrain.load(new Vector3i(-1,0,0));
		terrain.load(new Vector3i(-1,0,-1));*/

		//world.getViewPoint().setPosition(new Vector3f(0,0,0));
		//world.getViewPoint().setOrientation(new Vector3f(0,MathF.toRadians(0),0));
		
		PlayerCharacter player = new PlayerCharacter("", world.getViewPoint(), world);
		player.setPosition(new Vector3f(0f,1.5f,0f));
		
		//o = new VisibleObject("cube");
		//o.setPosition(new Vector3f(1f,0,30f));
		//world.setViewPoint(new TargetedViewPoint(o, 10));
		//p = new VisibleObject("cube");
		//p.setPosition(new Vector3f(.5f,10f,-50));
		Main mainObject = new Main();
		t = new Timer(1, -1, mainObject, "printFPS");
		InputHandler.addObserver(mainObject);
		//world.addObject(o);
		//world.addObject(p);
		
		
		
		
		world.start();
	}
	
	public void printFPS(Timer t) {
		System.out.println(world.getFPS());
		if (!world.isRunning()) {
			t.stop();
		}
	}
	
	public void keyUp(int key) {
		
	}
	
	public void keyDown(int key) {
		if (key == Keyboard.KEY_ESCAPE) {
			world.setPaused(!world.isPaused());
		}
		else if (key == Keyboard.KEY_P) {
			try {
				Display.setFullscreen(true);
			}
			catch (LWJGLException e) {
				System.err.println("Error: Failed to enter fullscreen");
			}
		}
		else if (key == Keyboard.KEY_F) {
			if (t.isRunning()) {
				t.stop();
			}
			else {
				printFPS(t);
				t.start();
			}
		}
		/*
		else if (key == Keyboard.KEY_1) {
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
		/*
		if (key == Keyboard.KEY_1) {
			world.getViewPoint().rotate(new Vector3f(MathF.toRadians(-1),0,0));
		}
		else if (key == Keyboard.KEY_2) {
			world.getViewPoint().rotate(new Vector3f(MathF.toRadians(1),0,0));
		}
		else if (key == Keyboard.KEY_3) {
			world.getViewPoint().rotate(new Vector3f(0,MathF.toRadians(-1),0));
		}
		else if (key == Keyboard.KEY_4) {
			world.getViewPoint().rotate(new Vector3f(0,MathF.toRadians(1),0));
		}
		else if (key == Keyboard.KEY_5) {
			world.getViewPoint().rotate(new Vector3f(0,0,MathF.toRadians(-1)));
		}
		else if (key == Keyboard.KEY_6) {
			world.getViewPoint().rotate(new Vector3f(0,0,MathF.toRadians(1)));
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
		//System.out.println(o.getTransformationMatrix());
	}
	
	public void mouseMoved(int x, int y, int dx, int dy) {
	}
	
	public void mouseDown(int button) {
	}
	
	public void mouseUp(int button) {
	}
	
	public void scroll(int scrollDistance) {
	}

}
