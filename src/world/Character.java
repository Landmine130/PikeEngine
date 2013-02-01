package world;
import vecmath.Vector3f;


public class Character extends VisibleObject {
	
	private float xSpeed;
	private float zSpeed;
	
	
	public Character(String modelName) {
		super(modelName);
	}
	
	public Character(String modelName, Shader shader) {
		super(modelName, shader);
	}
	
	public Character(Model model, Shader shader) {
		super(model, shader);
	}
	
	
	public float getXSpeed() {
		return xSpeed;
	}
	
	public float getZSpeed() {
		return zSpeed;
	}
	
	public void setXSpeed(float speed) {
		xSpeed = speed;
	}
	
	public void setZSpeed(float speed) {
		zSpeed = speed;
	}
	
	public void accelerateX(float magnitude) {
		xSpeed += magnitude;
	}
	
	public void accelerateZ(float magnitude) {
		zSpeed += magnitude;
	}
	
	public void prepareToUpdate(double timeElapsed) {
		move(new Vector3f((float)(timeElapsed * xSpeed), 0 , (float)(timeElapsed * zSpeed)));
	}
	
	public void update() {
		
	}
}
