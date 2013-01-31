package world.terrain;

import vecmath.Vector3i;
import world.WorldObject;

public class Chunk {

	private Vector3i position;
	
	private WorldObject[][][] objects;
	
	private Vector3i size;
	
	public Chunk(Vector3i size) {
		objects = new WorldObject[size.x][size.y][size.z];
		this.size = size;
	}
	
	public WorldObject get(Vector3i position) {
		return objects[position.x][position.y][position.z];
	}
	
	public WorldObject get(int x, int y, int z) {
		return objects[x][y][z];
	}
	
	public void set(WorldObject o, Vector3i position) {
		objects[position.x][position.y][position.z] = o;
	}
	
	public void set(WorldObject o, int x, int y, int z) {
		objects[x][y][z] = o;
	}
	
	public Vector3i getSize() {
		return size;
	}
	
	public Vector3i getPosition() {
		return position;
	}
}
