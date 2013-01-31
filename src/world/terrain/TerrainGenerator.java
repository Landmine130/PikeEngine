package world.terrain;

import world.World;
import world.WorldObject;
import world.VisibleObject;
import vecmath.Vector3f;
import vecmath.Vector3i;

import java.util.ArrayList;

class TerrainGenerator {
	
	private long seed;
	private ArrayList<TerrainFeature> terrainFeatures = new ArrayList<TerrainFeature>();
	private World world;
	
	public TerrainGenerator(long seed, World world) {
		this.seed = seed;
		this.world = world;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public Chunk generateChunk(Vector3i size, Vector3i position) {
		Chunk chunk = new Chunk(size, position);
		
		for (int i = 0; i < chunk.getSize().x; i++) {
			for (int j = 0; j < chunk.getSize().z; j++) {
				
				WorldObject o = new VisibleObject("banana");
				o.setPosition(new Vector3f(i, 0, j));
				world.addObject(o);
				Vector3i blockPosition = new Vector3i(i,0,j);
				chunk.set(o, blockPosition);
			}
		}
		
		return chunk;
	}
}
