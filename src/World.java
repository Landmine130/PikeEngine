
import java.awt.Window;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import vecmath.Matrix3f;
import vecmath.Matrix4f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL32.*;

public class World {
	
	private ViewPoint viewPoint;
	
	private final int GL_MAJOR_VERSION = 3;
	private final int GL_MINOR_VERSION = 2;
	
	private ArrayList<WorldUpdateObserver> observers;
	private ArrayList<VisibleObject> objects;
	
	private volatile boolean isRunning;
	private volatile boolean isPaused;
	
	private final int TARGET_FPS = 60;
	
	private SoundManager soundManager;
	
	private final int INITIAL_WIDTH = 800;
	private final int INITIAL_HEIGHT = 450;
	
	private int alphaBufferPrecision = 8;
	private int depthBufferPrecision = 24;
	private int stencilBufferPrecision = 0;

	private boolean fullscreen;
	
	private Matrix4f projectionMatrix;

	private double timeSinceLastUpdate;
	
	public World() {
		
		observers = new ArrayList<WorldUpdateObserver>();
		objects = new ArrayList<VisibleObject>();

		viewPoint = new ViewPoint();
		
		soundManager = new SoundManager();
		soundManager.initialize(8);
		
		fullscreen = false;
		setPaused(false);
		
		try {
			//setDisplayMode();
			Display.setTitle("Game");
			Display.setFullscreen(fullscreen);
			Display.setDisplayMode(new DisplayMode(INITIAL_WIDTH, INITIAL_HEIGHT));
			PixelFormat pixelFormat = new PixelFormat(alphaBufferPrecision, depthBufferPrecision, stencilBufferPrecision);
			ContextAttribs contextAtrributes = new ContextAttribs(GL_MAJOR_VERSION, GL_MINOR_VERSION).withProfileCore(true).withForwardCompatible(true);			 
			Display.create(pixelFormat, contextAtrributes);
			Display.setResizable(true);
			Mouse.setGrabbed(true);
			isRunning = true;
			
		} catch (LWJGLException le) {
			System.out.println("Game exiting - exception during initialization:");
			le.printStackTrace();
			isRunning = false;
			return;
		}
		
		glEnable(GL_CULL_FACE);
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);

		glClearColor(0.4f, 0.6f, 0.9f, 0f);
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_ONE, GL_ALPHA);
		glEnable(GL_TEXTURE_2D);
	    glEnable(GL_DEPTH_TEST);
	    glDepthMask(true);
		glDepthFunc(GL_LEQUAL);
		glDepthRange(0.0f, 1.0f);
	}

	public void addObserver(WorldUpdateObserver o) {
		observers.add(o);
	}
	
	public void removeObserver(WorldUpdateObserver o) {
		observers.remove(o);
	}
	
	public void addObject(VisibleObject o) {
		objects.add(o);
	}
	
	public void removeObject(VisibleObject o) {
		objects.remove(o);
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
	      System.out.println("Unable to enter fullscreen, continuing in windowed mode");
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

		timeSinceLastUpdate = Timer.getTime();
		while (isRunning) {
			glViewport(0, 0, Display.getWidth(), Display.getHeight());
			if (!isPaused) {
				updateObjects();
				notifyObservers();
				timeSinceLastUpdate = Timer.getTime();
			}
			drawFrame();
			Display.update();
			Display.sync(TARGET_FPS);
			InputHandler.checkInput();
			if (Display.isCloseRequested()) {
				isRunning = false;
			}
		}
		tearDownGL();
	}
	
	private void updateObjects() {
		for (VisibleObject o : objects) {
			o.prepareToUpdate(Timer.getTime() - timeSinceLastUpdate);
		}
		viewPoint.prepareToUpdate(Timer.getTime() - timeSinceLastUpdate);
		
		for (VisibleObject o : objects) {
			o.update();
		}
		viewPoint.update();
	}
	
	private void drawFrame() {
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glActiveTexture(GL_TEXTURE0);
		
		float aspectRatio = Display.getWidth() / (float)Display.getHeight();
		projectionMatrix = CreateLeftHandedPerspective(viewPoint.getFieldOfView(), aspectRatio, viewPoint.getNearViewDistance(), viewPoint.getFarViewDistance());
		Matrix4f viewMatrix = viewPoint.getTransformationMatrix();
		
		Matrix4f modelViewProjectionMatrix;
		Matrix4f modelViewMatrix;
		Matrix4f modelMatrix;
		Matrix3f normalMatrix;
		FloatBuffer modelViewProjectionBuffer;
		FloatBuffer normalBuffer;
		
		for (VisibleObject o : objects) {
			modelMatrix = o.getTransformationMatrix();
			modelViewMatrix = new Matrix4f();
			modelViewMatrix.mul(viewMatrix, modelMatrix);
			
			normalMatrix = new Matrix3f();
			modelViewMatrix.get(normalMatrix);
			normalMatrix.invert();
			normalMatrix.transpose();
			
			modelViewProjectionMatrix = new Matrix4f();
			modelViewProjectionMatrix.mul(projectionMatrix, modelViewMatrix);
			
			glUseProgram(o.getShader().getProgram());
			
			modelViewProjectionBuffer = BufferUtils.createFloatBuffer(16);
			normalBuffer = BufferUtils.createFloatBuffer(9);
			
			modelViewProjectionMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			glUniformMatrix4(o.getShader().getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
			normalMatrix.store(normalBuffer);
			normalBuffer.flip();
			glUniformMatrix3(o.getShader().getNormalMatrixUniformLocation(), false, normalBuffer);
			
			o.getModel().getTexture().bind();
			glUniform1i(o.getShader().getTextureUniformLocation(), 0);
			
			glBindVertexArray(o.getModel().getVertexArray());
			
			glBindBuffer(GL_ARRAY_BUFFER, o.getModel().getBuffer());
			
			
			//System.out.println(modelViewProjectionMatrix);
			
			// Bind to the index VBO that has all the information about the order of the vertices
			//glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, o.getModel().getElementBuffer());
			
			// Draw the vertices
			//glDrawElements(GL_TRIANGLES, o.getModel().getSize(), GL_UNSIGNED_BYTE, 0);
			glDrawArrays(GL_TRIANGLES, 0, o.getModel().getVertexCount());
		}
	}
	
	private void notifyObservers() {
		
		for (WorldUpdateObserver o : observers) {
			o.worldUpdated(this, Timer.getTime() - timeSinceLastUpdate);
		}
	}
	
	private Matrix4f CreateLeftHandedPerspective(float fov, float aspect, float zNear, float zFar) {
	    float top = zNear * (float) Math.tan(fov * MathF.PI_OVER_360);
	    float bottom = -top;
	    float right = top * aspect;
	    float left = bottom * aspect;
	    
	    /*return new Matrix4f(2 * zNear / (right - left),	0,0, 0,
				0, 2 * zNear / (top - bottom), 0, 0,
				0, 0, -(zFar + zNear) / (zFar - zNear), -1,
				0, 0, (-2 * zFar * zNear) / (zFar - zNear), 0);*/
	    
		Matrix4f retVal = new Matrix4f(2 * zNear / (right - left),	0,0, 0,
							0, 2 * zNear / (top - bottom), 0, 0,
							0, 0, (zFar + zNear) / (zFar - zNear), (2 * zFar * zNear) / (zFar - zNear),
							0, 0, -1, 0);
		retVal.transpose();
		return retVal;
	}

	public void setViewPoint(ViewPoint viewPoint) {

		this.viewPoint = viewPoint;
	}
	
}

