package world;

import vecmath.Vector3d;
import vecmath.Matrix4d;
import vecmath.Vector3i;

import java.util.LinkedHashSet;

public class WorldObject {
	
	protected Vector3d position;
	protected Vector3d orientation;
	
	private LinkedHashSet<WorldObjectMovementObserver> observers = new LinkedHashSet<WorldObjectMovementObserver>();
	
	public WorldObject() {
		position = new Vector3d();
		orientation = new Vector3d();
	}
	
	public void move(Vector3d distance) {
		
		Vector3d newPosition = new Vector3d(distance);
		
		synchronized (position) {
			newPosition.add(position);
		}
		
		setPosition(newPosition);
	}
	
	public Vector3d getPosition() {
		Vector3d ret = new Vector3d();
		synchronized (position) {
			ret.set(position);
		}
		return ret;
	}
	
	public void setPosition(Vector3d position) {
		
		Vector3d tmp = new Vector3d(position);
		notifyObserversWillMove(tmp);
		
		synchronized (this.position) {
			this.position.set(tmp);
		}
		
		notifyObserversDidMove();
	}
	
	public void setPosition(Vector3i position) {
		
		Vector3d tmp = new Vector3d(position);
		notifyObserversWillMove(tmp);
		
		synchronized (this.position) {
			this.position.set(tmp);
		}
		
		notifyObserversDidMove();
	}
	
	public void rotate(Vector3d rotation) {

		Vector3d newOrientation = new Vector3d(rotation);
		
		synchronized (orientation) {
			newOrientation.add(orientation);
		}
		
		setOrientation(newOrientation);
	}
	
	public Vector3d getOrientation() {
		Vector3d ret = new Vector3d();
		synchronized (orientation) {
			ret.set(orientation);
		}
		return ret;
	}
	
	public void setOrientation(Vector3d orientation) {
		
		Vector3d tmp = new Vector3d(orientation);
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
	
	private void notifyObserversWillMove(Vector3d newPosition) {
		
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
	
	private void notifyObserversWillRotate(Vector3d newOrientation) {
		
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
	
	public Matrix4d getTransformationMatrix() {
		
		Matrix4d transformation = new Matrix4d();
		
		synchronized (position) {
			transformation.set(position);
		}
		
		double ox;
		double oy;
		double oz;
		
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
