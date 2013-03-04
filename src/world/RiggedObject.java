package world;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.GL_SHORT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vecmath.Matrix3f;
import vecmath.Matrix4f;
import world.Drawable.idType;


public class RiggedObject extends WorldObject implements Drawable {	
	
	private RiggedShader shader;
	private RiggedModel model;
	private float opacity;
	
	public RiggedObject(String modelName) {
		this.shader = RiggedShader.riggedShader;
		RiggedModel model = RiggedModel.getRiggedModel(modelName, modelName, true);
		this.model = model;
		opacity = 1f;
	}
	
	public RiggedObject(String modelName, RiggedShader shader) {
		RiggedModel model = RiggedModel.getRiggedModel(modelName, modelName, true);
		this.model = model;
		this.shader = shader;
		opacity = 1f;
	}
	
	public RiggedObject(RiggedModel model, RiggedShader shader) {
		this.model = model;
		this.shader = shader;
		opacity = 1f;
	}
	
	public RiggedShader getShader() {
		return shader;
	}
	
	public void setShader(RiggedShader shader) {
		this.shader = shader;
	}

	public RiggedModel getModel() {
		return model;
	}

	public void setModel(RiggedModel model) {
		this.model = model;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof RiggedObject)) {
			return false;
		}
		RiggedObject v = (RiggedObject) o;
		return model == v.model && shader == v.shader && position.equals(v.position) && orientation.equals(v.orientation) && opacity == v.opacity;
	}
	
	
	private boolean inView;
	protected FloatBuffer modelViewProjectionBuffer = BufferUtils.createFloatBuffer(16);
	protected FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9);
	protected FloatBuffer jointBuffer;
	
	public void prepareToDrawInWorld(World world) {
		
		ViewPoint viewPoint = world.getViewPoint();
		
		Matrix4f modelViewProjectionMatrix = getTransformationMatrix();
		modelViewProjectionMatrix.mul(viewPoint.getTransformationMatrix(), modelViewProjectionMatrix);
		
		Animation animation = model.getAnimation();
		float animationTime = animation.getTime();
		
		inView = viewPoint.isSphereInView(animation.getRadius(animationTime), modelViewProjectionMatrix.translationVector());
		if (inView) {
			
			int boneCount = animation.getBoneCount();
			jointBuffer = BufferUtils.createFloatBuffer(16 * boneCount);
			
			if (animation != null) {
				
				Matrix4f[] jointPositions = animation.getJointPositions(animationTime);
				
				for (int i = 0; i < boneCount; i++) {
					jointPositions[i].store(jointBuffer);
				}
				jointBuffer.flip();
			}
			
			Matrix3f normalMatrix = new Matrix3f();
			modelViewProjectionMatrix.get(normalMatrix);
			normalMatrix.invert();
			normalMatrix.transpose();
			
			modelViewProjectionMatrix.mul(viewPoint.getProjectionMatrix(), modelViewProjectionMatrix);
			
			modelViewProjectionMatrix.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			normalMatrix.store(normalBuffer);
			normalBuffer.flip();
		}
	}
	
	public boolean needsDrawn() {
		return inView;
	}
	
	public void drawInWorld(World world, Drawable lastDrawn) {
		
		int shaderProgram = shader.getProgram();
		int vertexArray = model.getVertexArray();
		int arrayBuffer = model.getArrayBuffer();
		int elementArrayBuffer = model.getElementArrayBuffer();
		
		if (lastDrawn == null || !lastDrawn.isLastBound(shaderProgram, idType.shader)) {
			glUseProgram(shaderProgram);
		}
		
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
		glUniformMatrix3(shader.getNormalMatrixUniformLocation(), false, normalBuffer);
		glUniform1(shader.getJointArrayUniformLocation(), jointBuffer);
		
		Texture texture = model.getTexture();
		if (texture != null) {
			if (lastDrawn == null || !lastDrawn.isLastBound(texture.getID(), idType.texture)) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
		}

		if (lastDrawn == null || !lastDrawn.isLastBound(vertexArray, idType.vertexArray)) {
			glBindVertexArray(vertexArray);
		}
					
		if (lastDrawn == null || !lastDrawn.isLastBound(arrayBuffer, idType.arrayBuffer)) {
			glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
		}
		
		if (lastDrawn == null || !lastDrawn.isLastBound(elementArrayBuffer, idType.elementArrayBuffer)) {
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
		}
		
		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_SHORT, 0);
	}

	public boolean isLastBound(int id, idType type) {
		return false;
	}
}
