package world;
import misc.MathF;
import vecmath.Vector3f;


public class Character extends VisibleObject implements WorldUpdateObserver {
	
	private volatile float xSpeed;
	private volatile float zSpeed;
	private volatile float speed;
	private volatile float direction;
	private volatile World world;
		
	public Character(String modelName, World world) {
		super(modelName);
		this.world = world;
		world.addObserver(this);
	}
	
	public Character(String modelName, Shader shader, World world) {
		super(modelName, shader);
		this.world = world;
		world.addObserver(this);
	}
	
	public Character(Model model, Shader shader, World world) {
		super(model, shader);
		this.world = world;
		world.addObserver(this);
	}
	
	public float getXSpeed() {
		return xSpeed;
	}
	
	public float getZSpeed() {
		return zSpeed;
	}
	
	public void setXSpeed(float speed) {
		xSpeed = speed;
		recalculateVector();
	}
	
	public void setZSpeed(float speed) {
		zSpeed = speed;
		recalculateVector();
	}
	
	public void accelerateX(float magnitude) {
		xSpeed += magnitude;
		recalculateVector();
	}
	
	public void accelerateZ(float magnitude) {
		zSpeed += magnitude;
		recalculateVector();
	}
	
	private void recalculateVector() {
		direction = MathF.atan2(zSpeed, xSpeed) - MathF.PI_OVER_2;
		speed = MathF.sqrt(xSpeed * xSpeed + zSpeed * zSpeed);
	}
	
	private void recalculateComponents() {
		xSpeed = speed * MathF.cos(direction + MathF.PI_OVER_2);
		zSpeed = speed * MathF.sin(direction + MathF.PI_OVER_2);
	}
	
	public void accelerate(float magnitude) {
		speed += magnitude;
		recalculateComponents();
	}
	
	public void setDirection(float radians) {
		direction = radians;
		recalculateComponents();
	}
	
	public float getDirection() {
		return direction;
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
		recalculateComponents();
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void worldUpdated(World w, float timeElapsed) {
		move(new Vector3f(timeElapsed * xSpeed, 0, timeElapsed * zSpeed));
	}

	public void worldStarted(World w) {
		
	}
	
	public void worldPaused(World w) {
		
	}
	
	public void worldResumed(World w) {
		
	}

	public void worldClosed(World w) {
		
	}
}
