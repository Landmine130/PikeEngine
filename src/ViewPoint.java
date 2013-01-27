import vecmath.Matrix4f;
import vecmath.Vector3f;


public class ViewPoint extends WorldObject {

	private float fieldOfView;
	private float nearViewDistance;
	private float farViewDistance;
		
	public ViewPoint() {
		fieldOfView = (float) Math.toRadians(65f);
		nearViewDistance = .1f;
		farViewDistance = 100.0f;
	}
	
	public float getFieldOfView() {
		return fieldOfView;
	}
	
	public void setFieldOfView(float FOV) {
		fieldOfView = FOV;
	}
	
	public float getNearViewDistance() {
		return nearViewDistance;
	}
	
	public void setNearViewDistance(float distance) {
		nearViewDistance = distance;
	}
	
	public float getFarViewDistance() {
		return farViewDistance;
	}
	
	public void setFarViewDistance(float distance) {
		farViewDistance = distance;
	}
	
	public void prepareToUpdate() {
		
	}
	
	public void update() {
		//move(new Vector3f(-0.03f, -.000f, 0.0f));
	}
	
	public Matrix4f getTransformationMatrix() {
		Matrix4f transformation = new Matrix4f();
		transformation.rotX(-orientation.x);
		transformation.rotY(-orientation.y);
		transformation.rotZ(-orientation.z);
		Vector3f negativePosition = new Vector3f(position);
		negativePosition.negate();
		transformation.translate(negativePosition);
		return transformation;
	}
}
