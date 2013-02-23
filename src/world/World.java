package world;

import misc.Timer;
import misc.InputHandler;
import misc.MathF;
import java.nio.FloatBuffer;
import java.util.LinkedHashSet;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;

import vecmath.Matrix3f;
import vecmath.Matrix4f;
import vecmath.Vector3f;
import world.terrain.Terrain;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class World implements WorldObjectMovementObserver {
	
	private ViewPoint viewPoint;
	
	private final int GL_MAJOR_VERSION = 3;
	private final int GL_MINOR_VERSION = 2;
	
	private static final int INITIAL_WIDTH = 800;
	private static final int INITIAL_HEIGHT = 450;
	
	private LinkedHashSet<WorldUpdateObserver> observers = new LinkedHashSet<WorldUpdateObserver>();
	private LinkedHashSet<Drawable> drawableObjects = new LinkedHashSet<Drawable>();
	
	private volatile boolean isRunning;
	private volatile boolean isPaused;
	
	private int targetFPS = 60;
	
	private SoundManager soundManager;
	
	private int alphaBufferPrecision = 8;
	private int depthBufferPrecision = 24;
	private int stencilBufferPrecision = 0;
	
	private double timeSinceLastUpdate;
	
	private volatile Drawable skybox;
	private volatile Terrain terrain;

	private volatile double fps;

	public World(long seed) {
		
		soundManager = new SoundManager();
		soundManager.initialize(8);
		
		isPaused = false;
		
		try {
			Display.setTitle("Robots");
			Display.setDisplayMode(new DisplayMode(INITIAL_WIDTH, INITIAL_HEIGHT));
			PixelFormat pixelFormat = new PixelFormat(alphaBufferPrecision, depthBufferPrecision, stencilBufferPrecision);
			ContextAttribs contextAtrributes = new ContextAttribs(GL_MAJOR_VERSION, GL_MINOR_VERSION).withProfileCore(true).withForwardCompatible(true);			 
			Display.create(pixelFormat, contextAtrributes);
			isRunning = true;
			
		} catch (LWJGLException le) {
			System.err.println("Game exiting - exception during initialization:");
			le.printStackTrace();
			isRunning = false;
			return;
		}
		
		viewPoint = new ViewPoint();
		
		glEnable(GL_CULL_FACE);
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);

	    glEnable(GL_BLEND);
	    glBlendFunc(GL_ONE, GL_ALPHA);
		glEnable(GL_TEXTURE_2D);
	    glEnable(GL_DEPTH_TEST);
	    glDepthMask(true);
	    glDepthFunc(GL_LEQUAL);
	    
		terrain = new Terrain(seed);
	}
	
	
	public Drawable getSkybox() {
		return skybox;
	}
	
	public void setSkybox(Drawable skybox) {
		this.skybox = skybox;
	}

	public void addObserver(WorldUpdateObserver o) {
		synchronized (observers) {
			observers.add(o);
		}
	}
	
	public void removeObserver(WorldUpdateObserver o) {
		synchronized (observers) {
			observers.remove(o);
		}
	}
	
	public void addDrawable(Drawable o) {
		synchronized (drawableObjects) {
			drawableObjects.add(o);
		}
	}
	
	public void removeDrawable(Drawable o) {
		synchronized (drawableObjects) {
			drawableObjects.remove(o);
		}
	}
	
	/**
   * Sets the display mode for fullscreen mode
	 */
	/*
	private boolean setDisplayMode() {
	    try {
			// get modes
			DisplayMode[] dm = org.lwjgl.util.Display.getAvailableDisplayModes(width, height, -1, -1, -1, -1, 60, 60);
	
	      org.lwjgl.util.Display.setDisplayMode(dm, new String[] {
	          "width=" + width,
	          "height=" + height,
	          "freq=" + 60,
	          "bpp=" + org.lwjgl.opengl.Display.getDisplayMode().getBitsPerPixel()
	         });
	      return true;
	    } catch (Exception e) {
	      e.printStackTrace();
	      System.err.println("Unable to enter fullscreen, continuing in windowed mode");
	      return false;
	    }
	}
	*/
	
	public void setPaused(boolean isPaused) {
		
		this.isPaused = isPaused;
		Keyboard.enableRepeatEvents(isPaused);
		Mouse.setGrabbed(!isPaused);
		Mouse.setClipMouseCoordinatesToWindow(!isPaused);
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public ViewPoint getViewPoint() {
		return viewPoint;
	}
	
	public void stop() {
		
		isRunning = false;
	}
	
	private void tearDownGL() {
		
		soundManager.destroy();
		Display.destroy();
	}
	
	public void start() {
		setPaused(false);
		notifyObserversOfStart();
		double fpsTime = Timer.getTime();
		boolean wasPaused = isPaused();
		timeSinceLastUpdate = Timer.getTime();
		while (isRunning) {
			if(Display.wasResized()) {
				glViewport(0, 0, Display.getWidth(), Display.getHeight());
				viewPoint.updatePerspectiveMatrix();
			}
			if (!isPaused) {
				if (wasPaused) {
					notifyObserversOfResume();
					wasPaused = false;
				}
				notifyObserversOfUpdate();
				timeSinceLastUpdate = Timer.getTime();
			}
			else if (!wasPaused) {
				notifyObserversOfPause();
				wasPaused = true;
			}
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			if (skybox != null) {
				glDepthRange(0f, .99f);
				drawFrame();
				glDepthRange(.99f, 1f);
				drawSkybox();
			}
			else {
				drawFrame();
			}
			Display.update();
			Display.sync(targetFPS);
			double currentTime = Timer.getTime();
			fps = (1 / (currentTime - fpsTime) + fps) / 2;
			fpsTime = currentTime;
			InputHandler.checkInput();
			if (Display.isCloseRequested()) {
				stop();
			}
		}
		notifyObserversOfClose();
		tearDownGL();
	}
	
	private void drawSkybox() {
		skybox.drawInWorld(this);
}

	
	private void drawFrame() {
		
		LinkedHashSet<Drawable> set;
		synchronized (drawableObjects) {
			set = new LinkedHashSet<Drawable>(drawableObjects);
		}
		for (Drawable o : set) {
			o.drawInWorld(this);
		}
	}
	
	private void notifyObserversOfUpdate() {
		
		LinkedHashSet<WorldUpdateObserver> set;
		synchronized (observers) {
			set = new LinkedHashSet<WorldUpdateObserver>(observers);
		}
		float timeElapsed = (float)(Timer.getTime() - timeSinceLastUpdate);
		for (WorldUpdateObserver o : set) {
			o.worldUpdated(this, timeElapsed);
		}
	}
	
	private void notifyObserversOfStart() {
		
		LinkedHashSet<WorldUpdateObserver> set;
		synchronized (observers) {
			set = new LinkedHashSet<WorldUpdateObserver>(observers);
		}
		
		for (WorldUpdateObserver o : set) {
			o.worldStarted(this);
		}
	}
	
	private void notifyObserversOfClose() {
		
		LinkedHashSet<WorldUpdateObserver> set;
		synchronized (observers) {
			set = new LinkedHashSet<WorldUpdateObserver>(observers);
		}
		
		for (WorldUpdateObserver o : set) {
			o.worldClosed(this);
		}
	}
	
	private void notifyObserversOfPause() {
		
		LinkedHashSet<WorldUpdateObserver> set;
		synchronized (observers) {
			set = new LinkedHashSet<WorldUpdateObserver>(observers);
		}
		
		for (WorldUpdateObserver o : set) {
			o.worldPaused(this);
		}
	}
	
	private void notifyObserversOfResume() {
		LinkedHashSet<WorldUpdateObserver> set;
		synchronized (observers) {
			set = new LinkedHashSet<WorldUpdateObserver>(observers);
		}
		
		for (WorldUpdateObserver o : set) {
			o.worldResumed(this);
		}
	}
	
	public double getFPS() {
		return fps;
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setViewPoint(ViewPoint viewPoint) {

		this.viewPoint = viewPoint;
	}


	public void worldObjectWillMove(WorldObject o, Vector3f newPosition) {
		
	}

	public void worldObjectDidMove(WorldObject o) {
		
	}

	public void worldObjectWillRotate(WorldObject o, Vector3f newOrientation) {
		
	}

	public void worldObjectDidRotate(WorldObject o) {
		
	}
	
	public Terrain getTerrain() {
		return terrain;
	}
}

