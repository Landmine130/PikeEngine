package world;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import vecmath.Vector3f;

import misc.InputHandler;
import misc.InputObserver;
import misc.MathF;

public class PlayerCharacter extends Character implements InputObserver, WorldObjectMovementObserver {
	
	private ViewPoint viewPoint;
	
	private float movementSpeed = 2.5f;
	
	public PlayerCharacter(String modelName, ViewPoint viewPoint) {
		super(modelName);
		this.viewPoint = viewPoint;
		setup();
	}
	
	public PlayerCharacter(String modelName, Shader shader, ViewPoint viewPoint) {
		super(modelName, shader);
		this.viewPoint = viewPoint;
		setup();
	}
	
	public PlayerCharacter(Model model, Shader shader, ViewPoint viewPoint) {
		super(model, shader);
		this.viewPoint = viewPoint;
		setup();
	}
	
	private void setup() {
		viewPoint.addMovementObserver(this);
		InputHandler.addObserver(this);
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			accelerateZ(1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			accelerateZ(-1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
		accelerateX(1);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			accelerateX(-1);
		}
	}
	
	
	public ViewPoint getViewPoint() {
		return viewPoint;
	}
	
	public void setViewPoint(ViewPoint viewPoint) {
		this.viewPoint = viewPoint;
	}	
	
	public void setPosition(Vector3f position) {
		super.setPosition(position);
		viewPoint.setPosition(position);
	}
	
	public void worldObjectWillMove(WorldObject o, Vector3f newPosition) {
		
	}
	
	public void worldObjectDidMove(WorldObject o) {
		
	}
	
	public void worldObjectWillRotate(WorldObject o, Vector3f newOrientation) {
		if (newOrientation.x < -MathF.PI_OVER_2) {
			newOrientation.set(-MathF.PI_OVER_2, newOrientation.y, newOrientation.z);
		}
		if (newOrientation.x > MathF.PI_OVER_2) {
			newOrientation.set(MathF.PI_OVER_2, newOrientation.y, newOrientation.z);
		}
	}
	
	public void worldObjectDidRotate(WorldObject o) {
		
	}
	
	private void recalculateSpeed() {
		
		float xSpeed = 0;
		float zSpeed = 0;

		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)) {
			zSpeed = MathF.sin(-viewPoint.getOrientation().y + MathF.PI_OVER_2) * movementSpeed;
			xSpeed = MathF.cos(-viewPoint.getOrientation().y + MathF.PI_OVER_2) * movementSpeed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W)) {
			zSpeed = MathF.sin(-viewPoint.getOrientation().y - MathF.PI_OVER_2) * movementSpeed;
			xSpeed = MathF.cos(-viewPoint.getOrientation().y - MathF.PI_OVER_2) * movementSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			zSpeed += MathF.sin(-viewPoint.getOrientation().y) * movementSpeed;
			xSpeed += MathF.cos(-viewPoint.getOrientation().y) * movementSpeed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			zSpeed += MathF.sin(-viewPoint.getOrientation().y + MathF.PI) * movementSpeed;
			xSpeed += MathF.cos(-viewPoint.getOrientation().y + MathF.PI) * movementSpeed;
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
		viewPoint.rotate(new Vector3f(-MathF.toRadians(dy / 6.0f), MathF.toRadians(dx / 6.0f), 0));
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
