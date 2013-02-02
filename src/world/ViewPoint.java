package world;
import org.lwjgl.opengl.Display;

import misc.MathF;
import vecmath.Matrix4f;
import vecmath.Vector3f;


public class ViewPoint extends WorldObject {

	private float fieldOfView;
	private float nearViewDistance;
	private float farViewDistance;
	private Matrix4f projectionMatrix;
	
	public ViewPoint() {
		nearViewDistance = .1f;
		farViewDistance = 500.0f;
		setFieldOfView(MathF.toRadians(65f));
	}
	
	public float getFieldOfView() {
		return fieldOfView;
	}
	
	public void setFieldOfView(float FOV) {
		fieldOfView = FOV;
		updatePerspectiveMatrix();

	}
	
	public float getNearViewDistance() {
		return nearViewDistance;
	}
	
	public void setNearViewDistance(float distance) {
		nearViewDistance = distance;
		updatePerspectiveMatrix();

	}
	
	public float getFarViewDistance() {
		return farViewDistance;
	}
	
	public void setFarViewDistance(float distance) {
		farViewDistance = distance;
		updatePerspectiveMatrix();
	}
	
	public void updatePerspectiveMatrix() {
		float aspectRatio = Display.getWidth() / (float)Display.getHeight();
		CreateLeftHandedPerspective(fieldOfView, aspectRatio, nearViewDistance, farViewDistance);
	}
	
	public void update() {

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
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	private Vector3f X,Y,negativeZ; // the camera referential
	private float tang;
	private float sphereFactorX;
	private float sphereFactorY;
	
	public void setPosition(Vector3f position) {
		super.setPosition(position);
		calculateReferentials();
	}
	
	public void setOrientation(Vector3f orientation) {
		super.setOrientation(orientation);
		calculateReferentials();
	}
	
	private void calculateReferentials() {
		Matrix4f rotation = new Matrix4f();
		rotation.rotX(orientation.x);
		rotation.rotY(orientation.y);
		rotation.rotZ(orientation.z);
		Matrix4f translation = new Matrix4f(Vector3f.Z_UNIT_VECTOR);
		translation.mul(rotation, translation);
		negativeZ = rotation.translationVector();
		negativeZ.negate();
		negativeZ.normalize();
		translation = new Matrix4f(Vector3f.Y_UNIT_VECTOR);
		translation.mul(rotation, translation);
		Y = translation.translationVector();
		Y.normalize();
		translation = new Matrix4f(Vector3f.X_UNIT_VECTOR);
		translation.mul(rotation, translation);
		X = translation.translationVector();
		X.normalize();
		
	}
	
	private void CreateLeftHandedPerspective(float fov, float aspect, float zNear, float zFar) {
	    float f = 1.0f / MathF.tan(fov*0.5f);
		projectionMatrix = new Matrix4f(f / aspect,	0,0, 0,
							0, f, 0, 0,
							0, 0, (zFar + zNear) / (zFar - zNear), (-2 * zFar * zNear) / (zFar - zNear),
							0, 0, 1, 0);
		projectionMatrix.transpose();
		
		// compute width and height of the near and far plane sections
		tang = MathF.tan(fov);
		sphereFactorY = 1/MathF.cos(fov);
		// compute half of the the horizontal field of view and sphereFactorX
		float anglex = MathF.atan(tang*fov);
		sphereFactorX = 1/MathF.cos(anglex);
	}
	
	public boolean isSphereInView(float radius, Vector3f offset) {
		float d1,d2;
		float az,ax,ay,zz1,zz2;

		az = position.innerProduct(negativeZ);
		if (az > farViewDistance + radius || az < nearViewDistance-radius) {
			System.out.println("Z");
			return false;
		}

		ax = offset.innerProduct(X);
		zz1 = az * tang * fieldOfView;
		d1 = sphereFactorX * radius;
		if (ax > zz1+d1 || ax < -zz1-d1) {
			System.out.println("X");
			return false;
		}

		ay = offset.innerProduct(Y);
		zz2 = az * tang;
		d2 = sphereFactorY * radius;
		if (ay > zz2+d2 || ay < -zz2-d2) {
			System.out.println("Y");
			return false;
		}
		return true;
	}
}
