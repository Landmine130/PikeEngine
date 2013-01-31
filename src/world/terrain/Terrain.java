package world.terrain;

import java.util.HashMap;
import java.util.MissingResourceException;
import vecmath.Vector3i;
import world.WorldObject;

public class Terrain {
	
	private long seed;
	private final int CHUNK_LOAD_DISTANCE = 20;
	private HashMap<Vector3i, Chunk> loadedChunks = new HashMap<Vector3i, Chunk>(CHUNK_LOAD_DISTANCE * 6);
	private final int CHUNK_SIZE = 16;
	private TerrainGenerator generator;
	
	public Terrain(long seed) {
		this.seed = seed;
		generator = new TerrainGenerator(seed);
	}
	
	public long getSeed() {
		return seed;
	}
	
	public WorldObject get(Vector3i position) {
		
		Vector3i chunkCoordinate = new Vector3i(position);
		chunkCoordinate.div(CHUNK_SIZE);
		
		Vector3i blockCoordinate = new Vector3i(position);
		blockCoordinate.mod(CHUNK_SIZE);
		blockCoordinate.abs();
		
		Chunk chunk = loadedChunks.get(chunkCoordinate);
		
		if (chunk == null) {
			
			throw new MissingResourceException("Unloaded chunk at " + position.x + ", " + position.y + ", " + position.z + " was read", "Chunk", position.x + ", " + position.y + ", " + position.z);
		}
		
		return chunk.get(blockCoordinate);
	}
	
	public void set(WorldObject o, Vector3i position) {
		
		Vector3i chunkCoordinate = new Vector3i(position);
		chunkCoordinate.div(CHUNK_SIZE);
		
		Vector3i blockCoordinate = new Vector3i(position);
		blockCoordinate.mod(CHUNK_SIZE);
		blockCoordinate.abs();

		Chunk chunk = loadedChunks.get(chunkCoordinate);
		
		if (chunk == null) {
			
			throw new MissingResourceException("Unloaded chunk at " + position.x + ", " + position.y + ", " + position.z + " was written", "Chunk", position.x + ", " + position.y + ", " + position.z);
		}
		
		chunk.set(o, blockCoordinate);
	}
	
	public void load(Vector3i chunkPosition) {
		loadedChunks.put(chunkPosition, generator.generateChunk(new Vector3i(CHUNK_SIZE, CHUNK_SIZE, CHUNK_SIZE)));
	}
	
	public void unload(Vector3i chunkPosition) {
		loadedChunks.remove(chunkPosition);
		System.gc();
	}
}