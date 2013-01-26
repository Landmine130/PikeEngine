
import vecmath.Vector3f;
import vecmath.Matrix4f;
import java.util.ArrayList;

public class WorldObject {
	
	protected Vector3f position;
	protected Vector3f orientation;
	
	private ArrayList<WorldObjectMovementObserver> observers;
	
	public WorldObject() {
		position = new Vector3f();
		orientation = new Vector3f();
		observers = new ArrayList<WorldObjectMovementObserver>();
	}
	
	public void prepareToUpdate() {
		
	}
	
	public void update() {
		
	}
	
	public void move(Vector3f distance) {
		
		Vector3f newPosition = new Vector3f(position);
		newPosition.add(distance);
		
		notifyObserversWillMove(newPosition);

		position.set(newPosition);
		
		notifyObserversDidMove();
	}
	
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	
	public void setPosition(Vector3f position) {
		
		notifyObserversWillMove(position);

		this.position.set(position);
		
		notifyObserversDidMove();
	}
	
	public void rotate(Vector3f rotation) {

		Vector3f newOrientation = new Vector3f(orientation);
		newOrientation.add(rotation);
		
		notifyObserversWillRotate(newOrientation);

		orientation.set(newOrientation);
		
		notifyObserversDidRotate();
	}
	
	public Vector3f getOrientation() {
		return new Vector3f(orientation);
	}
	
	public void setOrientation(Vector3f orientation) {
		
		notifyObserversWillRotate(orientation);

		this.orientation.set(orientation);
		
		notifyObserversDidRotate();
	}
	
	public void addMovementObserver(WorldObjectMovementObserver o) {
		observers.add(o);
	}
	
	public void removeMovementObserver(WorldObjectMovementObserver o) {
		observers.remove(o);
	}
	
	private void notifyObserversWillMove(Vector3f newPosition) {
		for (WorldObjectMovementObserver o : observers) {
			o.worldObjectWillMove(this, newPosition);
		}
	}
	
	private void notifyObserversDidMove() {
		for (WorldObjectMovementObserver o : observers) {
			o.worldObjectDidMove(this);
		}
	}
	
	private void notifyObserversWillRotate(Vector3f newOrientation) {
		for (WorldObjectMovementObserver o : observers) {
			o.worldObjectWillRotate(this, newOrientation);
		}
	}
	
	private void notifyObserversDidRotate() {
		for (WorldObjectMovementObserver o : observers) {
			o.worldObjectDidRotate(this);
		}
	}
	public Matrix4f getTransformationMatrix() {
		Matrix4f transformation = new Matrix4f(position);
		transformation.rotX(orientation.x);
		transformation.rotY(orientation.y);
		transformation.rotZ(orientation.z);
		return transformation;
	}
}
