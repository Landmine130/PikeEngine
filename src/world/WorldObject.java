package world;

import vecmath.Vector3f;
import vecmath.Matrix4f;
import vecmath.Vector3i;

import java.util.LinkedHashSet;

public class WorldObject {
	
	protected Vector3f position;
	protected Vector3f orientation;
	
	private LinkedHashSet<WorldObjectMovementObserver> observers = new LinkedHashSet<WorldObjectMovementObserver>();
	
	public WorldObject() {
		position = new Vector3f();
		orientation = new Vector3f();
	}
	
	public void move(Vector3f distance) {
		
		Vector3f newPosition = new Vector3f(position);
		newPosition.add(distance);
		
		setPosition(newPosition);
		}
	
	public Vector3f getPosition() {
		return new Vector3f(position);
	}
	
	public void setPosition(Vector3f position) {
		
		Vector3f tmp = new Vector3f(position);
		notifyObserversWillMove(tmp);

		this.position.set(tmp);
		
		notifyObserversDidMove();
	}
	
	public void setPosition(Vector3i position) {
		
		Vector3f tmp = new Vector3f(position);
		notifyObserversWillMove(tmp);

		this.position.set(tmp);
		
		notifyObserversDidMove();
	}
	
	public void rotate(Vector3f rotation) {

		Vector3f newOrientation = new Vector3f(orientation);
		newOrientation.add(rotation);
		
		setOrientation(newOrientation);
	}
	
	public Vector3f getOrientation() {
		return new Vector3f(orientation);
	}
	
	public void setOrientation(Vector3f orientation) {
		
		Vector3f tmp = new Vector3f(orientation);
		notifyObserversWillRotate(tmp);

		this.orientation.set(tmp);
		
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
