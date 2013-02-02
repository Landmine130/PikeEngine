package world;

public class Skybox {
	
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
}
