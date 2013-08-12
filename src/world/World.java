package world;

import misc.Timer;
import misc.Benchmark;
import misc.InputHandler;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;


public class World {
		
	private final int GL_MAJOR_VERSION = 3;
	private final int GL_MINOR_VERSION = 2;
	
	private static final int INITIAL_WIDTH = 800;
	private static final int INITIAL_HEIGHT = 450;
	
	private LinkedHashSet<WorldUpdateObserver> observers = new LinkedHashSet<WorldUpdateObserver>();
	private LinkedHashSet<Drawable> drawableObjects = new LinkedHashSet<Drawable>();
	
	private volatile boolean isRunning;
	private volatile boolean isPaused;
	
	private volatile int targetFPS = 60;
	
	private SoundManager soundManager;
	
	private int alphaBufferPrecision = 8;
	private int depthBufferPrecision = 24;
	private int stencilBufferPrecision = 0;
	
	private double startTime;
	private double simulationTime;
	
	private volatile Drawable skybox;
	private volatile ViewPoint viewPoint;

	private volatile double fps;
	
	private BackgroundUpdater[] updaters;
	private BackgroundDrawPreparer[] drawPreparers;
	private Timer processorCheckTimer = new Timer(5d, -1d, this, "updateProcessorCount");
	private Benchmark benchmark = new Benchmark();
	private Drawable lastDrawn = null;
	
	LinkedHashSet<Drawable> tempDrawableObjects = new LinkedHashSet<Drawable>();
	LinkedHashSet<WorldUpdateObserver> tempObservers = new LinkedHashSet<WorldUpdateObserver>();

	private boolean started = false;
	
	private static World mainWorld;

	public static double getMainWorldLiveSimulationTime() {
		return mainWorld.getLiveSimulationTime();
	}
	
	public static double getMainWorldSimulationTime() {
		return mainWorld.getSimulationTime();
	}
	
