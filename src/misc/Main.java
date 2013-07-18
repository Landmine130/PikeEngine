package misc;

import world.terrain.*;
import world.*;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import vecmath.Vector3d;
import vecmath.Vector4d;
import vecmath.Vector3i;

public class Main implements InputObserver {
		
	private static World world;
	private static VisibleObject o;
	private static VisibleObject p;
	private static Timer t;
	
	private static PhysicsObject physicsObject;
	
	public static void main(String[] args) {
		
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Robots");
		// To change app icon on Mac, use -Xdock:icon option
		
		Display.setResizable(true);
		
		world = new World();
		world.setSkybox(new Skybox());
/*		terrain.get(new Vector3i(0,0,0));
		terrain.get(new Vector3i(0,0,-1));
		terrain.load(new Vector3i(-1,0,0));
		terrain.load(new Vector3i(-1,0,-1));*/
		
		try {
			Md5ToModeldata.convertAnimation("Resources/Models/Animaitons/boblampclean.md5anim", "Resources/Models/Animaitons/boblampclean.animationdata");
			Md5ToModeldata.convertMesh("Resources/Models/boblampclean.md5mesh", "boblampclean");
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		/*RiggedObject riggedObject = new RiggedObject("boblampclean");
		riggedObject.setPosition(new Vector3d(0,0.5,80));
		riggedObject.getAnimation().start();
		world.addDrawable(riggedObject);*/
		
		physicsObject = new PhysicsObject("banana");
		world.addDrawable(physicsObject);
		world.addObserver(physicsObject);
		
		
		
		PlayerCharacter player = new PlayerCharacter("", world.getViewPoint(), world);
		player.setPosition(new Vector3d(0,0,-5));
		//o = new VisibleObject("cube");
		//o.setPosition(new Vector3f(1f,0,30f));
		//world.setViewPoint(new TargetedViewPoint(o, 10));
		//p = new VisibleObject("cube");
		//p.setPosition(new Vector3f(.5f,10f,-50));
		Main mainObject = new Main();
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
		switch (key) {
			case Keyboard.KEY_ESCAPE:
				world.setPaused(!world.isPaused());
			break;
			case Keyboard.KEY_P:
				try {
					Display.setFullscreen(true);
				}
				catch (LWJGLException e) {
					System.err.println("Error: Failed to enter fullscreen");
				}
				Force force = new Force(new Vector4d(0,10,0,.1), new Vector3d(.1,0,.1));
				physicsObject.addForce(force);
			break;
			case Keyboard.KEY_F:
				if (t == null || !t.isRunning()) {
					t = new Timer(1, -1, this, "printFPS");
					printFPS(t);
					t.start();
				}
				else {
					t.stop();
				}
				break;
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
