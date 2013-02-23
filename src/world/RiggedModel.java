package world;

import java.nio.FloatBuffer;
import java.util.HashMap;

public class RiggedModel extends Model {

    private static HashMap<String, RiggedModel> loadedModels = new HashMap<String, RiggedModel>();
	
	public static RiggedModel getRiggedModel(String name, boolean textured) {
		name = MODELDATA_PATH + name;
		RiggedModel m = loadedModels.get(name);
		if (m == null) {
			m = new RiggedModel(name, textured);
			loadedModels.put(name, m);
		}
		else {
			m = new RiggedModel(m);
		}
		return m;
	}
	
	
	private HashMap<String, Animation> animations = new HashMap<String, Animation>();
	
	
	public RiggedModel(String path, boolean textured) {
		super(path, textured);
	}

	public RiggedModel(FloatBuffer data, Texture texture, int vertexCount,
			int normalCount, int textureCoordinateCount) {
		super(data, texture, vertexCount, normalCount, textureCoordinateCount);
	}

	public RiggedModel(Model m) {
		super(m);
	}

	public RiggedModel(float[] data, Texture texture, int vertexCount,
			int normalCount, int textureCoordinateCount) {
		super(data, texture, vertexCount, normalCount, textureCoordinateCount);
	}

}
