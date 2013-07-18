package world;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import vecmath.Vector3d;

public class PhysicsModel extends Model {
	
    private static HashMap<String, PhysicsModel> loadedModels = new HashMap<String, PhysicsModel>();

	private double mass;
	private Vector3d momentOfInertia;
	
	public static PhysicsModel getModel(String name, boolean textured) {
		name = MODELDATA_PATH + name;
		PhysicsModel m = loadedModels.get(name);
		if (m == null) {
			m = new PhysicsModel(name, textured);
			loadedModels.put(name, m);
		}
		m = new PhysicsModel(m);
		return m;
	}
	
	public PhysicsModel(String path, boolean textured) {
		super(path, textured);
		
	}

	public PhysicsModel(ByteBuffer data, Texture texture, int vertexCount) {
		super(data, texture, vertexCount);
		
	}

	public PhysicsModel(PhysicsModel m) {
		super(m);
		mass = m.mass;
		momentOfInertia = new Vector3d(m.momentOfInertia);
	}

	public PhysicsModel(byte[] data, Texture texture, int vertexCount) {
		super(data, texture, vertexCount);
		
	}
	
	protected String getExtension() {
		return ".physicsmodeldata";
	}
	
	public double getMass() {
		return mass;
	}
	
	public Vector3d getMomentOfInertia() {
		return momentOfInertia;
	}
	
	protected static final int DOUBLE_BYTE_COUNT = FLOAT_BYTE_COUNT * 2;
	protected static final int HEADER_SIZE = 3 * FLOAT_BYTE_COUNT + 4 * DOUBLE_BYTE_COUNT;
	
	protected void loadModelDataForName(String name) throws FileNotFoundException, IOException {
		File file = new File(name);
		int dataSize = (int)file.length() - HEADER_SIZE;
		int floatDataSize = dataSize / FLOAT_BYTE_COUNT;
		
		DataInputStream reader = new DataInputStream(new FileInputStream(file));
		
		vertexCount = reader.readInt();
		reader.readInt();
		reader.readInt();
		
		mass = reader.readDouble();
		momentOfInertia = new Vector3d(reader.readDouble(), reader.readDouble(), reader.readDouble());
		
		data = BufferUtils.createByteBuffer(dataSize);
		
		for (int i = 0; i < floatDataSize; i++) {
			data.putFloat(reader.readFloat());
		}
		data.flip();
		reader.close();
	}
}
