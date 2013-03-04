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
		
		Vector3f newPosition = new Vector3f(distance);
		
		synchronized (position) {
			newPosition.add(position);
		}
		
		setPosition(newPosition);
	}
	
	public Vector3f getPosition() {
		Vector3f ret = new Vector3f();
		synchronized (position) {
			ret.set(position);
		}
		return ret;
	}
	
	public void setPosition(Vector3f position) {
		
		Vector3f tmp = new Vector3f(position);
		notifyObserversWillMove(tmp);
		
		synchronized (this.position) {
			this.position.set(tmp);
		}
		
		notifyObserversDidMove();
	}
	
	public void setPosition(Vector3i position) {
		
		Vector3f tmp = new Vector3f(position);
		notifyObserversWillMove(tmp);
		
		synchronized (this.position) {
			this.position.set(tmp);
		}
		
		notifyObserversDidMove();
	}
	
	public void rotate(Vector3f rotation) {

		Vector3f newOrientation = new Vector3f(rotation);
		
		synchronized (orientation) {
			newOrientation.add(orientation);
		}
		
		setOrientation(newOrientation);
	}
	
	public Vector3f getOrientation() {
		Vector3f ret = new Vector3f();
		synchronized (orientation) {
			ret.set(orientation);
		}
		return ret;
	}
	
	public void setOrientation(Vector3f orientation) {
		
		Vector3f tmp = new Vector3f(orientation);
		notifyObserversWillRotate(tmp);
		
		synchronized (this.orientation) {
			this.orientation.set(tmp);
		}
		
		notifyObserversDidRotate();
	}
	
	public void addMovementObserver(WorldObjectMovementObserver o) {
		synchronized (observers) {
			observers.add(o);
		}
	}
	
	public void removeMovementObserver(WorldObjectMovementObserver o) {
		synchronized (observers) {
			observers.remove(o);
		}
	}
	
	private void notifyObserversWillMove(Vector3f newPosition) {
		
		LinkedHashSet<WorldObjectMovementObserver> temp = new LinkedHashSet<WorldObjectMovementObserver>();
		synchronized (observers) {
			temp.addAll(observers);
		}
		
		for (WorldObjectMovementObserver o : temp) {
			o.worldObjectWillMove(this, newPosition);
		}
	}
	
	private void notifyObserversDidMove() {
		
		LinkedHashSet<WorldObjectMovementObserver> temp = new LinkedHashSet<WorldObjectMovementObserver>();
		synchronized (observers) {
			temp.addAll(observers);
		}
		
		for (WorldObjectMovementObserver o : temp) {
			o.worldObjectDidMove(this);
		}
	}
	
	private void notifyObserversWillRotate(Vector3f newOrientation) {
		
		LinkedHashSet<WorldObjectMovementObserver> temp = new LinkedHashSet<WorldObjectMovementObserver>();
		synchronized (observers) {
			temp.addAll(observers);
		}
		
		for (WorldObjectMovementObserver o : temp) {
			o.worldObjectWillRotate(this, newOrientation);
		}
	}
		
	private void notifyObserversDidRotate() {
		
		LinkedHashSet<WorldObjectMovementObserver> temp = new LinkedHashSet<WorldObjectMovementObserver>();
		synchronized (observers) {
			temp.addAll(observers);
		}
		
		for (WorldObjectMovementObserver o : temp) {
			o.worldObjectDidRotate(this);
		}
	}
	
	public Matrix4f getTransformationMatrix() {
		
		Matrix4f transformation = new Matrix4f();
		
		synchronized (position) {
			transformation.set(position);
		}
		
		float ox;
		float oy;
		float oz;
		
		synchronized (orientation) {
			ox = orientation.x;
			oy = orientation.y;
			oz = orientation.z;
		}
		
		transformation.rotX(ox);
		transformation.rotY(oy);
		transformation.rotZ(oz);
		
		return transformation;
	}
}
