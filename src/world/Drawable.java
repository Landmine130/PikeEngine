package world;

public interface Drawable {
	public void prepareToDrawInWorld(World world);
	public void drawInWorld(World world, Drawable lastDrawn);
	public boolean needsDrawn();
	public boolean isLastBound(int id, idType type);
	
	public enum idType {
		shader, 
		texture, 
		vertexArray, 
		arrayBuffer
	}
}
