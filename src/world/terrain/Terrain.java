package world.terrain;

import java.util.HashMap;
import java.util.HashSet;

import vecmath.Vector3i;
import world.VisibleObject;

public class Terrain {
	
	private long seed;
	private HashMap<Vector3i, VisibleObject> blockMap = new HashMap<Vector3i, VisibleObject>();
	private HashSet<Vector3i> loadedBlocks = new HashSet<Vector3i>();

	private TerrainGenerator generator;
	
	public Terrain(long seed) {
		this.seed = seed;
		generator = new TerrainGenerator(seed);
	}
	
	public long getSeed() {
		return seed;
	}
	
	public VisibleObject get(Vector3i position) {
		
		VisibleObject o = null;
		Vector3i tmp = new Vector3i(position);
		if (loadedBlocks.contains(tmp)) {
			o = blockMap.get(tmp);
		}
		else {
			o = load(tmp);
		}
		return o;
	}
	
	public void set(VisibleObject o) {
		
		Vector3i position = o.getPosition().toVector3i();
		blockMap.put(position, o);
	}
	
	private VisibleObject load(Vector3i position) {
		VisibleObject o = generator.generate(position);
		blockMap.put(position, o);
		loadedBlocks.add(position);
		return o;
	}
	
	public void unload(Vector3i position) {
		blockMap.remove(position);
		loadedBlocks.remove(position);
	}
	
	public boolean isLoaded(Vector3i position) {
		return blockMap.get(position) != null;
	}
}