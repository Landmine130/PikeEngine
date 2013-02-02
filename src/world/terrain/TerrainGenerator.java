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
	
	public Chunk generateChunk(Vector3i position) {
		Chunk chunk = new Chunk(position);
		
		for (int i = 0; i < Chunk.CHUNK_SIZE; i++) {
			for (int j = 0; j < Chunk.CHUNK_SIZE; j++) {
				
				WorldObject o = new VisibleObject("cube");
				o.setPosition(new Vector3f(i + position.x * Chunk.CHUNK_SIZE, 0, j + position.z * Chunk.CHUNK_SIZE));
				world.addObject(o);
				Vector3i blockPosition = new Vector3i(i,0,j);
				chunk.set(o, blockPosition);
			}
		}
		
		return chunk;
	}
}
