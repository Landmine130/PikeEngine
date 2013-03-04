package world;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class RiggedShader extends Shader {

	public static final RiggedShader riggedShader = new RiggedShader();
	
	public static final int WEIGHT1_ATTRIBUTE_LOCATION = 3;
	public static final int WEIGHT2_ATTRIBUTE_LOCATION = 4;
	public static final int WEIGHT3_ATTRIBUTE_LOCATION = 5;
	public static final int WEIGHT4_ATTRIBUTE_LOCATION = 6;
	public static final int BONE_INDEX_ATTRIBUTE_LOCATION = 7;
	public static final int BIAS_ATTRIBUTE_LOCATION = 8;

	protected int jointArrayUniformLocation;
	
	protected void bindVariableLocations(int program) {
		
		glBindAttribLocation(program, WEIGHT1_ATTRIBUTE_LOCATION, "weight1");
		glBindAttribLocation(program, WEIGHT2_ATTRIBUTE_LOCATION, "weight2");		
		glBindAttribLocation(program, WEIGHT3_ATTRIBUTE_LOCATION, "weight3");
		glBindAttribLocation(program, WEIGHT4_ATTRIBUTE_LOCATION, "weight4");
		glBindAttribLocation(program, BONE_INDEX_ATTRIBUTE_LOCATION, "jointIndexes");
		glBindAttribLocation(program, BIAS_ATTRIBUTE_LOCATION, "biases");
		glBindAttribLocation(program, NORMAL_ATTRIBUTE_LOCATION, "bindNormal");
		glBindAttribLocation(program, TEXTURE_COORDINATE_ATTRIBUTE_LOCATION, "textureCoordinateIn");
	}
	
	protected void getVariableLocations(int program) {
		modelViewProjectionMatrixUniformLocation = glGetUniformLocation(program, "modelViewProjectionMatrix");
		normalMatrixUniformLocation = glGetUniformLocation(program, "normalMatrix");
		jointArrayUniformLocation = glGetUniformLocation(program, "joints");
		textureUniformLocation = glGetUniformLocation(program, "textureSampler");
	}
	
	public int getJointArrayUniformLocation() {
		return jointArrayUniformLocation;
	}

}
