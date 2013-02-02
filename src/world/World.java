package world;

import misc.Timer;
import misc.InputHandler;
import misc.MathF;
import java.nio.FloatBuffer;
import java.util.LinkedHashSet;
import java.util.TreeSet;

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
	
	private LinkedHashSet<WorldUpdateObserver> observers = new LinkedHashSet<WorldUpdateObserver>();
	private TreeSet<VisibleObject> visibleObjects = new TreeSet<VisibleObject>();
	private LinkedHashSet<WorldObject> worldObjects = new LinkedHashSet<WorldObject>();

	private volatile boolean isRunning;
	private volatile boolean isPaused;
	
	private int targetFPS = 60;
	
	private SoundManager soundManager;
	
	private final int INITIAL_WIDTH = 800;
	private final int INITIAL_HEIGHT = 450;
	
	private int alphaBufferPrecision = 8;
	private int depthBufferPrecision = 24;
	private int stencilBufferPrecision = 0;

	private boolean fullscreen;
	
	private double timeSinceLastUpdate;
	
	private Skybox skybox;
	
	private volatile double fps;
	
	public World() {
		
		soundManager = new SoundManager();
		soundManager.initialize(8);
		
		fullscreen = false;
		isPaused = false;
		
		try {
			//setDisplayMode();
			Display.setTitle("Game");
			Display.setFullscreen(fullscreen);
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
	}
	
	
	public Skybox getSkybox() {
		return skybox;
	}
	
	public void setSkybox(Skybox skybox) {
		this.skybox = skybox;
	}

	public void addObserver(WorldUpdateObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(WorldUpdateObserver o) {
		observers.remove(o);
	}
	
	public void addObject(WorldObject o) {
		if (o instanceof VisibleObject) {
			visibleObjects.add((VisibleObject) o);
		}
		worldObjects.add(o);
	}
	
	public void removeObject(WorldObject o) {
		if (o instanceof VisibleObject) {
			visibleObjects.remove(o);
		}
		worldObjects.remove(o);
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
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}
	
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
		for (WorldUpdateObserver o : observers) {
			o.worldStarted(this);
		}
		double fpsTime = Timer.getTime();
		boolean wasPaused = isPaused();
		timeSinceLastUpdate = Timer.getTime();
		while (isRunning) {
			if (!isPaused) {
				if (wasPaused) {
					for (WorldUpdateObserver o : observers) {
						o.worldResumed(this);
					}
					for (WorldObject o : worldObjects) {
						o.worldResumed(this);
					}
					wasPaused = false;
				}
				updateObjects();
				notifyObservers();
				timeSinceLastUpdate = Timer.getTime();
			}
			else if (!wasPaused) {
				for (WorldUpdateObserver o : observers) {
					o.worldPaused(this);
				}
				for (WorldObject o : worldObjects) {
					o.worldPaused(this);
				}
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
			if(Display.wasResized()) {
				glViewport(0, 0, Display.getWidth(), Display.getHeight());
				viewPoint.updatePerspectiveMatrix();

			}
			Display.sync(targetFPS);
			double currentTime = Timer.getTime();
			fps = (1 / (currentTime - fpsTime) + fps) / 2;
			fpsTime = currentTime;
			InputHandler.checkInput();
			if (Display.isCloseRequested()) {
				stop();
			}
		}
		for (WorldUpdateObserver o : observers) {
			o.worldClosed(this);
		}
		tearDownGL();
	}
	
	private void updateObjects() {
		for (WorldObject o : worldObjects) {
			o.prepareToUpdate(Timer.getTime() - timeSinceLastUpdate);
		}
		viewPoint.prepareToUpdate(Timer.getTime() - timeSinceLastUpdate);

		for (WorldObject o : worldObjects) {
			o.update();
		}
		viewPoint.update();
	}
	
	private void drawSkybox() {
		Shader shader = skybox.getShader();
		glUseProgram(shader.getProgram());
		Matrix4f viewMatrix = new Matrix4f();
	    viewMatrix.rotX(-viewPoint.getOrientation().x);
	    viewMatrix.rotY(-viewPoint.getOrientation().y);
	    viewMatrix.rotZ(-viewPoint.getOrientation().z);
	    Matrix4f modelViewMatrix;
		Texture texture;
		Model model;
		Matrix4f projectionMatrix = viewPoint.getProjectionMatrix();

		model = skybox.getFront();
	    
	    modelViewMatrix = new Matrix4f(viewMatrix);
	    modelViewMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    modelViewMatrix.mul(projectionMatrix, modelViewMatrix);
		modelViewMatrix.store(modelViewProjectionBuffer);
		modelViewProjectionBuffer.flip();
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);

		texture = model.getTexture();
		if (texture != null) {
			texture.bind();
			glUniform1i(shader.getTextureUniformLocation(), 0);
		}
		glBindVertexArray(model.getVertexArray());
		glBindBuffer(GL_ARRAY_BUFFER, model.getBuffer());
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
		
	    model = skybox.getLeft();
	    
	    modelViewMatrix = new Matrix4f(viewMatrix);
	    modelViewMatrix.rotY(MathF.PI_OVER_2);
	    modelViewMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    modelViewMatrix.mul(projectionMatrix, modelViewMatrix);

		modelViewMatrix.store(modelViewProjectionBuffer);
		modelViewProjectionBuffer.flip();
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
		
		texture = model.getTexture();
		if (texture != null) {
			texture.bind();
			glUniform1i(shader.getTextureUniformLocation(), 0);
		}
		glBindVertexArray(model.getVertexArray());
		glBindBuffer(GL_ARRAY_BUFFER, model.getBuffer());
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	 
	    model = skybox.getBack();

	    modelViewMatrix = new Matrix4f(viewMatrix);
	    modelViewMatrix.rotY(MathF.PI);
	    modelViewMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    modelViewMatrix.mul(projectionMatrix, modelViewMatrix);

		modelViewMatrix.store(modelViewProjectionBuffer);
		modelViewProjectionBuffer.flip();
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
		
		texture = model.getTexture();
		if (texture != null) {
			texture.bind();
			glUniform1i(shader.getTextureUniformLocation(), 0);
		}
		glBindVertexArray(model.getVertexArray());
		glBindBuffer(GL_ARRAY_BUFFER, model.getBuffer());
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	 
	    model = skybox.getRight();

	    modelViewMatrix = new Matrix4f(viewMatrix);
	    modelViewMatrix.rotY(-MathF.PI_OVER_2);
	    modelViewMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    modelViewMatrix.mul(projectionMatrix, modelViewMatrix);

		modelViewMatrix.store(modelViewProjectionBuffer);
		modelViewProjectionBuffer.flip();
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
		
		texture = model.getTexture();
		if (texture != null) {
			texture.bind();
			glUniform1i(shader.getTextureUniformLocation(), 0);
		}
		glBindVertexArray(model.getVertexArray());
		glBindBuffer(GL_ARRAY_BUFFER, model.getBuffer());
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	 
	    model = skybox.getTop();

	    modelViewMatrix = new Matrix4f(viewMatrix);
	    modelViewMatrix.translate(new Vector3f(-.5f,.5f,.5f));
	    modelViewMatrix.rotX(-MathF.PI_OVER_2);
	    modelViewMatrix.mul(projectionMatrix, modelViewMatrix);

		modelViewMatrix.store(modelViewProjectionBuffer);
		modelViewProjectionBuffer.flip();
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
		
		texture = model.getTexture();
		if (texture != null) {
			texture.bind();
			glUniform1i(shader.getTextureUniformLocation(), 0);
		}
		glBindVertexArray(model.getVertexArray());
		glBindBuffer(GL_ARRAY_BUFFER, model.getBuffer());
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	 
	    model = skybox.getBottom();
	    if (model != null) {

		    modelViewMatrix = new Matrix4f(viewMatrix);
		    modelViewMatrix.rotX(MathF.PI_OVER_2);
		    modelViewMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
		    modelViewMatrix.mul(projectionMatrix, modelViewMatrix);

			modelViewMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
			
			texture = model.getTexture();
			if (texture != null) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
			glBindVertexArray(model.getVertexArray());
			glBindBuffer(GL_ARRAY_BUFFER, model.getBuffer());
			glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	    }
}

	private FloatBuffer modelViewProjectionBuffer = BufferUtils.createFloatBuffer(16);
	private FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9);
	
	private void drawFrame() {
		
		glActiveTexture(GL_TEXTURE0);

		Matrix4f viewMatrix = viewPoint.getTransformationMatrix();
		
		Matrix4f modelViewProjectionMatrix;
		Matrix3f normalMatrix;
		Model model;
		Shader shader;
		Texture texture;

		for (VisibleObject o : visibleObjects) {			
			model = o.getModel();
			shader = o.getShader();
			if (model != null && shader != null) {
				modelViewProjectionMatrix = o.getTransformationMatrix();
				modelViewProjectionMatrix.mul(viewMatrix, modelViewProjectionMatrix);
				if (viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector())) {
					normalMatrix = new Matrix3f();
					modelViewProjectionMatrix.get(normalMatrix);
					normalMatrix.invert();
					normalMatrix.transpose();
					
					modelViewProjectionMatrix.mul(viewPoint.getProjectionMatrix(), modelViewProjectionMatrix);
					
					glUseProgram(shader.getProgram());
					
					modelViewProjectionMatrix.store(modelViewProjectionBuffer);
					modelViewProjectionBuffer.flip();
					glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
					normalMatrix.store(normalBuffer);
					normalBuffer.flip();
					glUniformMatrix3(shader.getNormalMatrixUniformLocation(), false, normalBuffer);
					
					texture = model.getTexture();
					if (texture != null) {
						texture.bind();
						glUniform1i(shader.getTextureUniformLocation(), 0);
					}
					glBindVertexArray(model.getVertexArray());
					glBindBuffer(GL_ARRAY_BUFFER, model.getBuffer());

					glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
				}
			}
		}
	}
	

	
	private void notifyObservers() {
		
		for (WorldUpdateObserver o : observers) {
			o.worldUpdated(this, Timer.getTime() - timeSinceLastUpdate);
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
	
}

