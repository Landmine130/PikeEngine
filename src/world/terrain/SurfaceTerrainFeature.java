package world.terrain;

import vecmath.Vector3i;
import vecmath.Vector3f;
import world.VisibleObject;
import world.WorldObject;

class SurfaceTerrainFeature extends TerrainFeature {

	public SurfaceTerrainFeature(Vector3i position, long seed) {
		super(position, seed);
	}

	public void addFeature(WorldObject object) {
		
		Vector3i offset = getPosition();
		Vector3i position = object.getPosition().toVector3i();
		offset.add(position);
		
		int height = getHeight(offset);
		WorldObject o = new VisibleObject("cube");
		o.setPosition(new Vector3f(position.x, height, position.z));
	}
	
	private int getHeight(Vector3i offset) {
		int height = 0;
		
		
		
		return height;
	}
}
