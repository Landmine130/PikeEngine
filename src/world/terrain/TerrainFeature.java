package world.terrain;

import world.WorldObject;
import vecmath.Vector3i;

class TerrainFeature {
	
	private Vector3i position;
	
	private long seed;
	
	public TerrainFeature(Vector3i position, long seed) {
		this.position = position;
		this.seed = seed;
	}
	
	public Vector3i getPosition() {
		return position;
	}
	
	public void addFeature(Chunk chunk) {
		Vector3i offset = new Vector3i();
		offset.add(position, chunk.getPosition());
		
	}
	
	
}
