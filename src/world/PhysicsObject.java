package world;

import java.util.concurrent.ConcurrentHashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import vecmath.Vector3d;

public class PhysicsObject extends VisibleObject implements WorldUpdateObserver, Runnable {

	private Vector3d velocity = new Vector3d();
	private Vector3d angularVelocity = new Vector3d();
	private volatile double mass;
	private volatile double massReciprical;
	private Vector3d momentOfInertia;

	private Vector3d acceleration = new Vector3d();
	private Vector3d accelerationPartUpdate = new Vector3d();
	private Vector3d angularAcceleration = new Vector3d();
	private Vector3d angularAccelerationPartUpdate = new Vector3d();
	
	private DelayQueue<DelayedForce> forceRemovalQueue = new DelayQueue<DelayedForce>();
	private ConcurrentHashMap<Force, DelayedForce> delayedForces = new ConcurrentHashMap<Force, DelayedForce>();
	private LinkedHashSet<DynamicForce> dynamicForces = new LinkedHashSet<DynamicForce>();
	private ConcurrentHashMap<DynamicForce, Double> dynamicForceStartTimes = new ConcurrentHashMap<DynamicForce, Double>();

	private Thread forceRemovalThread = new Thread(this);
	private volatile double lastUpdateTime;
		
	public PhysicsObject(String modelName) {
		super(modelName);
		init();
	}
	
	public PhysicsObject(String modelName, Shader shader) {
		super(modelName, shader);
		init();
	}
	
	public PhysicsObject(PhysicsModel model, Shader shader) {
		super(model, shader);
		momentOfInertia = new Vector3d();
		setModel(model);
		init();
	}
	
	private void init() {
		forceRemovalThread.setDaemon(true);
		forceRemovalThread.start();
		lastUpdateTime = World.getMainWorldLiveSimulationTime();
	}
	
	protected void loadModel(String name) {
		if (momentOfInertia == null) {
			momentOfInertia = new Vector3d();
		}
		setModel(PhysicsModel.getModel(name, true));
	}
	
	public double getMass() {
		return mass;
	}
	
	public void setMass(double mass) {
		synchronized (this) {
			this.mass = mass;
			massReciprical = 1 / mass;
		}
	}
	
	public Vector3d getMomentOfInertia() {
		Vector3d ret = new Vector3d();
		synchronized (momentOfInertia) {
			ret.set(momentOfInertia);
		}
		return ret;
	}
	
	public void setMomentOfInertia(Vector3d momentOfInertia) {
		synchronized (this.momentOfInertia) {
			this.momentOfInertia.set(momentOfInertia);
		}
	}
	
	public Vector3d getVelocity() {
		Vector3d ret = new Vector3d();
		synchronized (velocity) {
			ret.set(velocity);
		}
		return ret;
	}
	
	public void setVelociy(Vector3d velocity) {
		synchronized (this.velocity) {
			this.velocity.set(velocity);
		}
	}
	
	public Vector3d getAcceleration() {
		Vector3d ret = new Vector3d();
		synchronized (acceleration) {
			ret.set(acceleration);
		}
		return ret;
	}
	
	public void setAcceleration(Vector3d acceleration) {
		synchronized (this.acceleration) {
			this.acceleration.set(acceleration);
		}
	}
	
	public Vector3d getAngularAcceleration() {
		Vector3d ret = new Vector3d();
		synchronized (angularAcceleration) {
			ret.set(angularAcceleration);
		}
		return ret;
	}
	
	public void setAngularAcceleration(Vector3d angularAcceleration) {
		synchronized (this.angularAcceleration) {
			this.angularAcceleration.set(angularAcceleration);
		}
	}
	
	public void setModel(PhysicsModel model) {
		super.setModel(model);
		setMass(model.getMass());
		setMomentOfInertia(model.getMomentOfInertia());
	}
	
	public void addForce(Vector3d force) {
		
		Vector3d v = new Vector3d();

		synchronized (this) {
			
			v.scale(massReciprical, force);
			synchronized (acceleration) {
				acceleration.add(v);
			}
			v.scale((World.getMainWorldSimulationTime() - lastUpdateTime) * massReciprical, force);
			accelerationPartUpdate.sub(v);
		}
	}
	
