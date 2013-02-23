package world;


public class RiggedObject extends WorldObject {

	private RiggedShader shader;
	private RiggedModel model;
	private float opacity;
	
	public RiggedObject(String modelName) {
		this.shader = RiggedShader.animatedShader;
		RiggedModel model = RiggedModel.getRiggedModel(modelName, true);
		this.model = model;
		opacity = 1f;
	}
	
	public RiggedObject(String modelName, RiggedShader shader) {
		RiggedModel model = RiggedModel.getRiggedModel(modelName, true);
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
	
	public void drawInWorld(World world) {
		
	}
}
