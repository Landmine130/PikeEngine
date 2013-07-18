package world;

import vecmath.Vector3d;
import vecmath.Vector4d;

public class Force {

	private volatile double duration;
	private volatile Vector3d vector = new Vector3d();
	private volatile Vector3d torque = new Vector3d();
	
	public Force(Vector3d force, double duration) {
		vector.set(force);
		this.duration = duration;
	}
	
	public Force(Vector4d force) {
		vector.set(force.x, force.y, force.z);
		duration = force.w;
	}
	
	public Force(Vector3d force, Vector3d offset, double duration) {
		vector.set(force.x, force.y, force.z);
		this.duration = duration;
		torque.cross(offset, this.vector);
	}
	
	public Force(Vector4d force, Vector3d offset) {
		vector.set(force.x, force.y, force.z);
		duration = force.w;
		torque.cross(offset, this.vector);
	}

	public Vector3d getVector() {
		return vector;
	}
	
	public Vector3d getTorque() {
		return torque;
	}
	
	public double getDuration() {
		return duration;
	}
	
	public double getTimeRemaining(double startTime) {
		return getTimeRemaining(startTime, World.getMainWorldSimulationTime());
	}
	
	public double getTimeRemaining(double startTime, double currentTime) {
		return duration - (currentTime - startTime);
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Force)) {
			return false;
		}
		Force f = (Force) o;
		return f == this || (f.duration == duration && f.vector.equals(vector));
	}
	
	public boolean equals(Force f) {
		
		return f == this || (f.duration == duration && f.vector.equals(vector));
	}
}
