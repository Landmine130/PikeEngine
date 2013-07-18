package world;


import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import vecmath.Vector3d;

import misc.InputHandler;
import misc.InputObserver;

public class PlayerCharacter extends Character implements InputObserver, WorldObjectMovementObserver {
	
	private static final double PI_OVER_2 = Math.PI / 2;
	
	private ViewPoint viewPoint;
	
	private double movementSpeed = 2.5f;

	public PlayerCharacter(String modelName, ViewPoint viewPoint, World world) {
		super(modelName, world);
		this.viewPoint = viewPoint;
		setup();
	}
	
	public PlayerCharacter(String modelName, Shader shader, ViewPoint viewPoint, World world) {
		super(modelName, shader, world);
		this.viewPoint = viewPoint;
		setup();
	}
	
	public PlayerCharacter(Model model, Shader shader, ViewPoint viewPoint, World world) {
		super(model, shader, world);
		this.viewPoint = viewPoint;
		setup();
	}
	
	private void setup() {
		viewPoint.addMovementObserver(this);
		InputHandler.addObserver(this);
		recalculateSpeed();
	}
	
	
	public ViewPoint getViewPoint() {
		return viewPoint;
	}
	
	public void setViewPoint(ViewPoint viewPoint) {
		this.viewPoint = viewPoint;
	}
			
	public void setPosition(Vector3d position) {
		super.setPosition(position);
		viewPoint.setPosition(position);
	}
	
	public void worldObjectWillMove(WorldObject o, Vector3d newPosition) {
		
	}
	
	public void worldObjectDidMove(WorldObject o) {
		
	}
	
	public void worldObjectWillRotate(WorldObject o, Vector3d newOrientation) {
		if (newOrientation.x < -PI_OVER_2) {
			newOrientation.set(-PI_OVER_2, newOrientation.y, newOrientation.z);
		}
		if (newOrientation.x > PI_OVER_2) {
			newOrientation.set(PI_OVER_2, newOrientation.y, newOrientation.z);
		}
	}
	
	public void worldObjectDidRotate(WorldObject o) {
		
	}
	
	private void recalculateSpeed() {
		
		double xSpeed = 0;
		double zSpeed = 0;

		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)) {
			zSpeed = Math.sin(-viewPoint.getOrientation().y + PI_OVER_2) * movementSpeed;
			xSpeed = Math.cos(-viewPoint.getOrientation().y + PI_OVER_2) * movementSpeed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W)) {
			zSpeed = Math.sin(-viewPoint.getOrientation().y - PI_OVER_2) * movementSpeed;
			xSpeed = Math.cos(-viewPoint.getOrientation().y - PI_OVER_2) * movementSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			zSpeed += Math.sin(-viewPoint.getOrientation().y) * movementSpeed;
			xSpeed += Math.cos(-viewPoint.getOrientation().y) * movementSpeed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			zSpeed += Math.sin(-viewPoint.getOrientation().y + Math.PI) * movementSpeed;
			xSpeed += Math.cos(-viewPoint.getOrientation().y + Math.PI) * movementSpeed;
		}
		setXSpeed(xSpeed);
		setZSpeed(zSpeed);
	}
	
	public void keyUp(int key) {
		if (key == Keyboard.KEY_W || key == Keyboard.KEY_A || key == Keyboard.KEY_S || key == Keyboard.KEY_D) {
			recalculateSpeed();
		}
	}
	
	public void keyDown(int key) {
		if (key == Keyboard.KEY_W || key == Keyboard.KEY_A || key == Keyboard.KEY_S || key == Keyboard.KEY_D) {
			recalculateSpeed();
		}
	}
	
	public void mouseMoved(int x, int y, int dx, int dy) {
		viewPoint.rotate(new Vector3d(-Math.toRadians(dy / 6.0f), Math.toRadians(dx / 6.0f), 0));
		recalculateSpeed();
	}
	
	public void mouseDown(int button) {
		
	}
	
	public void mouseUp(int button) {
		
	}
	
	public void scroll(int scrollDistance) {
		
	}
	
	public void worldPaused(World w) {
		InputHandler.removeObserver(this);
	}
	
	public void worldResumed(World w) {
		InputHandler.addObserver(this);
	}
}
