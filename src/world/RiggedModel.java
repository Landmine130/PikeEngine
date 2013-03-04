package world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;

public class RiggedModel extends Model {

    private static final HashMap<String, RiggedModel> loadedModels = new HashMap<String, RiggedModel>();
	
	public static final String ELEMENT_PATH_EXTENSION = ".elements";
	public static final int WEIGHTS_PER_VERTEX = 4;

	public static RiggedModel getRiggedModel(String modelName, String animationName, boolean textured) {
		modelName = MODELDATA_PATH + modelName;
		RiggedModel m = loadedModels.get(modelName);
		if (m == null) {
			m = new RiggedModel(modelName, textured, Animation.getAnimationForName(animationName));
			loadedModels.put(modelName, m);
		}
		else {
			m = new RiggedModel(m);
		}
		return m;
	}
	
	public static void addLoadedModel(String path, RiggedModel m) {
		loadedModels.put(path, m);
	}
	
	public static void unloadModel(String name) {
		name = MODELDATA_PATH + name;
		loadedModels.remove(name);
	}
	
	protected static String getExtension() {
		return ".riggedmodeldata";
	}
	
	
	private volatile Animation animation = null;
	private ByteBuffer elementData;
	private int elementCount;
	private int elementBuffer;
	
	public RiggedModel(String path, boolean textured, Animation a) {
		super(path, textured);
		animation = a;
	}

	public RiggedModel(ByteBuffer data, ByteBuffer elementData, Texture texture, int vertexCount, int elementCount, Animation a) {
		super(data, texture, vertexCount);
		this.elementData = elementData;
		this.elementCount = elementCount;
		animation = a;
	}

	public RiggedModel(RiggedModel m) {
		super(m);
		elementData = m.elementData;
		elementCount = m.elementCount;
		elementBuffer = m.elementBuffer;
		animation = new Animation(m.animation);
	}

	public RiggedModel(byte[] data, byte[] elementData, Texture texture, int vertexCount, int elementCount, Animation a) {
		super(data, texture, vertexCount);
		this.elementData = BufferUtils.createByteBuffer(elementData.length);
		this.elementData.put(elementData);
		this.elementData.flip();
		this.elementCount = elementCount;
		animation = a;
	}
	
	public void setAnimation(Animation a) {
		animation = a;
	}
	
	public void setAnimation(String name) {
		animation = Animation.getAnimationForName(name);
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public int getElementCount() {
		return elementCount;
	}
	
	public int getElementArrayBuffer() {
		return elementBuffer;
	}
	
	public float getRadius() {
		return animation.getRadius();
	}
	
	protected void init() {
		try {
			vertexArray = glGenVertexArrays();
			glBindVertexArray(vertexArray);
			
			buffer = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, buffer);
			glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
			
			glEnableVertexAttribArray(RiggedShader.WEIGHT1_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(RiggedShader.WEIGHT2_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(RiggedShader.WEIGHT3_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(RiggedShader.WEIGHT4_ATTRIBUTE_LOCATION);
			
			glEnableVertexAttribArray(RiggedShader.BONE_INDEX_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(RiggedShader.BIAS_ATTRIBUTE_LOCATION);

			glEnableVertexAttribArray(RiggedShader.NORMAL_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(RiggedShader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION);
			
			glVertexAttribPointer(RiggedShader.WEIGHT1_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 88, 0);
			glVertexAttribPointer(RiggedShader.WEIGHT2_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 88, 12);
			glVertexAttribPointer(RiggedShader.WEIGHT3_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 88, 24);
			glVertexAttribPointer(RiggedShader.WEIGHT4_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 88, 36);

			glVertexAttribPointer(RiggedShader.BONE_INDEX_ATTRIBUTE_LOCATION, 4, GL_BYTE, false, 88, 48);
			glVertexAttribPointer(RiggedShader.BIAS_ATTRIBUTE_LOCATION, 4, GL_FLOAT, false, 88, 52);

			glVertexAttribPointer(RiggedShader.NORMAL_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 88, 68);
			glVertexAttribPointer(RiggedShader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION, 2, GL_FLOAT, false, 88, 80);
			
			elementBuffer = glGenBuffers();
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
			glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementData, GL_STATIC_DRAW);
			
			//glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
			glBindBuffer(GL_ARRAY_BUFFER, 0);
			glBindVertexArray(0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final int FLOAT_BYTE_COUNT = Float.SIZE / Byte.SIZE;
	private static final int SHORT_BYTE_COUNT = Short.SIZE / Byte.SIZE;
	private static final int HEADER_SIZE = 3 * FLOAT_BYTE_COUNT;
	
	private static final int ELEMENT_HEADER_SIZE = 1 * FLOAT_BYTE_COUNT;
	
	protected void loadModelDataForName(String name) throws FileNotFoundException, IOException {
		File file = new File(name);

		int dataSize = (int)file.length() - HEADER_SIZE;
		
		DataInputStream reader = new DataInputStream(new FileInputStream(file));
		
		vertexCount = reader.readInt();
		
		data = BufferUtils.createByteBuffer(dataSize);
		
		for (int i = 0; i < vertexCount; i++) {
			// Weights
			for (int j = 0; j < WEIGHTS_PER_VERTEX; j++) {
				data.putFloat(reader.readFloat());
				data.putFloat(reader.readFloat());
				data.putFloat(reader.readFloat());
			}
			// Bone indexes
			for (int j = 0; j < WEIGHTS_PER_VERTEX; j++) {
				data.put(reader.readByte());
			}
			// Biases
			for (int j = 0; j < WEIGHTS_PER_VERTEX; j++) {
				data.putFloat(reader.readFloat());
			}
			// Normal
			data.putFloat(reader.readFloat());
			data.putFloat(reader.readFloat());
			data.putFloat(reader.readFloat());
			// Texture Coordinates
			data.putFloat(reader.readFloat());
			data.putFloat(reader.readFloat());
		}
		
		data.flip();
		reader.close();
		
		File elementFile = new File(name + ELEMENT_PATH_EXTENSION);
		DataInputStream elementReader = new DataInputStream(new FileInputStream(elementFile));
		
		int elementDataSize = (int)elementFile.length() - ELEMENT_HEADER_SIZE;
		int elementShortDataSize = elementDataSize / SHORT_BYTE_COUNT;
		
		elementData = BufferUtils.createByteBuffer(elementDataSize);
		
		elementCount = elementReader.readInt();
		
		for (int i = 0; i < elementShortDataSize; i++) {
			elementData.putShort(elementReader.readShort());
		}
		elementData.flip();
		elementReader.close();
	}
}
