package world;

import vecmath.Vector3d;

public class FixedEndPointForce extends Force {
	
	public final double stopTime;
	
	public FixedEndPointForce(Vector3d force, double stopTime) {
		super(force, 0);
		this.stopTime = stopTime;
	}
	
	public FixedEndPointForce(Vector3d force, Vector3d offset, double stopTime) {
		super(force, offset, 0);
		this.stopTime = stopTime;
	}
	
	public double getStopTime() {
		return stopTime;
	}
	
	public double getTimeRemaining(double startTime, double currentTime) {
		return stopTime - currentTime;
	}
}
