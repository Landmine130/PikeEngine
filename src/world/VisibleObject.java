package world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import vecmath.Matrix3f;
import vecmath.Matrix4f;


public class VisibleObject extends WorldObject implements Drawable {

	private volatile Shader shader;
	private volatile Model model;
	
	private volatile float opacity;
	
	public VisibleObject(String modelName) {
		this.shader = Shader.shader;
		Model model = Model.getModel(modelName, true);
		this.model = model;
		opacity = 1f;
	}
	
	public VisibleObject(String modelName, Shader shader) {
		Model model = Model.getModel(modelName, true);
		this.model = model;
		this.shader = shader;
		opacity = 1f;
	}
	
	public VisibleObject(Model model, Shader shader) {
		this.model = model;
		this.shader = shader;
		opacity = 1f;
	}
	
	public Shader getShader() {
		return shader;
	}
	
	public void setShader(Shader shader) {
		this.shader = shader;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof VisibleObject)) {
			return false;
		}
		VisibleObject v = (VisibleObject) o;
		return model == v.model && shader == v.shader && position.equals(v.position) && orientation.equals(v.orientation) && opacity == v.opacity;
	}

	protected FloatBuffer modelViewProjectionBuffer = BufferUtils.createFloatBuffer(16);
	protected FloatBuffer normalBuffer = BufferUtils.createFloatBuffer(9);
	protected boolean inView = true;
	
	public void prepareToDrawInWorld(World world) {
		ViewPoint viewPoint = world.getViewPoint();
		
		Matrix4f modelViewProjectionMatrix = getTransformationMatrix();
		modelViewProjectionMatrix.mul(viewPoint.getTransformationMatrix(), modelViewProjectionMatrix);
		inView = viewPoint.isSphereInView(model.getRadius(), modelViewProjectionMatrix.translationVector());
		if (inView) {
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
		
		if (lastDrawn == null || !lastDrawn.isLastBound(shaderProgram, idType.shader)) {
			glUseProgram(shaderProgram);
		}
		
		glUniformMatrix4(shader.getModelViewProjectionMatrixUniformLocation(), false, modelViewProjectionBuffer);
		glUniformMatrix3(shader.getNormalMatrixUniformLocation(), false, normalBuffer);
		
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
		
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
	}
	
	public boolean isLastBound(int id, idType type) {
		switch (type) {
		
		case arrayBuffer:
			return id == model.getArrayBuffer();
		case shader:
			return id == shader.getProgram();
		case texture:
			Texture texture = model.getTexture();
			if (texture != null) {
				return id == model.getTexture().getID();
			}
			else {
				return false;
			}
		case vertexArray:
			return id == model.getVertexArray();
		default:
			return false;
		}
	}
	
/*	public int compareTo(Object o) {
		int retValue = 0;
		VisibleObject v = (VisibleObject) o;
		retValue = (shader.getProgram() - v.shader.getProgram()) * 1000000;
		retValue += (model.getVertexArray() - v.model.getVertexArray()) * 10000;
		int myTexID = 0, oTexID = 0;
		
		if (model.getTexture() != null) {
			myTexID = model.getTexture().getID();
		}
		if (v.model.getTexture() != null) {
			oTexID = v.model.getTexture().getID();
		}
		retValue += (myTexID - oTexID) * 100;
		
		retValue += position.epsilonEquals(v.position, 0.00001f)?0:1;
		retValue += orientation.epsilonEquals(v.orientation, 0.00001f)?0:1;
		if (retValue == 0 && this != v) {
			retValue = 1;
		}
		
		return retValue;
	}*/
}
