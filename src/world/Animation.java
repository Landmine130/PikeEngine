package world;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

import misc.Md5ToModeldata;
import misc.Timer;
import vecmath.Matrix4f;
import vecmath.Quat4f;
import vecmath.Vector3f;

public class Animation {
	
	public static final int MAX_JOINTS = 40;
	
	public static final String ANIMATIONDATA_PATH = "Resources/Models/Animaitons/";
	public static final String ANIMATION_FILE_EXTENSION = ".animationdata";

	public static final HashMap<String, Animation> loadedAnimations = new HashMap<String, Animation>();
	
	private static final int HEADER_SIZE = 3;
	private static final int FLOAT_BYTE_COUNT = Float.SIZE / Byte.SIZE;
	public static Animation getAnimationForName(String name) {
		
		String path = ANIMATIONDATA_PATH + name + ANIMATION_FILE_EXTENSION;
		Animation a = loadedAnimations.get(path);
		
		if (a == null) {
						
			File file = new File(path);
			long dataSize = file.length() / FLOAT_BYTE_COUNT - HEADER_SIZE;
			try {
				 DataInputStream reader = new DataInputStream(new FileInputStream(file));
				
				int fps = reader.readInt();
				int frameCount = reader.readInt();
				int jointCount = reader.readInt();

				Vector3f[][] positionFrames = new Vector3f[frameCount][jointCount];
				Quat4f[][] orientationFrames = new Quat4f[frameCount][jointCount];
				
				int[] parentJointIndexes = new int[jointCount];
				float[] frameRadii = new float[frameCount];

				for (int i = 0; i < frameCount; i++) {
					
					frameRadii[i] = reader.readFloat();
					
					int parsedFloats = i * jointCount;
					
					for (int j = 0; j < jointCount && parsedFloats + j < dataSize; j++) {
						positionFrames[i][j] = new Vector3f(reader.readFloat(), reader.readFloat(), reader.readFloat());
						float ox = reader.readFloat();
						float oy = reader.readFloat();
						float oz = reader.readFloat();
						// possible optimization: Calculate ow instead of reading from file
						float ow = reader.readFloat();
						orientationFrames[i][j] = new Quat4f(ox, oy, oz, ow);
					}
				}
				
				for (int i = 0; i < jointCount; i++) {
					parentJointIndexes[i] = reader.readInt();
				}
				reader.close();

				a = new Animation(positionFrames, orientationFrames, parentJointIndexes, frameRadii, 1.0f / fps);
				loadedAnimations.put(path, a);
			}
			catch (Exception e) {
				System.err.println("Error: could not read file " + path);
				e.printStackTrace();
				return null;
			}
		}
		a = new Animation(a);
		return a;
	}
	
	
	private volatile double startTime = World.getMainWorldSimulationTime();
	private volatile boolean running = false;
	private volatile double stopTime;
	private float frameLength;
	private Vector3f[][] positionFrames;
	private Quat4f[][] orientationFrames;
	private int[] parentJointIndexes;
	private float[] frameRadii;
	
	/**
	 * Creates a new Animation object with the specified parameters
	 * @param frames an array of frames with each frame represented as an array of Matrix4f objects
	 * @param parentJointIndexes an array which specifies the index of the parent for each Matrix4f in a frame
	 * @param frameLength the duration of a single frame in seconds
	 */
	public Animation(Vector3f[][] positionFrames, Quat4f[][] orientationFrames, int[] parentJointIndexes, float[] frameRadii, float frameLength) {
		this.positionFrames = positionFrames;
		this.orientationFrames = orientationFrames;
		this.parentJointIndexes = parentJointIndexes;
		this.frameLength = frameLength;
		this.frameRadii = frameRadii;
	}
	
	public Animation(Animation a) {
		this.positionFrames = a.positionFrames;
		this.orientationFrames = a.orientationFrames;		this.parentJointIndexes = a.parentJointIndexes;
		this.frameLength = a.frameLength;
		this.frameRadii = a.frameRadii;
	}
	
	public double getTime() {
		if (running) {
			setTime(World.getMainWorldSimulationTime() - startTime);
			return World.getMainWorldSimulationTime() - startTime;
		}
		else {
			return stopTime;
		}
	}
	
	public void setTime(double time) {
		double length = frameLength * getFrameCount();
		time = time % length;
		startTime = World.getMainWorldSimulationTime() - time;
		stopTime = time;
	}
	
	public void start() {
		setTime(stopTime);
		running = true;
	}
	
	public void stop() {
		stopTime = getTime();
		running = false;
	}
	
	public void reset() {
		setTime(0);
	}
	
	public float getRadius(double animationTime) {
		
		double currentFrame = animationTime / frameLength;
		int currentIndex = (int) currentFrame;
		int nextIndex = (currentIndex + 1) % getFrameCount();
		
		float currentFrameRadius = frameRadii[currentIndex];
		float nextFrameRadius = frameRadii[nextIndex];
		
		return currentFrameRadius > nextFrameRadius ? currentFrameRadius : nextFrameRadius;
	}
	
	public float getRadius() {
		return getRadius(getTime());
	}
	
	public int getFrameCount() {
		return positionFrames.length;
	}
	
	public int getBoneCount() {
		return positionFrames[0].length;
	}
	
	/**
	 * Calculates object-local positions of all bones in this animation for a given time
	 * @param animationTime the time since animation start that the return value is calculated for
	 * @return the object-local positions of the bones for this animation
	 */
	public Matrix4f[] getJointPositions(double animationTime) {
		
		Matrix4f m2 = new Matrix4f(); 

		Vector3f interpolatedPosition = new Vector3f();
		Quat4f interpolatedOrientation = new Quat4f();

		Matrix4f[] currentPositions = new Matrix4f[getBoneCount()];
		
		double currentFrame = animationTime / frameLength;
		int currentIndex = (int) currentFrame;
		int nextIndex = (currentIndex + 1) % getFrameCount();
		
		Vector3f[] lastFramePositions = positionFrames[currentIndex];
		Quat4f[] lastFrameOrientations = orientationFrames[currentIndex];
		Vector3f[] nextFramePositions = positionFrames[nextIndex];
		Quat4f[] nextFrameOrientations = orientationFrames[nextIndex];
		
		float nextFrameWeight = (float)(currentFrame - currentIndex);
		
		for (int i = 0; i < lastFramePositions.length; i++) {
			
			interpolatedPosition.set(lastFramePositions[i]);
			interpolatedOrientation.set(lastFrameOrientations[i]);
			
			interpolatedPosition.lerp(nextFramePositions[i], nextFrameWeight);
			interpolatedOrientation.slerp(nextFrameOrientations[i], nextFrameWeight);
			
			Matrix4f interpolatedMatrix = new Matrix4f(interpolatedPosition);
			m2.set(interpolatedOrientation);
			interpolatedMatrix.mul(m2);
			
			int parentIndex = parentJointIndexes[i];
			
			if (parentIndex >= 0) {
				interpolatedMatrix.mul(currentPositions[parentIndex], interpolatedMatrix);
			}
			currentPositions[i] = interpolatedMatrix;
		}
		
		return currentPositions;
	}
	
	/**
	 * Calculates object-local positions of all bones in this animation for the time since animation start
	 * @return the current object-local positions of the bones for this animation
	 */
	public Matrix4f[] getJointPositions() {
		return getJointPositions(getTime());
	}
}
