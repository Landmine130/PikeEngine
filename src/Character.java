import vecmath.Vector3f;


public class Character extends CollisionObject {
	
	private float xSpeed;
	private float zSpeed;
	
	public float getXSpeed() {
		return xSpeed;
	}
	
	public float getZSpeed() {
		return zSpeed;
	}
	
	public void setXSpeed(float speed) {
		
	}
	
	public void setZSpeed(float speed) {
		
	}
	
	public void prepareToUpdate(double timeElapsed) {
		move(new Vector3f((float)(timeElapsed * xSpeed), 0 , (float)(timeElapsed * zSpeed)));
	}
	
	public void update() {
		
	}
}
