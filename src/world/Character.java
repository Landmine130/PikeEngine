package world;
import vecmath.Vector3d;


public class Character extends VisibleObject implements WorldUpdateObserver {
	
	private static final double PI_OVER_2 = Math.PI / 2;
	
	private volatile double xSpeed;
	private volatile double zSpeed;
	private volatile double speed;
	private volatile double direction;
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
	
	public double getXSpeed() {
		return xSpeed;
	}
	
	public double getZSpeed() {
		return zSpeed;
	}
	
	public void setXSpeed(double speed) {
		xSpeed = speed;
		recalculateVector();
	}
	
	public void setZSpeed(double speed) {
		zSpeed = speed;
		recalculateVector();
	}
	
	public void accelerateX(double magnitude) {
		xSpeed += magnitude;
		recalculateVector();
	}
	
	public void accelerateZ(double magnitude) {
		zSpeed += magnitude;
		recalculateVector();
	}
	
	private void recalculateVector() {
		direction = Math.atan2(zSpeed, xSpeed) - PI_OVER_2;
		speed = Math.sqrt(xSpeed * xSpeed + zSpeed * zSpeed);
	}
	
	private void recalculateComponents() {
		xSpeed = speed * Math.cos(direction + PI_OVER_2);
		zSpeed = speed * Math.sin(direction + PI_OVER_2);
	}
	
	public void accelerate(double magnitude) {
		speed += magnitude;
		recalculateComponents();
	}
	
	public void setDirection(double radians) {
		direction = radians;
		recalculateComponents();
	}
	
	public double getDirection() {
		return direction;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
		recalculateComponents();
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void worldUpdated(World w, double timeElapsed) {
		move(new Vector3d(timeElapsed * xSpeed, 0, timeElapsed * zSpeed));
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
