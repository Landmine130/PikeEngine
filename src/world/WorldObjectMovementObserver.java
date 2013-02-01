package world;
import vecmath.Vector3f;


public interface WorldObjectMovementObserver {
	
	/**
	 * Called on all observers of an object just before it moves.
	 * @param o The moving object
	 * @param newPosition The position that the object will move to. Note that modifying newPosition will affect the new position of o.
	 */
	public void worldObjectWillMove(WorldObject o, Vector3f newPosition);
	public void worldObjectDidMove(WorldObject o);
	/**
	 * Called on all observers of an object just before it rotates.
	 * @param o The rotating object
	 * @param newOrientation The orientation that the object will rotate to. Note that modifying newOrientation will affect the new orientation of o.
	 */
	public void worldObjectWillRotate(WorldObject o, Vector3f newOrientation);
	public void worldObjectDidRotate(WorldObject o);
	
}
