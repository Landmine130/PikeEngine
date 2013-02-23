package misc;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import vecmath.Quat4f;
import vecmath.Vector2f;
import vecmath.Vector3f;


public class Md5ToModeldata {

	/**
	 * @param args
	 */
	public static void convert(String filePath) throws Exception {

		int extensionStartIndex = filePath.lastIndexOf('.');
		String name = filePath.substring(0, extensionStartIndex);
		String extension = filePath.substring(extensionStartIndex);
		if (extension.equalsIgnoreCase(".md5mesh")) {
			
			String vertexOutName = name + ".vertexdata";
			String triangleOutName = name + ".triangledata";
			String jointOutName = name + ".jointdata";
			
			BufferedReader reader = null;
			DataOutputStream vertexWriter = null;
			DataOutputStream triangleWriter = null;
			DataOutputStream jointWriter = null;

			try {
				vertexWriter = new DataOutputStream(new FileOutputStream(vertexOutName));
				triangleWriter = new DataOutputStream(new FileOutputStream(triangleOutName));
				jointWriter = new DataOutputStream(new FileOutputStream(jointOutName));
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			}
			catch (Exception e) {
				System.err.println("In or out file could not be opened");
				e.printStackTrace();
				System.exit(1);
			}
			try {
				String version = nextLine(reader);
				if (version.contains("MD5Version ")) {
					if (!version.endsWith("10")) {
						System.err.println("Wrong Version: only MD5Version 10 is supported");
					}
				}
				else {
					error();
				}
				nextLine(reader);
				String numJointsString = nextLine(reader);
				if (!numJointsString.contains("numJoints ")) {
					error();
				}
				int numJoints = Integer.parseInt(wordNumber(2, numJointsString));
				System.out.println("numJoints: " + numJoints);
				String numMeshesString = nextLine(reader);
				if (!numMeshesString.contains("numMeshes ")) {
					error();
				}
				int numMeshes = Integer.parseInt(wordNumber(2, numMeshesString));
				System.out.println("numMeshes: " + numMeshes);
				
				String line = nextLine(reader);
				
				if (!wordNumber(1,line).contains("joints") || wordNumber(2, line).contains("{")) {
					error();
				}
				
				Joint[] joints = new Joint[numJoints];
				
				for (int j = 0; j < numJoints; j++) {
					line = nextLine(reader);
					
					String jointName = wordNumber(1, line);
					jointName = jointName.replace('"', ' ');
					jointName = jointName.trim();
					int parent = Integer.parseInt(wordNumber(2, line));
					float px = Float.parseFloat(wordNumber(4, line));
					float py = Float.parseFloat(wordNumber(5, line));
					float pz = Float.parseFloat(wordNumber(6, line));
					float ox = Float.parseFloat(wordNumber(9, line));
					float oy = Float.parseFloat(wordNumber(10, line));
					float oz = Float.parseFloat(wordNumber(11, line));
					float t = 1.0f - (ox * ox) - (oy * oy) - (oz * oz);
					float ow;
					if (t < 0.0f)
					{
						ow = 0.0f;
					}
					else
					{
						ow = -MathF.sqrt(t);
					}
					joints[j] = new Joint();
					joints[j].parent = parent;
					joints[j].name = jointName;
					joints[j].position = new Vector3f(px, py, pz);
					joints[j].orientation = new Quat4f(ox, oy, oz, ow);

				}
				
				Mesh[] meshes = new Mesh[numMeshes];
				
				nextLine(reader);
				for (int currentMesh = 0; currentMesh < numMeshes; currentMesh++) {
					meshes[currentMesh] = new Mesh();
					nextLine(reader);
					line = nextLine(reader);
					String textureFile = wordNumber(2, line);
					textureFile = textureFile.substring(1, textureFile.length());
					line = nextLine(reader);
					if (line.contains("numverts")) {
						int numVerts = Integer.parseInt(wordNumber(2, line));
						meshes[currentMesh].vertexes = new Vertex[numVerts];
						for (int k = 0; k < numVerts; k++) {
							line = nextLine(reader);
							int index = Integer.parseInt(wordNumber(2, line));
							float textX = Float.parseFloat(wordNumber(4, line));
							float textY = Float.parseFloat(wordNumber(5, line));
							meshes[currentMesh].vertexes[index] = new Vertex();
							meshes[currentMesh].vertexes[index].textureCoordinate = new Vector2f(textX, textY);
							meshes[currentMesh].vertexes[index].weightStartIndex = Integer.parseInt(wordNumber(7, line));
							meshes[currentMesh].vertexes[index].weights = new Weight[Integer.parseInt(wordNumber(8, line))];

						}
					}
					else {
						error();
					}
					line = nextLine(reader);
					if (line.contains("numtris")) {
						int numTris = Integer.parseInt(wordNumber(2, line));
						line = nextLine(reader);
						meshes[currentMesh].traingles = new Triangle[numTris];
						for (int k = 0; k < numTris; k++) {
							
							int index = Integer.parseInt(wordNumber(2, line));
							int v1 = Integer.parseInt(wordNumber(3, line));
							int v2 = Integer.parseInt(wordNumber(4, line));
							int v3 = Integer.parseInt(wordNumber(5, line));
							
							meshes[currentMesh].traingles[index] = new Triangle();
							meshes[currentMesh].traingles[index].v1 = v1;
							meshes[currentMesh].traingles[index].v2 = v2;
							meshes[currentMesh].traingles[index].v3 = v3;
						}
					}
					else {
						error();
					}
					Weight[] weights = null;
					line = nextLine(reader);
					if (line.contains("numweights")) {
						int numWeights = Integer.parseInt(wordNumber(2, line));
						weights = new Weight[numWeights];
						for (int k = 0; k < numWeights; k++) {
							line = nextLine(reader);
							
							int index = Integer.parseInt(wordNumber(2, line));
							int jointIndex = Integer.parseInt(wordNumber(3, line));
							float bias = Float.parseFloat(wordNumber(4, line));
							float px = Float.parseFloat(wordNumber(6, line));
							float py = Float.parseFloat(wordNumber(7, line));
							float pz = Float.parseFloat(wordNumber(8, line));

							weights[index] = new Weight();
							weights[index].jointIndex = jointIndex;
							weights[index].bias = bias;
							weights[index].position = new Vector3f(px, py, pz);
						}
					}
					else {
						error();
					}
					for (int k = 0; k < meshes[currentMesh].vertexes.length; k++) {
						for (int l = 0; l < meshes[currentMesh].vertexes[k].weights.length; l++) {
							meshes[currentMesh].vertexes[k].weights[l] = weights[meshes[currentMesh].vertexes[k].weightStartIndex + l];
						}
						
					}
					line = nextLine(reader);
					if (!line.contains("}")) {
						error();
					}
				}
				
				vertexWriter.close();
				triangleWriter.close();
				jointWriter.close();
			}
			catch (Exception e) {
				System.err.println("An error occurred");
				e.printStackTrace();
			}
		}
		if (extension.equalsIgnoreCase(".md5anim")) {
			
			String outName = name + ".animationdata";
			DataOutputStream writer = null;
			BufferedReader reader = null;
			try {
				writer = new DataOutputStream(new FileOutputStream(outName));
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			}
			catch (Exception e) {
				System.err.println("File could not be opened");
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			System.err.println("Input file is not of type md5mesh or md5anim");
			System.exit(2);
		}
	}
	
	private static String wordNumber(int target, String str) {
		
		int index = 0;
		
		for (int i = 1; index != -1; i++) {
			int nextSpaceIndex = str.indexOf(' ', index);
			int nextTabIndex = str.indexOf('\t', index);
			int nextIndex = (nextSpaceIndex > nextTabIndex)?nextSpaceIndex:nextTabIndex;
			String word = str.substring(index, nextIndex);
			if (word.startsWith(" ") || word.startsWith("\t")) {
				i--;
			}
			else if (i == target) {
				return word;
			}
			index = nextIndex + 1;
		}
		return null;
	}

	private static String nextLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		while (line != null) {
			if (!line.equals("")) {
				return line;
			}
		}
		return null;
	}
	
	private static void error() throws Exception {
		System.err.println("Error when parsing file");
		throw new Exception("");
	}
}

class Joint {
	public String name;
	public int parent;
	public Vector3f position;
	public Quat4f orientation;
	public float radius;
}
class Mesh {
	public String textureFile;
	public String shaderName;
	public Triangle[] traingles;
	public Vertex[] vertexes;
}

class Triangle {
	public int v1;
	public int v2;
	public int v3;
}

class Vertex {
	public Vector2f textureCoordinate;
	public Weight[] weights;
	public int weightStartIndex;
}

class Weight {
	public Vector3f position;
	public float bias;
	public int jointIndex;
}
