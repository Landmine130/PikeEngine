package world;

import vecmath.Quat4d;
import vecmath.Vector3d;
import vecmath.Matrix4d;
import vecmath.Vector3i;

import java.util.LinkedHashSet;

public class WorldObject {
	
	protected Vector3d position;
	protected Quat4d orientation;
	
	private LinkedHashSet<WorldObjectMovementObserver> observers = new LinkedHashSet<WorldObjectMovementObserver>();
	
	public WorldObject() {
		position = new Vector3d();
		orientation = new Quat4d();
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
	
	public void rotate(Quat4d rotation) {

		Quat4d newOrientation = new Quat4d(rotation);
		
		synchronized (orientation) {
			newOrientation.mul(orientation);
		}
		
		setOrientation(newOrientation);
	}
	
	public void rotateExtrinsic(Vector3d rotation) {

		Quat4d newOrientation = new Quat4d();
		newOrientation.set(rotation);
		
		synchronized (orientation) {
			newOrientation.mul(orientation);
		}
		
		setOrientation(newOrientation);
	}
	
	public void rotateIntrinsic(Vector3d rotation) {

		Quat4d newOrientation = new Quat4d();
		newOrientation.set(rotation);
		
		synchronized (orientation) {
			newOrientation.mul(orientation, newOrientation);
		}
		
		setOrientation(newOrientation);
	}
	
	public Quat4d getOrientation() {
		Quat4d ret = new Quat4d();
		synchronized (orientation) {
			ret.set(orientation);
		}
		return ret;
	}
	
	public Vector3d getEulerOrientation() {
		Vector3d ret = new Vector3d();
		synchronized (orientation) {
			ret.set(orientation);
		}
		return ret;
	}
	
	public void setOrientation(Quat4d orientation) {
		
		Quat4d tmp = new Quat4d(orientation);
		notifyObserversWillRotate(tmp);
		
		synchronized (this.orientation) {
			this.orientation.set(tmp);
		}
		
		notifyObserversDidRotate();
	}
	
	public void setOrientation(Vector3d orientation) {
		
		Quat4d tmp = new Quat4d();
		tmp.set(orientation);
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
	
	private void notifyObserversWillRotate(Quat4d newOrientation) {
		
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
		
		synchronized (orientation) {
			transformation.setRotation(orientation);
		}
		
		return transformation;
	}
}
