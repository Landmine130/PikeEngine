package world;

public interface Drawable {
	
	/**
	 * Called before drawInWorld on a background thread to do any non OpenGL processing needed for rendering.
	 * @param world the world this object is preparing to draw in
	 */
	public void prepareToDrawInWorld(World world);
	
	/**
	 * Called on main thread for doing all OpenGL calls. Any non-OpenGL processing should occur in prepareToDrawInWorld
	 * @param world the world this object is drawing in
	 * @param lastDrawn the Drawable that was most recently drawn in world
	 */
	public void drawInWorld(World world, Drawable lastDrawn);
	
	/**
	 * Called on main thread after prepareToDrawInWorld.
	 * @return whether or not drawInWorld should be called
	 */
	public boolean needsDrawn();
	
	/**
	 * determines if an OpenGL object was bound when this object last finished drawing.
	 * @param id the OpenGl id of the object
	 * @param type the type of OpenGL object
	 * @return whether or not the OpenGL object was bound when this object last finished drawing
	 */
	public boolean isLastBound(int id, idType type);
	
	public enum idType {
		shader, 
		texture, 
		vertexArray, 
		arrayBuffer,
		elementArrayBuffer
	}
}
