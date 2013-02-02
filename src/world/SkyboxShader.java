package world;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class SkyboxShader extends Shader {
	
	public static final SkyboxShader skyboxShader = new SkyboxShader();
	
	
	protected String vertexShaderName() {
		return "SkyboxShader.vsh";
	}
	
	protected String fragmentShaderName() {
		return "SkyboxShader.fsh";
	}
	
	protected void bindVariableLocations(int program) {
		
		glBindAttribLocation(program, POSITION_ATTRIBUTE_LOCATION, "position");
		glBindAttribLocation(program, TEXTURE_COORDINATE_ATTRIBUTE_LOCATION, "textureCoordinateIn");
	}
	
	protected void getVariableLocations(int program) {
		modelViewProjectionMatrixUniformLocation = glGetUniformLocation(program, "modelViewProjectionMatrix");
		textureUniformLocation = glGetUniformLocation(program, "textureSampler");
	}
}
