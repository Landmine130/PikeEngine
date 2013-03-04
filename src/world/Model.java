package world;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Model {

    private static HashMap<String, Model> loadedModels = new HashMap<String, Model>();
	
	public static final String MODELDATA_PATH = "Resources/Models/";
	public static final String MODELDATA_PATH_EXTENSION = ".modeldata";
	protected ByteBuffer data;

	protected int vertexCount;

	protected int vertexArray;
	protected int buffer;
		
	protected volatile Texture texture;
	
	protected float radius = -1;
	
	public static Model getModel(String name, boolean textured) {
		name = MODELDATA_PATH + name;
		Model m = loadedModels.get(name);
		if (m == null) {
			m = new Model(name, textured);
			loadedModels.put(name, m);
		}
		else {
			m = new Model(m);
		}
		return m;
	}
	
	public static void unloadModel(String name) {
		name = MODELDATA_PATH + name;
		loadedModels.remove(name);
	}
	
	protected static String getExtension() {
		return ".modeldata";
	}
	
	public Model(String path, boolean textured) {
		try {
			loadModelDataForName(path + getExtension());
		} 
		catch(FileNotFoundException e) {
		     System.err.println("Error: Model file not found");
		} 
		catch (IOException e) {
		     System.err.println("Error: IOException when loading model");
		}
		if (textured) {
			try {
				texture = TextureLoader.getTexture(path + ".jpg");
			} 
			catch (Exception e) {
			
				System.err.println("Error: Texture could not be loaded");
			}
		}
		init();
	}
	
	public Model(ByteBuffer data, Texture texture, int vertexCount) {
		this.data = data;
		this.texture = texture;
		this.vertexCount = vertexCount;
		init();
	}
	
	public Model(Model m) {
		this.buffer = m.buffer;
		this.data = m.data;
		this.vertexArray = m.vertexArray;
		this.vertexCount = m.vertexCount;
		this.texture = m.texture;
		this.radius = m.radius;
	}
	
	public Model(byte[] data, Texture texture, int vertexCount) {
		this.data = BufferUtils.createByteBuffer(data.length);
		this.data.put(data);
		this.data.flip();
		this.texture = texture;
		this.vertexCount = vertexCount;
		init();
	}
	
	protected void init() {
		try {
			if (radius < 0) {
				radius = calculateRadius();
			}
			vertexArray = glGenVertexArrays();
			glBindVertexArray(vertexArray);
			
			buffer = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, buffer);
			glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
			
			glEnableVertexAttribArray(Shader.POSITION_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(Shader.NORMAL_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(Shader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION);
			
			glVertexAttribPointer(Shader.POSITION_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 32, 0);
			glVertexAttribPointer(Shader.NORMAL_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 32, 12);
			glVertexAttribPointer(Shader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION, 2, GL_FLOAT, false, 32, 24);
		}
		catch (Exception e) {
			
		}
		
	}
	
	public int getVertexArray() {
		return vertexArray;
	}

	public int getArrayBuffer() {
		return buffer;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	private static final int FLOAT_BYTE_COUNT = Float.SIZE / Byte.SIZE;
	private static final int HEADER_SIZE = 3 * FLOAT_BYTE_COUNT;
	
	protected void loadModelDataForName(String name) throws FileNotFoundException, IOException {
		File file = new File(name);
		int dataSize = (int)file.length() - HEADER_SIZE;
		int floatDataSize = dataSize / FLOAT_BYTE_COUNT;
		
		DataInputStream reader = new DataInputStream(new FileInputStream(file));
		
		vertexCount = reader.readInt();
		reader.readInt();
		reader.readInt();
		
		data = BufferUtils.createByteBuffer(dataSize);
		
		for (int i = 0; i < floatDataSize; i++) {
			data.putFloat(reader.readFloat());
		}
		data.flip();
		reader.close();
	}
	
	private float calculateRadius() {
		float max = Float.MIN_VALUE;
		Vector3f vertex = new Vector3f();
		float length;
		while (data.hasRemaining()) {
			vertex.set(data.get(), data.get(), data.get());
			length = vertex.length();
			if (length > max) {
				max = length;
			}
			data.position(data.position() + 5);
		}
		data.flip();
		return max;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public ByteBuffer getData() {
		return data;
	}
	
	public float getRadius() {
		return radius;
	}
}
