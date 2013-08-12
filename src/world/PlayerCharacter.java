package world;


import java.util.HashSet;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import vecmath.Quat4d;
import vecmath.Vector3d;
import vecmath.Vector3f;
import vecmath.Vector3i;
import world.terrain.Terrain;

import misc.InputHandler;
import misc.InputObserver;
import misc.MathF;

public class PlayerCharacter extends Character implements InputObserver, WorldObjectMovementObserver {
	
	private static final double PI_OVER_2 = Math.PI / 2;
	
	private ViewPoint viewPoint;
	
	private double movementSpeed = 2.5f;
	private int viewDistance = 50;
	private volatile HashSet<Vector3i> loadedObjects = new HashSet<Vector3i>();
	private volatile HashSet<Vector3i> accessedPositions = new HashSet<Vector3i>();
	private Vector3i oldPosition;
	Terrain terrain = new Terrain(System.nanoTime());
	
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
			
	/*public void setPosition(Vector3d position) {
		super.setPosition(position);
		viewPoint.setPosition(position);
	}*/
	
	public void setPosition(Vector3d position) {
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
		
		positionCopy.y = 0;
		int zDistance;
		int radiusSquared = viewDistance * viewDistance;
		
		for (positionCopy.x = xMin; positionCopy.x <= xMax; positionCopy.x++) {
			zDistance = (int)MathF.sqrt(radiusSquared - (positionCopy.x - position.x) * (positionCopy.x - position.x));
			zMin = position.z - zDistance;
			zMax = position.z + zDistance;
			for (positionCopy.z = zMin; positionCopy.z <= zMax; positionCopy.z++) {
				Vector3i positionCopyCopy = new Vector3i(positionCopy);
				synchronized (accessedPositions) {
					accessedPositions.add(positionCopyCopy);
				}
				if (!loadedObjects.contains(positionCopyCopy)) {
					VisibleObject o = terrain.get(positionCopyCopy);
					if (o != null) {
						getWorld().addDrawable(o);
						loadedObjects.add(positionCopyCopy);
					}
				}
			}
		}
		HashSet<Vector3i> accessedPositionsCopy;
		synchronized (accessedPositions) {
			accessedPositionsCopy = new HashSet<Vector3i>(accessedPositions);
		}
		
		synchronized (loadedObjects) {
			loadedObjects.removeAll(accessedPositionsCopy);
				
			for (Vector3i v : loadedObjects) {
				if (terrain.isLoaded(v)) {
					getWorld().removeDrawable(terrain.get(v));
					terrain.unload(v);
				}
			}
			
			loadedObjects.clear();
		}
		HashSet<Vector3i> swap = loadedObjects;
		loadedObjects = accessedPositions;
		accessedPositions = swap;
	}
	
	public void worldObjectWillMove(WorldObject o, Vector3d newPosition) {
		
	}
	
	public void worldObjectDidMove(WorldObject o) {
		
	}
	
	public void worldObjectWillRotate(WorldObject o, Quat4d newOrientation) {
		
		Vector3d newEulerOrientation = new Vector3d();
		newEulerOrientation.set(newOrientation);
		
		if (newEulerOrientation.z < -PI_OVER_2) {
			newOrientation.set(new Vector3d(-PI_OVER_2, newEulerOrientation.y, 0));
		}
		else if (newEulerOrientation.z > PI_OVER_2) {
			newOrientation.set(new Vector3d(PI_OVER_2, newEulerOrientation.y, 0));
		}
	}
	
	public void worldObjectDidRotate(WorldObject o) {
		
	}
	
	private void recalculateSpeed() {
		
		double xSpeed = 0;
		double zSpeed = 0;
		
		Vector3d eulerOrientation = viewPoint.getEulerOrientation();

		if (Keyboard.isKeyDown(Keyboard.KEY_W) && !Keyboard.isKeyDown(Keyboard.KEY_S)) {
			zSpeed = Math.sin(-eulerOrientation.y + PI_OVER_2) * movementSpeed;
			xSpeed = Math.cos(-eulerOrientation.y + PI_OVER_2) * movementSpeed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_S) && !Keyboard.isKeyDown(Keyboard.KEY_W)) {
			zSpeed = Math.sin(-eulerOrientation.y - PI_OVER_2) * movementSpeed;
			xSpeed = Math.cos(-eulerOrientation.y - PI_OVER_2) * movementSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A)) {
			zSpeed += Math.sin(-eulerOrientation.y) * movementSpeed;
			xSpeed += Math.cos(-eulerOrientation.y) * movementSpeed;
		}
		else if (Keyboard.isKeyDown(Keyboard.KEY_A) && !Keyboard.isKeyDown(Keyboard.KEY_D)) {
			zSpeed += Math.sin(-eulerOrientation.y + Math.PI) * movementSpeed;
			xSpeed += Math.cos(-eulerOrientation.y + Math.PI) * movementSpeed;
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
		viewPoint.rotateExtrinsic(new Vector3d(0, Math.toRadians(dx / 6.0f), 0));
		viewPoint.rotateIntrinsic(new Vector3d(-Math.toRadians(dy / 6.0f), 0, 0));
		setOrientation(viewPoint.getOrientation());
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
