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
	
	public TerrainGenerator(long seed) {
		this.seed = seed;
	}
	
	public long getSeed() {
		return seed;
	}
	
	public VisibleObject generate(Vector3i position) {
		VisibleObject o = null;
		if (position.y == 0) {
			o = new VisibleObject("cube");
			o.setPosition(position);
		}
		return o;
	}
}
