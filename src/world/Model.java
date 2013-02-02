package world;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

import vecmath.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Model {

    protected static HashMap<String, Model> loadedModels = new HashMap<String, Model>();
	
	public static final String MODELDATA_PATH = "Resources/Models/";
	
	protected FloatBuffer data;

	protected int vertexCount;
	protected int normalCount;
	protected int textureCoordinateCount;

	protected int vertexArray;
	protected int buffer;
		
	protected Texture texture;
	
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
		if (loadedModels.remove(name) != null) {
			System.gc();
		}
	}
	
	public Model(String path, boolean textured) {
		try {
			loadModelDataForName(path + ".modeldata");
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
	
	public Model(FloatBuffer data, Texture texture, int vertexCount, int normalCount, int textureCoordinateCount) {
		this.data = data;
		this.texture = texture;
		this.vertexCount = vertexCount;
		this.normalCount = normalCount;
		this.textureCoordinateCount = textureCoordinateCount;
		init();
	}
	
	public Model(Model m) {
		this.buffer = m.buffer;
		this.data = m.data;
		this.vertexArray = m.vertexArray;
		this.vertexCount = m.vertexCount;
		this.normalCount = m.normalCount;
		this.textureCoordinateCount = m.textureCoordinateCount;
		this.texture = m.texture;
		this.radius = m.radius;
	}
	
	public Model(float[] data, Texture texture, int vertexCount, int normalCount, int textureCoordinateCount) {
		this.data = BufferUtils.createFloatBuffer(data.length);
		this.data.put(data);
		this.data.flip();
		this.texture = texture;
		this.vertexCount = vertexCount;
		this.normalCount = normalCount;
		this.textureCoordinateCount = textureCoordinateCount;
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

	public int getBuffer() {
		return buffer;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	
	protected void loadModelDataForName(String name) throws FileNotFoundException, IOException {
		File file = new File(name);
		int dataSize = (int)(file.length() / 4) - 3;
		
		DataInputStream reader = new DataInputStream(new FileInputStream(file));
		
		vertexCount = reader.readInt();
		normalCount = reader.readInt();
		textureCoordinateCount = reader.readInt();
		
		data = BufferUtils.createFloatBuffer(dataSize);

		for (int i = 0; i < dataSize; i++) {
			data.put(reader.readFloat());
		}
		data.flip();
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

	public int getTextureCoordinateCount() {
		return textureCoordinateCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getNormalCount() {
		return normalCount;
	}
	
	public FloatBuffer getData() {
		return data;
	}
	
	public float getRadius() {
		return radius;
	}
}
