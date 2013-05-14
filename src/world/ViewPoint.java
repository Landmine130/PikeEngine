package world;
import org.lwjgl.opengl.Display;

import misc.MathF;
import vecmath.Matrix4d;
import vecmath.Matrix4f;
import vecmath.Vector3d;
import vecmath.Vector3f;


public class ViewPoint extends WorldObject {

	private volatile float fieldOfView;
	private volatile float nearViewDistance;
	private volatile float farViewDistance;
	private volatile Matrix4f projectionMatrix;
	
	public ViewPoint() {
		nearViewDistance = .1f;
		farViewDistance = 100.0f;
		fieldOfView = MathF.toRadians(65f);
		updatePerspectiveMatrix();
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
	
	public Matrix4d getTransformationMatrix() {
		
		Matrix4d transformation = new Matrix4d();
		
		double ox;
		double oy;
		double oz;
		
		synchronized (orientation) {
			ox = orientation.x;
			oy = orientation.y;
			oz = orientation.z;
		}
		
		transformation.rotX(-ox);
		transformation.rotY(-oy);
		transformation.rotZ(-oz);
		
		Vector3d negativePosition = new Vector3d();
		
		synchronized (position) {
			negativePosition.set(position);
		}
		negativePosition.negate();
		
		transformation.translate(negativePosition);
		
		return transformation;
	}
	
	public Matrix4f getProjectionMatrix() {
		return new Matrix4f(projectionMatrix);
	}
	
	private volatile float tang;
	private volatile float sphereFactorX;
	private volatile float sphereFactorY;
	private volatile float aspectRatio;
	
	private void CreateLeftHandedPerspective(float fov, float aspect, float zNear, float zFar) {
		aspectRatio = aspect;
		fov *= .5f;
		tang = MathF.tan(fov);
	    float f = 1.0f / tang;
		projectionMatrix = new Matrix4f(f / aspect,	0,0, 0,
							0, f, 0, 0,
							0, 0, (zFar + zNear) / (zFar - zNear), 1,
							0, 0, (-2 * zFar * zNear) / (zFar - zNear), 0);
		
		// compute width and height of the near and far plane sections
		sphereFactorY = 1/MathF.cos(fov);
		// compute half of the the horizontal field of view and sphereFactorX
		float anglex = MathF.atan(tang*aspect);
		sphereFactorX = 1/MathF.cos(anglex);
	}
	
	public boolean isSphereInView(float radius, Vector3f offset) {
		float d1,d2;
		float az,ax,ay,zz1,zz2;

		az = offset.innerProduct(Vector3f.Z_UNIT_VECTOR);
		if (az > farViewDistance + radius || az < nearViewDistance-radius) {
			return false;
		}
		
		ax = offset.innerProduct(Vector3f.X_UNIT_VECTOR);
		zz1 = az * tang * aspectRatio;
		d1 = sphereFactorX * radius;
		if (ax > zz1+d1 || ax < -zz1-d1) {
			return false;
		}
		
		ay = offset.innerProduct(Vector3f.Y_UNIT_VECTOR);
		zz2 = az * tang;
		d2 = sphereFactorY * radius;
		if (ay > zz2+d2 || ay < -zz2-d2) {
			return false;
		}
		
		return true;
	}
}
