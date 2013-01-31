package world;


public class VisibleObject extends CollisionObject {

	private Shader shader;
	private Model model;
	
	private float opacity;
	
	public VisibleObject(String modelName) {
		this.shader = new Shader();
		Model model = new Model(modelName);
		this.model = model;
		opacity = 1f;
	}
	
	public VisibleObject(String modelName, Shader shader) {
		Model model = new Model(modelName);
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
}
