package world;
import vecmath.Matrix4f;
import vecmath.Vector3f;


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

	public void rotate(Vector3f magnitude) {
		super.rotate(magnitude);
		
		updatePosition();
	}

	public void setOrientation(Vector3f orientation) {
		super.setOrientation(orientation);
		
		updatePosition();
	}

	// No effect when target is set
	public void move(Vector3f vector) {
		
		if (target == null) {
			super.move(vector);
		}
	}

	// No effect when target is set
	public void setPosition(Vector3f position) {
		
		if (target == null) {
			super.setPosition(position);
		}
	}

	private void updatePosition() {
		
		Matrix4f newLocation = new Matrix4f(target.getPosition());
		
		newLocation.rotX(orientation.x);
		newLocation.rotY(orientation.y);
		newLocation.rotZ(orientation.z);
		
		newLocation.translate(new Vector3f(0, 0, -distanceFromTarget));

		
		super.setPosition(newLocation.translationVector());
	}
	
	
	
	public void worldObjectWillMove(WorldObject o, Vector3f newPosition) {
		
	}

	public void worldObjectDidMove(WorldObject o) {
		updatePosition();
	}
	
	public void worldObjectWillRotate(WorldObject o, Vector3f newOrientation) {
		
	}
	
	public void worldObjectDidRotate(WorldObject o) {
		
	}
}
