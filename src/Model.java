
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Model {
	
	private final String MODELDATA_PATH = "Resources/Models/";
	
	private FloatBuffer data;

	private int vertexCount;
	private int normalCount;
	private int textureCoordinateCount;

	private int vertexArray;
	private int buffer;
		
	private Texture texture;
	
	public Model(String name) {
		name = MODELDATA_PATH + name;
		try {
			loadModelDataForName(name + ".modeldata");
			
			vertexArray = glGenVertexArrays();
			glBindVertexArray(vertexArray);
			
			buffer = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, buffer);
			glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
			
			glEnableVertexAttribArray(Shader.POSITION_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(Shader.NORMAL_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(Shader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION);
			
			glVertexAttribPointer(Shader.POSITION_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 32, 0);
			glVertexAttribPointer(Shader.NORMAL_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 32, 16);
			glVertexAttribPointer(Shader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION, 2, GL_FLOAT, false, 32, 24);
		}
		catch (FileNotFoundException e) {
		     System.err.println("Error: Model file not found");
		} 
		catch (IOException e) {
		     System.err.println("Error: IOException when loading model");
		}
		
		try {
			texture = TextureLoader.getTexture(name + ".jpg");
		} catch (IOException e) {
			
			System.err.println("Error: Texture file could not be opened\n" + e.toString());
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

	
	private void loadModelDataForName(String name) throws FileNotFoundException, IOException {

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

	public int getTextureCoordinateCount() {
		return textureCoordinateCount;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public int getNormalCount() {
		return normalCount;
	}
}
