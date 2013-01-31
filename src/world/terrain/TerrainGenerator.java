package world.terrain;

import world.WorldObject;
import world.VisibleObject;
import vecmath.Vector3f;
import vecmath.Vector3i;

import java.util.ArrayList;

class TerrainGenerator {
	
	private long seed;
	private ArrayList<TerrainFeature> terrainFeatures = new ArrayList<TerrainFeature>();
	
	
	public TerrainGenerator(long seed) {
		this.seed = seed;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public Chunk generateChunk(Vector3i size) {
		Chunk chunk = new Chunk(size);
		
		for (int i = 0; i < chunk.getPosition().x; i++) {
			for (int j = 0; j < chunk.getPosition().z; j++) {
				
				WorldObject o = new VisibleObject("banana");
				o.setPosition(new Vector3f(i, 0, j));
				
				Vector3i position = new Vector3i(i,0,j);
				chunk.set(o, position);
			}
		}
		
		return chunk;
	}
}
