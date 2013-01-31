package world.terrain;

import vecmath.Vector3i;
import vecmath.Vector3f;
import world.VisibleObject;
import world.WorldObject;

class SurfaceTerrainFeature extends TerrainFeature {

	public SurfaceTerrainFeature(Vector3i position, long seed) {
		super(position, seed);
	}

	public void addFeature(Chunk chunk) {
		
		Vector3i offset = new Vector3i();
		offset.add(getPosition(), chunk.getPosition());
		
		int[][] heightMap = generateHeightMap(chunk.getSize().x, chunk.getSize().z);
		
		for (int i = 0; i < heightMap.length; i++) {
			for (int j = 0; j < heightMap[i].length; j++) {
				
				WorldObject o = new VisibleObject("banana");
				int y = heightMap[i][j];
				o.setPosition(new Vector3f(i, y, j));
				chunk.set(o, i, y, j);
			}
		}
	}
	
	private int[][] generateHeightMap(int width, int depth) {
		int[][] heightMap = new int[width][depth];
		
		
		
		return heightMap;
	}
}
