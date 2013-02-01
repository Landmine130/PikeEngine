package world;

import java.util.TreeSet;

import vecmath.Vector3f;

public class CollisionObject extends WorldObject {
	
	private TreeSet<CollisionObject> collidableObjects = new TreeSet<CollisionObject>();
	private TreeSet<CollisionObserver> collisionObservers = new TreeSet<CollisionObserver>();;
	private float collisionRadius;
	
	
	public CollisionObject() {
		
		collisionRadius = 0;
	}

	public CollisionObject(float collisionRadius) {

		this.collisionRadius = collisionRadius;
	}

	
	public void addCollidableObject(CollisionObject o) {
		collidableObjects.add(o);
	}

	public void removeCollidableObject(CollisionObject o) {
		collidableObjects.remove(o);
	}

	public void addCollisionObserver(CollisionObserver o) {
		collisionObservers.add(o);
	}

	public void removeCollisionObserver(CollisionObserver o) {
		collisionObservers.remove(o);
	}

	private boolean checkCollision(Vector3f newPosition) {
		
		boolean collisionOccured = false;
		
		for (CollisionObject o : collidableObjects) {
						
			if (o.collisionRadius + collisionRadius < getPosition().distance(o.getPosition())) {
				
				notifyObserversOfCollision(o, newPosition);
				o.notifyObserversOfCollision(this, o.getPosition());
				collisionOccured = true;
			}
		}
		return collisionOccured;
	}

	private void notifyObserversOfCollision(CollisionObject o, Vector3f newPosition) {
		
		super.move(collision(o, getPosition(), newPosition));
		
		for (CollisionObserver observer : collisionObservers) {
			observer.collision(this, o);
		}
	}

	public void setPosition(Vector3f position) {
				
		if (!getPosition().equals(position) && !checkCollision(position)) {
			super.setPosition(position);	
		}
	}

	/**
	 * Called upon collision with another CollisionObject
	 * @param o The CollisionObject that this object collided with
	 * @param oldPosition The position of this object before this object moved
	 * @param newPosition The position of this object that this object was moving to
	 * @return The new position of this object relative to oldPosition
	 */
	public Vector3f collision(CollisionObject o, Vector3f oldPosition, Vector3f newPosition) {

		Vector3f movement = new Vector3f();
		movement.sub(newPosition, oldPosition);
		movement.normalize();
		movement.scale(getPosition().distance(o.getPosition()) - o.collisionRadius - collisionRadius);
		return movement;
	}

}
