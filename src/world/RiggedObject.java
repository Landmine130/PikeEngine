package world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import vecmath.Matrix3f;
import vecmath.Matrix4d;
import vecmath.Matrix4f;


public class RiggedObject extends WorldObject implements Drawable {	
	
	private RiggedShader shader;
	private volatile RiggedModel[] models;
	private volatile Animation animation;
	private float opacity;
	
	public RiggedObject(String modelName) {
		this.shader = RiggedShader.riggedShader;
		models = RiggedModel.getRiggedModels(modelName, true);
		animation = Animation.getAnimationForName(modelName);
		opacity = 1f;
	}
	
	public RiggedObject(String modelName, RiggedShader shader) {
		models = RiggedModel.getRiggedModels(modelName, true);
		animation = Animation.getAnimationForName(modelName);
		this.shader = shader;
		opacity = 1f;
	}
	
	public RiggedObject(RiggedModel[] models, RiggedShader shader, Animation a) {
		this.models = models;
		this.shader = shader;
		animation = a;
		opacity = 1f;
	}
	
	public RiggedShader getShader() {
		return shader;
	}
	
	public void setShader(RiggedShader shader) {
		this.shader = shader;
	}

	public RiggedModel[] getModel() {
		return models;
	}

	public void setModels(RiggedModel[] models) {
		this.models = models;
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public void setAnimation(Animation animation) {
		this.animation = animation;
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
		return models == v.models && shader == v.shader && position.equals(v.position) && orientation.equals(v.orientation) && opacity == v.opacity;
	}
	
	
	private boolean inView;
	protected FloatBuffer modelViewProjectionBuffer = BufferUtils.createFloatBuffer(16);
	protected FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9);
	protected FloatBuffer jointBuffer = BufferUtils.createFloatBuffer(16 * 64);
	
	public void prepareToDrawInWorld(World world) {
		
		ViewPoint viewPoint = world.getViewPoint();
		
		Matrix4d modelViewProjectionMatrix = getTransformationMatrix();
		modelViewProjectionMatrix.mul(viewPoint.getTransformationMatrix(), modelViewProjectionMatrix);
		
		Animation animation = this.animation;
		
		double animationTime = animation.getTime();
		
		Matrix4f modelViewProjectionMatrixF = new Matrix4f(modelViewProjectionMatrix);
		
		inView = viewPoint.isSphereInView(animation.getRadius(animationTime), modelViewProjectionMatrixF.translationVector());
		if (inView) {
			
			int boneCount = animation.getBoneCount();
			
			if (animation != null) {
				
				Matrix4f[] jointPositions = animation.getJointPositions(animationTime);

				
				for (int i = 0; i < boneCount; i++) {
					jointPositions[i].store(jointBuffer);
				}
				jointBuffer.flip();
			}
						
			Matrix3f normalMatrix = new Matrix3f();
			modelViewProjectionMatrixF.get(normalMatrix);
			normalMatrix.invert();
			normalMatrix.transpose();
			
			modelViewProjectionMatrixF.mul(viewPoint.getProjectionMatrix(), modelViewProjectionMatrixF);
			
			modelViewProjectionMatrixF.store(modelViewProjectionBuffer);
			modelViewProjectionBuffer.flip();
			normalMatrix.store(normalBuffer);
			normalBuffer.flip();
		}
	}
	
	public boolean needsDrawn() {
		return inView;
	}
	
	public void drawInWorld(World world, Drawable lastDrawn) {

		RiggedModel[] models = this.models;

		int shaderProgram = shader.getProgram();
		
		if (lastDrawn == null || !lastDrawn.isLastBound(shaderProgram, idType.shader)) {
			glUseProgram(shaderProgram);
		}
		
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
		glUniformMatrix3(shader.getNormalMatrixUniformLocation(), false, normalBuffer);
		glUniformMatrix4(shader.getJointArrayUniformLocation(), false, jointBuffer);
		
		for (int i = 0; i < models.length; i++) {
			
			RiggedModel model = models[i];
			
			int vertexArray = model.getVertexArray();
			int arrayBuffer = model.getArrayBuffer();
			int elementArrayBuffer = model.getElementArrayBuffer();
			

	
			Texture texture = model.getTexture();
			if (texture != null) {
				texture.bind();
				glUniform1i(shader.getTextureUniformLocation(), 0);
			}
	
			glBindVertexArray(vertexArray);
			glBindBuffer(GL_ARRAY_BUFFER, arrayBuffer);
			glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementArrayBuffer);
	
			glDrawElements(GL_TRIANGLES, model.getElementCount(), GL_UNSIGNED_SHORT, 0);
		}
		
	}

	public boolean isLastBound(int id, idType type) {
		return false;
	}
}