	public void addForce(Vector3d force, double currentTime) {
		
		Vector3d v = new Vector3d();
				
		synchronized (this) {
			v.scale(massReciprical, force);
			synchronized (acceleration) {
				acceleration.add(v);
			}
			v.scale((currentTime - lastUpdateTime) * massReciprical, force);
			accelerationPartUpdate.sub(v);
		}
	}
	
	public void addForce(Force force) {
		
		Vector3d v = new Vector3d();
		DelayedForce delayedForce;
		Vector3d torque = force.getTorque();
		
		synchronized (this) {
			delayedForce = new DelayedForce(force);
			double scale = (delayedForce.startTime - lastUpdateTime) * massReciprical;

			v.scale(massReciprical, force.getVector());
			synchronized (acceleration) {
				acceleration.add(v);
			}
			v.scale(scale, force.getVector());
			accelerationPartUpdate.sub(v);
			
			v.set(torque.x / momentOfInertia.x, torque.y / momentOfInertia.y, torque.z / momentOfInertia.z);
			synchronized (angularAcceleration) {
				angularAcceleration.add(v);
			}
			v.scale(scale, force.getTorque());
			angularAccelerationPartUpdate.sub(v);
		}
		delayedForces.put(force, delayedForce);
		forceRemovalQueue.add(delayedForce);
		forceRemovalThread.interrupt();
	}
	
	public void addForce(DynamicForce force) {
		
		Vector3d scaledForce = new Vector3d();
		
		synchronized (this) {
			double liveSimulationTime = World.getMainWorldLiveSimulationTime();
			
			Vector3d initialForce = force.forceForTime(0, this);
			double scale = (liveSimulationTime - lastUpdateTime) * massReciprical;
			
			scaledForce.scale(scale, initialForce);
			accelerationPartUpdate.sub(scaledForce);
			
			scaledForce.cross(initialForce, force.offsetForTime(0, this));
			scaledForce.scale(scale);
			angularAccelerationPartUpdate.sub(scaledForce);
			
			dynamicForces.add(force);
			dynamicForceStartTimes.put(force, World.getMainWorldLiveSimulationTime());
		}
	}
	
	
	public void removeForce(Vector3d force) {
		
		Vector3d v = new Vector3d();
		
		synchronized (this) {
			v.scale(massReciprical, force);
			synchronized (acceleration) {
				acceleration.sub(v);
			}
			v.scale((World.getMainWorldSimulationTime() - lastUpdateTime) * massReciprical, force);
			accelerationPartUpdate.add(v);
		}
	}
	
	public void removeForce(Vector3d force, double currentTime) {
		
		Vector3d v = new Vector3d();

		synchronized (this) {
			v.scale(massReciprical, force);
			synchronized (acceleration) {
				acceleration.sub(v);
			}
			v.scale((currentTime - lastUpdateTime) * massReciprical, force);		
			accelerationPartUpdate.add(v);
		}
	}
	
	public void removeForce(DynamicForce force) {
		
		Vector3d scaledForce = new Vector3d();

		synchronized (this) {
			
			double liveSimulationTime = World.getMainWorldLiveSimulationTime();
			double scale = (liveSimulationTime - lastUpdateTime) * massReciprical;
			double forceTime = liveSimulationTime - dynamicForceStartTimes.get(force);
			Vector3d currentForce = force.forceForTime(forceTime, this);
			
			scaledForce.scale(scale, currentForce);
			accelerationPartUpdate.add(scaledForce);
			
			scaledForce.cross(currentForce, force.offsetForTime(forceTime, this));
			scaledForce.scale(scale);
			angularAccelerationPartUpdate.add(scaledForce);
				
			dynamicForces.remove(force);
		}
	}
	
	private Vector3d scaledVector = new Vector3d();
	private Vector3d totalScaledVector = new Vector3d();
	private Vector3d angularScaledVector = new Vector3d();
	private Vector3d totalAngularScaledVector = new Vector3d();

