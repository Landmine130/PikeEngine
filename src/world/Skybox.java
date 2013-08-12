package world;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniformMatrix4;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import misc.MathF;
import vecmath.Matrix4d;
import vecmath.Matrix4f;
import vecmath.Vector3f;
import world.Drawable.idType;

public class Skybox implements Drawable {
	
	private static final String SKYBOX_PATH = "Resources/Skybox/"; 

	private Model[] models = new Model[6];
	private Shader shader;
	public Skybox() {
		shader = SkyboxShader.skyboxShader;

		try {
			models[0] = new SkyboxModel(TextureLoader.getTexture(SKYBOX_PATH + "skybox_front.jpg"));
			models[1] = new SkyboxModel(TextureLoader.getTexture(SKYBOX_PATH + "skybox_left.jpg"));
			models[2] = new SkyboxModel(TextureLoader.getTexture(SKYBOX_PATH + "skybox_back.jpg"));
			models[3] = new SkyboxModel(TextureLoader.getTexture(SKYBOX_PATH + "skybox_right.jpg"));
			models[4] = new SkyboxModel(TextureLoader.getTexture(SKYBOX_PATH + "skybox_top.jpg"));
		}
		catch (Exception e) {
			System.err.println("Error: Skybox could not be loaded");
		}
		try {
			models[5] = SkyboxModel.getModel(TextureLoader.getTexture(SKYBOX_PATH + "skybox_bottom.jpg"));
		}
		catch (Exception e) {
			
		}
	}
	
	public Skybox(Texture[] textures) {
		shader = SkyboxShader.skyboxShader;
		models[0] = SkyboxModel.getModel(textures[0]);
		models[1] = SkyboxModel.getModel(textures[1]);
		models[2] = SkyboxModel.getModel(textures[2]);
		models[3] = SkyboxModel.getModel(textures[3]);
		models[4] = SkyboxModel.getModel(textures[4]);
		models[5] = SkyboxModel.getModel(textures[5]);

	}
	
	public Skybox(Texture[] textures, Shader shader) {
		this.shader = shader;
		models[0] = SkyboxModel.getModel(textures[0]);
		models[1] = SkyboxModel.getModel(textures[1]);
		models[2] = SkyboxModel.getModel(textures[2]);
		models[3] = SkyboxModel.getModel(textures[3]);
		models[4] = SkyboxModel.getModel(textures[4]);
		models[5] = SkyboxModel.getModel(textures[5]);
	}
	
	public Model[] getModels() {
		return models;
	}
	
	public Model getFront() {
		return models[0];
	}
	
	public Model getLeft() {
		return models[1];
	}
	
	public Model getBack() {
		return models[2];
	}
	
	public Model getRight() {
		return models[3];
	}
	
	public Model getTop() {
		return models[4];
	}
	
	public Model getBottom() {
		return models[5];
	}
	
	public Shader getShader() {
		return shader;
	}

	private FloatBuffer modelViewProjectionBuffer = BufferUtils.createFloatBuffer(16);
	
	public void prepareToDrawInWorld(World world) {
		
	}
	
	public void drawInWorld(World world, Drawable lastDrawn) {
		
		ViewPoint viewPoint = world.getViewPoint();
		glUseProgram(shader.getProgram());
		
		Matrix4d viewMatrix = viewPoint.getTransformationMatrix();
		Matrix4f viewMatrixF = new Matrix4f();
		viewMatrixF.set(viewMatrix);
	   viewMatrixF.setTranslation(Vector3f.ZERO_VECTOR);
	    
	    Matrix4f modelViewProjectionMatrix;
		Texture texture;
		Model model;
		Matrix4f projectionMatrix = viewPoint.getProjectionMatrix();
		
		model = getFront();
		
		glBindVertexArray(model.getVertexArray());
		glBindBuffer(GL_ARRAY_BUFFER, model.getArrayBuffer());
		

	    modelViewProjectionMatrix = new Matrix4f(viewMatrixF);
	    modelViewProjectionMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    
		//if (viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector())) {
		
			modelViewProjectionMatrix.mul(projectionMatrix, modelViewProjectionMatrix);
			modelViewProjectionMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
	
			texture = model.getTexture();
			if (texture != null) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
	
			glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
		//}
		
	    model = getLeft();
	    
	    modelViewProjectionMatrix = new Matrix4f(viewMatrixF);
	    modelViewProjectionMatrix.rotY(MathF.PI_OVER_2);
	    modelViewProjectionMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    
		//if (viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector())) {
	
		    modelViewProjectionMatrix.mul(projectionMatrix, modelViewProjectionMatrix);
	
			modelViewProjectionMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
			
			texture = model.getTexture();
			if (texture != null) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
	
			glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
		//}
		
	    model = getBack();

	    modelViewProjectionMatrix = new Matrix4f(viewMatrixF);
	    modelViewProjectionMatrix.rotY(MathF.PI);
	    modelViewProjectionMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    
		//if (viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector())) {
	
		    modelViewProjectionMatrix.mul(projectionMatrix, modelViewProjectionMatrix);
	
			modelViewProjectionMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
			
			texture = model.getTexture();
			if (texture != null) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
			
			glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
		//}
		
	    model = getRight();

	    modelViewProjectionMatrix = new Matrix4f(viewMatrixF);
	    modelViewProjectionMatrix.rotY(-MathF.PI_OVER_2);
	    modelViewProjectionMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
	    
		//if (viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector())) {
	
		    modelViewProjectionMatrix.mul(projectionMatrix, modelViewProjectionMatrix);
	
			modelViewProjectionMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
			
			texture = model.getTexture();
			if (texture != null) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
	
			glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
		//}
		
	    model = getBottom();
	    if (model != null) {

		    modelViewProjectionMatrix = new Matrix4f(viewMatrixF);
		    modelViewProjectionMatrix.rotX(MathF.PI_OVER_2);
		    modelViewProjectionMatrix.translate(new Vector3f(-.5f,-.5f,.5f));
		    
			//if (viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector())) {
	
			    modelViewProjectionMatrix.mul(projectionMatrix, modelViewProjectionMatrix);
	
				modelViewProjectionMatrix.store(modelViewProjectionBuffer);
				modelViewProjectionBuffer.flip();
				glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
				
				texture = model.getTexture();
				if (texture != null) {
					texture.bind();
					glUniform1i(shader.getTextureUniformLocation(), 0);
				}
	
				glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
			//}
	    }
			
	    model = getTop();

	    modelViewProjectionMatrix = new Matrix4f(viewMatrixF);
	    modelViewProjectionMatrix.translate(new Vector3f(-.5f,.5f,.5f));
	    modelViewProjectionMatrix.rotX(-MathF.PI_OVER_2);
		
	    //if (viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector())) {
	
		    modelViewProjectionMatrix.mul(projectionMatrix, modelViewProjectionMatrix);
	
			modelViewProjectionMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
			
			texture = model.getTexture();
			if (texture != null) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
	
			glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	    //}
	}
	
	public boolean needsDrawn() {
		return true;
	}
	
	public boolean isLastBound(int id, idType type) {
		switch (type) {
		
		case arrayBuffer:
			return id == getTop().getArrayBuffer();
		case shader:
			return id == shader.getProgram();
		case texture:
			return id == getTop().getTexture().getID();
		case vertexArray:
			return id == getTop().getVertexArray();
		default:
			return false;
			
		}
	}
}