	public World() {
		
		mainWorld = this;
		soundManager = new SoundManager();
		soundManager.initialize(8);
		
		isPaused = false;
		
		try {
			Display.setTitle("Space");
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
		
		updaters = new BackgroundUpdater[Runtime.getRuntime().availableProcessors()];
		
		for (int i = 0; i < updaters.length; i++) {
			updaters[i] = new BackgroundUpdater(this);
		}
		
		drawPreparers = new BackgroundDrawPreparer[Runtime.getRuntime().availableProcessors()];
		
		for (int i = 0; i < drawPreparers.length; i++) {
			drawPreparers[i] = new BackgroundDrawPreparer(this);
		}
		
		processorCheckTimer.start();
		
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
	    
		glActiveTexture(GL_TEXTURE0);

	    
	}
	
	public ViewPoint getViewPoint() {
		return viewPoint;
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
	
	public int getTargetFPS() {
		return targetFPS;
	}
	
	public void setTargetFPS(int fps) {
		targetFPS = fps;
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
	
	public double getSimulationTime() {
		return simulationTime;
	}
	
	public double getLiveSimulationTime() {
		if (isPaused) {
			return simulationTime;
		}
		else {
			return Timer.getTime() - startTime;
		}
	}
	
	public void setPaused(boolean isPaused) {
		
		this.isPaused = isPaused;
		Keyboard.enableRepeatEvents(isPaused);
		Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
		Mouse.setGrabbed(!isPaused);
		Mouse.setClipMouseCoordinatesToWindow(!isPaused);
	}
	
	public boolean isPaused() {
		return isPaused;
	}
	
	public void start() {
		
		boolean shouldStart;
		synchronized (this) {
			shouldStart = !started;
			started = true;
		}
		
		if (shouldStart) {
			setPaused(false);
			notifyObserversOfStart();
			double fpsTime = Timer.getTime();
			boolean wasPaused = isPaused();
			simulationTime = 0;
			startTime = Timer.getTime();
			while (isRunning) {
				if(Display.wasResized()) {
					glViewport(0, 0, Display.getWidth(), Display.getHeight());
					viewPoint.updatePerspectiveMatrix();
				}
				if (!isPaused) {
					if (wasPaused) {
						notifyObserversOfResume();
						wasPaused = false;
						startTime = Timer.getTime() - simulationTime;
					}
					double newSimulationTime = Timer.getTime() - startTime;
					double deltaTime = newSimulationTime - simulationTime;
					simulationTime = newSimulationTime;
					notifyObserversOfUpdate(deltaTime);
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
					terminate();
				}
			}
			notifyObserversOfClose();
			tearDownGL();
		}
	}
	
	public void terminate() {
		isRunning = false;
	}
		
	private void drawSkybox() {
		skybox.prepareToDrawInWorld(this);
		if (skybox.needsDrawn()) {
			skybox.drawInWorld(this, lastDrawn);
			lastDrawn = skybox;
		}
	}
	
	public void updateProcessorCount() {
		int processorCount = Runtime.getRuntime().availableProcessors();
		
		if (processorCount != updaters.length) {
			BackgroundUpdater[] updaterTemp = new BackgroundUpdater[processorCount];
			
			for (int i = 0; i < updaterTemp.length; i++) {
				if (i < updaters.length) {
					updaterTemp[i] = updaters[i];
				}
				else {
					updaterTemp[i] = new BackgroundUpdater(this);
				}
			}
			
			synchronized (updaters) {
				
				for (int i = updaterTemp.length; i < updaters.length; i++) {
					updaters[i].stop();
				}
				
				updaters = updaterTemp;
			}
			
			BackgroundDrawPreparer[] preparerTemp;
			if (processorCount == 1) {
				preparerTemp = new BackgroundDrawPreparer[1];
			}
			else {
				preparerTemp = new BackgroundDrawPreparer[processorCount - 1];
			}
			
			
			for (int i = 0; i < preparerTemp.length; i++) {
				if (i < drawPreparers.length) {
					preparerTemp[i] = drawPreparers[i];
				}
				else {
					preparerTemp[i] = new BackgroundDrawPreparer(this);
				}
			}
			
			synchronized (drawPreparers) {
				
				for (int i = preparerTemp.length; i < drawPreparers.length; i++) {
					drawPreparers[i].stop();
				}
				
				drawPreparers = preparerTemp;
			}
		}
	}
	
	private LinkedList<Drawable> preparedDrawables = new LinkedList<Drawable>();
	
	public void addPreparedDrawable(Drawable d) {
		synchronized (preparedDrawables) {
			preparedDrawables.add(d);
			preparedDrawables.notifyAll();
		}
	}

	LinkedList<Drawable> tempPreparedDrawables = new LinkedList<Drawable>();

	private void drawFrame() {

		synchronized (drawableObjects) {
			tempDrawableObjects.addAll(drawableObjects);
		}
		Iterator<Drawable> iterator = tempDrawableObjects.iterator(); 
		
		synchronized (drawPreparers) {
			
			int objectsPerUpdater = tempDrawableObjects.size() / drawPreparers.length + 1;
			int remaining = tempDrawableObjects.size();

			for (int i = 0; i < drawPreparers.length; i++) {
								
				for (int j = 0; j < objectsPerUpdater && iterator.hasNext(); j++) {
					
					drawPreparers[i].add(iterator.next());
					iterator.remove();
				}
			}

			Drawable d;
			
			while (remaining > 0) {
				try {
					synchronized (preparedDrawables) {
						if (preparedDrawables.size() == 0) {
							preparedDrawables.wait(100);
						}
						tempPreparedDrawables.addAll(preparedDrawables);
						preparedDrawables.clear();
					}
				}
				catch (InterruptedException e) {
					
				}

				while (tempPreparedDrawables.size() > 0) {
					d = tempPreparedDrawables.removeFirst();
					if (d.needsDrawn()) {
						d.drawInWorld(this, lastDrawn);
						lastDrawn = d;
					}
					remaining--;
				}
			}
		}
	}
	
	private void notifyObserversOfUpdate(double deltaTime) {
		
		synchronized (observers) {
			tempObservers.addAll(observers);
		}
		Iterator<WorldUpdateObserver> iterator = tempObservers.iterator();
		
		synchronized (updaters) {
			
			int objectsPerUpdater = tempObservers.size() / updaters.length + 1;
			
			for (int i = 0; i < updaters.length; i++) {
				
				updaters[i].setDeltaTime(deltaTime);
				
				for (int j = 0; j < objectsPerUpdater && iterator.hasNext(); j++) {

					updaters[i].add(iterator.next());
					iterator.remove();
				}
			}
			
			boolean updatersUpdating = false;
			synchronized (this) {
				for (int i = 0; i < updaters.length; i++) {
					updatersUpdating = updatersUpdating || updaters[i].size() > 0;
				}
				while (updatersUpdating) {
					try {
						wait(100);
					}
					catch (InterruptedException e) {
						
					}
					
					updatersUpdating = false;
					
					for (int i = 0; i < updaters.length; i++) {
						updatersUpdating = updatersUpdating || updaters[i].size() > 0;
					}
				}
			}
		}
	}
	
	private void notifyObserversOfStart() {
		
		synchronized (observers) {
			tempObservers.addAll(observers);
		}
		
		for (WorldUpdateObserver o : tempObservers) {
			o.worldStarted(this);
		}
		tempObservers.clear();
	}
	
	private void notifyObserversOfClose() {
		
		synchronized (observers) {
			tempObservers.addAll(observers);
		}
		
		for (WorldUpdateObserver o : tempObservers) {
			o.worldClosed(this);
		}
		tempObservers.clear();
	}
	
	private void notifyObserversOfPause() {
		
		synchronized (observers) {
			tempObservers.addAll(observers);
		}
		
		for (WorldUpdateObserver o : tempObservers) {
			o.worldPaused(this);
		}
		tempObservers.clear();
	}
	
	private void notifyObserversOfResume() {
		synchronized (observers) {
			tempObservers.addAll(observers);
		}
		
		for (WorldUpdateObserver o : tempObservers) {
			o.worldResumed(this);
		}
		tempObservers.clear();
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
	
	private void tearDownGL() {
		processorCheckTimer.stop();
		synchronized (updaters) {
			for (int i = 0; i < updaters.length; i++) {
				updaters[i].stop();
			}
		}
		synchronized (drawPreparers) {
			for (int i = 0; i < drawPreparers.length; i++) {
				drawPreparers[i].stop();
			}
		}
		soundManager.destroy();
		Display.destroy();
	}
}


class BackgroundUpdater implements Runnable {

	private LinkedList<WorldUpdateObserver> objects = new LinkedList<WorldUpdateObserver>();
	
	private final Thread thread = new Thread(this);
	private World world;
	private volatile double deltaTime = 0;
	private boolean paused = false;
	private boolean running = true;
	
	public BackgroundUpdater(World w) {
		world = w;
		thread.setName("Updater " + thread.getName());
		thread.start();
	}
	
	public void add(WorldUpdateObserver o) {
		synchronized (objects) {
			objects.add(o);
			objects.notifyAll();
		}
	}
	
	public void setDeltaTime(double deltaTime) {
		this.deltaTime = deltaTime;
	}
	
	private LinkedList<WorldUpdateObserver> list = new LinkedList<WorldUpdateObserver>();
	
	public void run() {
		
		synchronized (objects) {
			list.addAll(objects);
		}
		
		 while (list.size() == 0) {
			try {
				synchronized (objects) {
					objects.wait(100);
				}
			}
			catch (InterruptedException e) {
				
			}
			if (!running) {
				break;
			}
			synchronized (objects) {
				list.addAll(objects);
				objects.clear();
			}
		}
		
		mainLoop:
		while (running) {

			while (list.size() > 0) {
							
				while (paused) {
					try {
						synchronized (this) {
							wait(100);
						}
					}
					catch (InterruptedException e) {
						
					}
				}
				list.removeFirst().worldUpdated(world, deltaTime);
			}
			
			if (!running) {
				break mainLoop;
			}
			
			synchronized (objects) {
				list.addAll(objects);
				objects.clear();
			}
			if (list.size() == 0) {
				synchronized (world) {
					world.notifyAll();
				}
				do {
					try {
						synchronized (objects) {
							objects.wait(100);
						}
					}
					catch (InterruptedException e) {
						
					}
					if (!running) {
						break mainLoop;
					}
					synchronized (objects) {
						list.addAll(objects);
						objects.clear();
					}
				} while (list.size() == 0);
			}
		}
	}
	
	public boolean isPuased() {
		return paused;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
		if (!paused) {
			synchronized (this) {
				notifyAll();
			}
		}
	}
	
	public int size() {
		synchronized (objects) {
			return objects.size();
		}
	}
	
	/**
	 * Permanently stops this object from drawing.
	 */
	public void stop() {
		running = false;
		paused = false;
		synchronized (objects) {
			objects.notifyAll();
		}
		synchronized (this) {
			notifyAll();
		}
	}
}

class BackgroundDrawPreparer implements Runnable {

	private LinkedList<Drawable> objects = new LinkedList<Drawable>();
	private final Thread thread = new Thread(this);
	private World world;
	private boolean paused = false;
	private boolean running = true;
	
	public BackgroundDrawPreparer(World w) {
		world = w;
		thread.setName("DrawPreparer " + thread.getName());
		thread.start();
	}
	
	public void add(Drawable o) {
		synchronized (objects) {
			objects.add(o);
			objects.notifyAll();
		}
	}
	
	private LinkedList<Drawable> list = new LinkedList<Drawable>();
	
	public void run() {
		
		synchronized (objects) {
			list.addAll(objects);
		}
		
		 while (list.size() == 0) {
			try {
				synchronized (objects) {
					objects.wait(100);
				}
			}
			catch (InterruptedException e) {
				
			}
			if (!running) {
				break;
			}
			synchronized (objects) {
				list.addAll(objects);
				objects.clear();
			}
		}
		
		 Benchmark bench = new Benchmark();
		
		mainLoop:
		while (running) {

			while (list.size() > 0) {
							
				while (paused) {
					try {
						synchronized (this) {
							wait(100);
						}
					}
					catch (InterruptedException e) {
						
					}
				}
				Drawable d = list.removeFirst();
				d.prepareToDrawInWorld(world);
				world.addPreparedDrawable(d);
			}
			
			if (!running) {
				break mainLoop;
			}
			
			synchronized (objects) {
				list.addAll(objects);
				objects.clear();
			}
			while (list.size() == 0) {
				try {
					synchronized (objects) {
						//bench.startTimer();
						objects.wait(100);
						//bench.printTimeElapsed();
					}
				}
				catch (InterruptedException e) {
					
				}
				if (!running) {
					break mainLoop;
				}
				synchronized (objects) {
					list.addAll(objects);
					objects.clear();
				}
			}
		}
	}
	
	public boolean isPuased() {
		return paused;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
		if (!paused) {
			synchronized (this) {
				notifyAll();
			}
		}
	}
	
	public int size() {
		synchronized (objects) {
			return objects.size();
		}
	}
	
	/**
	 * Permanently stops this object from drawing.
	 */
	public void stop() {
		running = false;
		paused = false;
		synchronized (objects) {
			objects.notifyAll();
		}
		synchronized (this) {
			notifyAll();
		}
	}
}

