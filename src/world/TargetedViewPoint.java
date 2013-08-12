package world;
import vecmath.Matrix4d;
import vecmath.Quat4d;
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

	public void rotateExtrinsic(Vector3d magnitude) {
		super.rotateExtrinsic(magnitude);
		
		updatePosition();
	}
	
	public void rotateIntrinsic(Vector3d magnitude) {
		super.rotateIntrinsic(magnitude);
		
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
		
		synchronized (orientation) {
			newLocation.setRotation(orientation);
		}
		
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
	
	public void worldObjectWillRotate(WorldObject o, Quat4d newOrientation) {
		
	}
	
	public void worldObjectDidRotate(WorldObject o) {
		
	}
}
