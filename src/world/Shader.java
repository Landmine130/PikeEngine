package world;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Shader {
	
	public static final String shaderFilePath = "Resources/Shaders/";
	
	public static final Shader shader = new Shader();
	
	private int program;
	
	protected int modelViewProjectionMatrixUniformLocation;
	protected int normalMatrixUniformLocation;
	protected int textureUniformLocation;
	
	public static final int POSITION_ATTRIBUTE_LOCATION = 0;
	public static final int NORMAL_ATTRIBUTE_LOCATION = 1;
	public static final int TEXTURE_COORDINATE_ATTRIBUTE_LOCATION = 2;
	
	public Shader() {
		glGetError();
		
		int vertexShader = loadShader(shaderFilePath + vertexShaderName(), GL_VERTEX_SHADER);
		int fragmentShader = loadShader(shaderFilePath + fragmentShaderName(), GL_FRAGMENT_SHADER);
		
		program = glCreateProgram();
		glAttachShader(program, vertexShader);
		glAttachShader(program, fragmentShader);
		 
		bindVariableLocations(program);
		 		
		glLinkProgram(program);
		
		getVariableLocations(program);

		glValidateProgram(program);
		
		int errorValue = glGetError();
		if (errorValue != GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("Error: Could not create shader program:\n" + errorValue + "\n" + errorString);
		}
	}
	
	protected String vertexShaderName() {
		return "shader.vsh";
	}
	
	protected String fragmentShaderName() {
		return "shader.fsh";
	}
	
	protected void bindVariableLocations(int program) {
		
		glBindAttribLocation(program, POSITION_ATTRIBUTE_LOCATION, "position");
		glBindAttribLocation(program, NORMAL_ATTRIBUTE_LOCATION, "normal");
		glBindAttribLocation(program, TEXTURE_COORDINATE_ATTRIBUTE_LOCATION, "textureCoordinateIn");
	}
	
	protected void getVariableLocations(int program) {
		modelViewProjectionMatrixUniformLocation = glGetUniformLocation(program, "modelViewProjectionMatrix");
		normalMatrixUniformLocation = glGetUniformLocation(program, "normalMatrix");
		textureUniformLocation = glGetUniformLocation(program, "textureSampler");
	}
	
	public int getProgram() {
		return program;
	}
	
	public void setProgram(int program) {
		this.program = program;
	}

	public int getModelViewProjectionMatrixUniformLocation() {
		return modelViewProjectionMatrixUniformLocation;
	}

	public int getNormalMatrixUniformLocation() {
		return normalMatrixUniformLocation;
	}
	
	public int getTextureUniformLocation() {
		return textureUniformLocation;
	}

	private int loadShader(String filename, int type) {
		StringBuilder shaderSource = new StringBuilder();
		int shaderID;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line;
			while ((line = reader.readLine()) != null) {
			shaderSource.append(line).append("\n");
			}
			reader.close();
		} catch (IOException e) {
			System.err.println("Could not read file.");
			e.printStackTrace();
		}
		 
		shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		 
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			String info = glGetShaderInfoLog(shaderID, 100);
			System.err.println("Could not compile shader.\n" + info);
			System.exit(-1);
		}		 
		return shaderID;
	}
}
