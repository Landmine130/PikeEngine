package world;


public class VisibleObject extends CollisionObject implements Comparable {

	private Shader shader;
	private Model model;
	
	private float opacity;
	
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
	
	public void prepareToUpdate(double timeElapsed) {
		
	}
	
	public void update() {

	}

	@Override
	public int compareTo(Object o) {
		int retValue = 0;
		VisibleObject v = (VisibleObject) o;
		retValue = shader.getProgram() - v.shader.getProgram() << 4;
		retValue += model.getVertexArray() - v.model.getVertexArray();
		retValue <<= 10;
		int myTexID = 0, oTexID = 0;
		
		if (model.getTexture() != null) {
			myTexID = model.getTexture().getID();
		}
		if (v.model.getTexture() != null) {
			oTexID = v.model.getTexture().getID();
		}
		retValue += myTexID - oTexID;
		retValue <<= 10;
		
		retValue += (byte) (hashCode() - v.hashCode());
		if (retValue == 0 && !(this == v)) {
			retValue = 1;
		}
		
		return retValue;
	}
}