	@Override
	public void worldUpdated(World w, double deltaTime) {
		
		double currentTime = w.getSimulationTime();
		
		for (DynamicForce force : dynamicForces) {
			double time = currentTime - dynamicForceStartTimes.get(force);
			
			Vector3d currentForce = force.forceForTime(time, this);
			
			scaledVector.scale(deltaTime, currentForce);
			totalScaledVector.add(scaledVector);
			
			angularScaledVector.cross(currentForce, force.offsetForTime(time, this));
			angularScaledVector.scale(deltaTime);
			totalAngularScaledVector.add(angularScaledVector);
		}
		
		synchronized (this) {
			synchronized (acceleration) {
				scaledVector.scale(deltaTime, acceleration);
			}

			synchronized (velocity) {
				velocity.add(scaledVector);
				velocity.add(totalScaledVector);
				velocity.add(accelerationPartUpdate);
				scaledVector.scale(deltaTime, velocity);
			}
			
			synchronized (angularAcceleration) {
				angularScaledVector.scale(deltaTime, angularAcceleration);
			}
			
			synchronized (angularVelocity) {
				angularVelocity.add(angularScaledVector);
				angularVelocity.add(totalAngularScaledVector);
				angularVelocity.add(angularAccelerationPartUpdate);
				angularScaledVector.scale(deltaTime, angularVelocity);
			}
			
			angularAccelerationPartUpdate.set(0, 0, 0);
			accelerationPartUpdate.set(0, 0, 0);
			lastUpdateTime = currentTime;
		}
		move(scaledVector);
		rotate(angularVelocity);
	}

	@Override
	public void worldStarted(World w) {
		
	}

	@Override
	public void worldPaused(World w) {
		
	}

	@Override
	public void worldResumed(World w) {
		
	}

	@Override
	public void worldClosed(World w) {
		
	}

	private Vector3d temp = new Vector3d();
	
	@Override
	public void run() {
		while (true) {
			
			DelayedForce f = forceRemovalQueue.poll();

			while (f != null) {
				
				Vector3d torque = f.force.getTorque();
				
				synchronized (this) {
					double scale = (f.stopTime - lastUpdateTime) * massReciprical;
					
					temp.scale(massReciprical, f.force.getVector());
					synchronized (acceleration) {
						acceleration.sub(temp);
					}
					temp.scale(scale, f.force.getVector());
					accelerationPartUpdate.add(temp);
					
					temp.set(torque.x / momentOfInertia.x, torque.y / momentOfInertia.y, torque.z / momentOfInertia.z);
					synchronized (angularAcceleration) {
						angularAcceleration.sub(temp);
					}
					temp.scale(scale, f.force.getTorque());
					angularAccelerationPartUpdate.add(temp);
				}
				f = forceRemovalQueue.poll();
			}
			
			f = forceRemovalQueue.peek();
			try {
				if (f == null) {
					Thread.sleep(1000);
				}
				else {
					long nanos = f.getDelay(TimeUnit.NANOSECONDS);
					Thread.sleep(nanos / 1000000, (int)(nanos % 999999));
				}
			}
			catch (InterruptedException e) {
				
			}
			
		}
	}
	
}


class DelayedForce implements Delayed {

	public final Force force;
	public final double startTime;
	public final double stopTime;
	
	public DelayedForce(Force f) {
		force = f;
		startTime = World.getMainWorldLiveSimulationTime();
		stopTime = force.getTimeRemaining(startTime, startTime) + startTime;
	}
	
	public boolean hasExpired() {
		return getDelay(TimeUnit.NANOSECONDS) <= 0;
	}
	
	@Override
	public int compareTo(Delayed arg0) {
		
		return (int)(getDelay(TimeUnit.NANOSECONDS) - arg0.getDelay(TimeUnit.NANOSECONDS));
	}
	
	@Override
	public long getDelay(TimeUnit unit) {
		
		return (long)(force.getTimeRemaining(startTime, World.getMainWorldLiveSimulationTime()) * 1000000000);
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof DelayedForce)) {
			return false;
		}
		DelayedForce f = (DelayedForce) o;
		return f == this || f.force.equals(force);
	}
	
	public boolean equals(DelayedForce f) {
		return f == this || f.force.equals(force);
	}
}