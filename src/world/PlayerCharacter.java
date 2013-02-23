package world;


import java.util.HashSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import vecmath.Vector3f;
import vecmath.Vector3i;
import world.terrain.Terrain;

import misc.InputHandler;
import misc.InputObserver;
import misc.MathF;

public class PlayerCharacter extends Character implements InputObserver, WorldObjectMovementObserver {
	
	private ViewPoint viewPoint;
	
	private float movementSpeed = 2.5f;
	private int viewDistance = 50;
	private HashSet<Vector3i> loadedObjects = new HashSet<Vector3i>();
	private HashSet<Vector3i> accessedPositions = new HashSet<Vector3i>();

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
		loadTerrain(getPosition().toVector3i());
	}
	
	
	public ViewPoint getViewPoint() {
		return viewPoint;
	}
	
	public void setViewPoint(ViewPoint viewPoint) {
		this.viewPoint = viewPoint;
	}
	
	private Vector3i oldPosition;
		
	public void setPosition(Vector3f position) {
		super.setPosition(position);
		viewPoint.setPosition(position);
		
		Vector3i intPosition = getPosition().toVector3i();
		if (!intPosition.equals(oldPosition)) {
			oldPosition = intPosition;
			loadTerrain(intPosition);
		}
	}
	
	private void loadTerrain(Vector3i position) {

		Vector3i positionCopy = new Vector3i(position);
		
		int xMax = positionCopy.x + viewDistance;
		int zMax; //= positionCopy.z + viewDistance;
		
		int xMin = positionCopy.x - viewDistance;
		int zMin;// = positionCopy.z - viewDistance;
		
		World w = getWorld();
		Terrain t = w.getTerrain();
		
		positionCopy.y = 0;
		int zDistance;
		int radiusSquared = viewDistance * viewDistance;
		
		for (positionCopy.x = xMin; positionCopy.x <= xMax; positionCopy.x++) {
			zDistance = (int)MathF.sqrt(radiusSquared - (positionCopy.x - position.x) * (positionCopy.x - position.x));
			zMin = position.z - zDistance;
			zMax = position.z + zDistance;
			for (positionCopy.z = zMin; positionCopy.z <= zMax; positionCopy.z++) {
				Vector3i positionCopyCopy = new Vector3i(positionCopy);
				accessedPositions.add(positionCopyCopy);
				if (!loadedObjects.contains(positionCopyCopy)) {
					VisibleObject o = t.get(positionCopyCopy);
					if (o != null) {
						w.addDrawable(o);
						loadedObjects.add(positionCopyCopy);
					}
				}
			}
		}
		loadedObjects.removeAll(accessedPositions);
		
		for (Vector3i v : loadedObjects) {
			if (t.isLoaded(v)) {
				w.removeDrawable(t.get(v));
				t.unload(v);
			}
		}
		
		loadedObjects.clear();
		HashSet<Vector3i> swap = loadedObjects;
		loadedObjects = accessedPositions;
		accessedPositions = swap;
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
