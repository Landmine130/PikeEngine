package world;
import vecmath.Matrix4d;
import vecmath.Vector3d;

public class TargetedViewPoint extends ViewPoint implements WorldObjectMovementObserver {

	private WorldObject target;
	private float distanceFromTarget;
	
	public TargetedViewPoint(WorldObject target, float distanceFromTarget) {
		super();
		this.distanceFromTarget = distanceFromTarget;
		setTarget(target);
	}
	
	public void setTarget(WorldObject target) {
		
		this.target = target;
			
		if (target != null) {
			target.addMovementObserver(this);
			updatePosition();
		}
	}

	public void setDistanceFromTarget(float distanceFromTarget) {
		
		this.distanceFromTarget = distanceFromTarget;
		
		updatePosition();
	}

	public void rotate(Vector3d magnitude) {
		super.rotate(magnitude);
		
		updatePosition();
	}

	public void setOrientation(Vector3d orientation) {
		super.setOrientation(orientation);
		
		updatePosition();
	}

	// No effect when target is set
	public void move(Vector3d vector) {
		
		if (target == null) {
			super.move(vector);
		}
	}

	// No effect when target is set
	public void setPosition(Vector3d position) {
		
		if (target == null) {
			super.setPosition(position);
		}
	}

	private void updatePosition() {
		
		Matrix4d newLocation = new Matrix4d();
		
		double ox;
		double oy;
		double oz;
		
		synchronized (orientation) {
			ox = orientation.x;
			oy = orientation.y;
			oz = orientation.z;
		}
		
		newLocation.rotX(ox);
		newLocation.rotY(oy);
		newLocation.rotZ(oz);
		
		Vector3d negativeZ = new Vector3d();
		
		synchronized (negativeZ) {
			negativeZ.set(0, 0, distanceFromTarget);
		}
		negativeZ.negate();
		
		newLocation.translate(negativeZ);
		super.setPosition(newLocation.translationVector());
	}
	
	
	
	public void worldObjectWillMove(WorldObject o, Vector3d newPosition) {
		
	}

	public void worldObjectDidMove(WorldObject o) {
		updatePosition();
	}
	
	public void worldObjectWillRotate(WorldObject o, Vector3d newOrientation) {
		
	}
	
	public void worldObjectDidRotate(WorldObject o) {
		
	}
}
