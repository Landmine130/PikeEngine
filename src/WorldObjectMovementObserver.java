import vecmath.Vector3f;


public interface WorldObjectMovementObserver {
	
	public void worldObjectWillMove(WorldObject o, Vector3f newPosition);
	public void worldObjectDidMove(WorldObject o);
	public void worldObjectWillRotate(WorldObject o, Vector3f newOrientation);
	public void worldObjectDidRotate(WorldObject o);
	
}
