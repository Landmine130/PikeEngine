package world;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;


public class SkyboxModel extends Model {
	
	private static float[] squareData = {0f,0f,0f,
										1f,1f,
										0f,1f,0f,
										1f,0f,
										1f,1f,0f,
										0f,0f,
										1f,1f,0f,
										0f,0f,
										1f,0f,0f,
										0f,1f,
										0f,0f,0f,
										1f,1f};
	private static int vertexCount = 6;

	private static SkyboxModel square = new SkyboxModel((Texture) null);

	
	public static SkyboxModel getModel(Texture texture) {
		SkyboxModel m = new SkyboxModel(square);
		m.setTexture(texture);
		return m;
	}
	
	private static ByteBuffer encode (float floatArray[]) { 

		ByteBuffer byteBuffer = BufferUtils.createByteBuffer(floatArray.length * 4); 
		FloatBuffer floatBuffer = byteBuffer.asFloatBuffer(); 
		floatBuffer.put(floatArray); 
		return byteBuffer; 
		} 
	
	public SkyboxModel(Texture t) {
		super(encode(squareData), t, vertexCount);
		
	}
	
	public SkyboxModel(SkyboxModel m) {
		super(m);
	}
	
	protected void init() {
		try {
			vertexArray = glGenVertexArrays();
			glBindVertexArray(vertexArray);
			
			buffer = glGenBuffers();
			glBindBuffer(GL_ARRAY_BUFFER, buffer);
			glBufferData(GL_ARRAY_BUFFER, data, GL_STATIC_DRAW);
			
			glEnableVertexAttribArray(Shader.POSITION_ATTRIBUTE_LOCATION);
			glEnableVertexAttribArray(Shader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION);
			
			glVertexAttribPointer(Shader.POSITION_ATTRIBUTE_LOCATION, 3, GL_FLOAT, false, 20, 0);
			glVertexAttribPointer(Shader.TEXTURE_COORDINATE_ATTRIBUTE_LOCATION, 2, GL_FLOAT, false, 20, 12);
		}
		catch (Exception e) {
			
		}
	}
}
