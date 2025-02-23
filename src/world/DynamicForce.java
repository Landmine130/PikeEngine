package world;

import vecmath.Vector3d;

public interface DynamicForce {
		
	public Vector3d forceForTime(double time, PhysicsObject object);
	public Vector3d offsetForTime(double time, PhysicsObject object);
}
