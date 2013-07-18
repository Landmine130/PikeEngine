package misc;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import vecmath.Matrix4f;
import vecmath.Quat4f;
import vecmath.Vector2f;
import vecmath.Vector3d;
import vecmath.Vector3f;
import world.Animation;
import world.RiggedModel;
import world.Texture;
import world.TextureLoader;


public class Md5ToModeldata {

	private static Quat4f[][] frameOrientations;

	/**
	 * @param args
	 */
	public static void convertMesh(String filePath, String animationName) throws Exception {

		int extensionStartIndex = filePath.lastIndexOf('.');
		String name = filePath.substring(0, extensionStartIndex);
		
		String vertexOutName = name + ".vertexdata";
		String triangleOutName = name + ".triangledata";
		String jointOutName = name + ".jointdata";
		String textureAtlasOutName = name + "_textureAtlas.jpg";

		BufferedReader reader = null;
		DataOutputStream vertexWriter = null;
		DataOutputStream triangleWriter = null;
		DataOutputStream jointWriter = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			File outFile = new File(vertexOutName);
			outFile.createNewFile();
			vertexWriter = new DataOutputStream(new FileOutputStream(outFile));
			outFile = new File(triangleOutName);
			outFile.createNewFile();
			triangleWriter = new DataOutputStream(new FileOutputStream(outFile));
			outFile = new File(jointOutName);
			outFile.createNewFile();
			jointWriter = new DataOutputStream(new FileOutputStream(outFile));
		}
		catch (FileNotFoundException e) {
			System.err.println("In or out file could not be opened");
			e.printStackTrace();
		}
		try {
			String version = nextLine(reader);
			if (version.contains("MD5Version ")) {
				if (!version.endsWith("10")) {
					System.err.println("Wrong Version: only MD5Version 10 is supported");
					error();
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
			
			if (!wordNumber(1,line).contains("joints") || !wordNumber(2, line).contains("{")) {
				error();
			}
			
			Joint[] joints = new Joint[numJoints];
			
			for (int j = 0; j < numJoints; j++) {
				line = nextLine(reader);
				
				String jointName = wordNumber(1, line);
				jointName = jointName.replace('"', ' ');
				jointName = jointName.trim();
				byte parent = Byte.parseByte(wordNumber(2, line));
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
				textureFile = textureFile.substring(1, textureFile.length() - 1);
				meshes[currentMesh].textureFile = textureFile;
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
					meshes[currentMesh].traingles = new Triangle[numTris];
					for (int k = 0; k < numTris; k++) {
						line = nextLine(reader);

						int index = Integer.parseInt(wordNumber(2, line));
						short v1 = Short.parseShort(wordNumber(3, line));
						short v2 = Short.parseShort(wordNumber(4, line));
						short v3 = Short.parseShort(wordNumber(5, line));
						
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
						byte jointIndex = Byte.parseByte(wordNumber(3, line));
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
			
			
			RiggedModel[] models = new RiggedModel[meshes.length]; 
			
			for (int i = 0; i < meshes.length; i++) {
				
				Texture t = TextureLoader.getTexture(meshes[i].textureFile);
				
				int elementCount = meshes[i].traingles.length * 3;
				
				for (int j = 0; j < meshes[i].traingles.length; j++) {
					
					Triangle tri = meshes[i].traingles[j];
					
					Vector3f temp = new Vector3f();
					
					Vector3f v1 = new Vector3f();
					for (int k = 0; k < meshes[i].vertexes[tri.v1].weights.length; k++) {
						temp.set(meshes[i].vertexes[tri.v1].weights[k].position);
						temp.scale(meshes[i].vertexes[tri.v1].weights[k].bias);
						v1.add(temp);
					}
					Vector3f v2 = new Vector3f();
					for (int k = 0; k < meshes[i].vertexes[tri.v2].weights.length; k++) {
						temp.set(meshes[i].vertexes[tri.v2].weights[k].position);
						temp.scale(meshes[i].vertexes[tri.v2].weights[k].bias);
						v2.add(temp);
					}
					Vector3f v3 = new Vector3f();
					for (int k = 0; k < meshes[i].vertexes[tri.v3].weights.length; k++) {
						temp.set(meshes[i].vertexes[tri.v3].weights[k].position);
						temp.scale(meshes[i].vertexes[tri.v3].weights[k].bias);
						v3.add(temp);
					}
					v3.sub(v1);
					v2.sub(v1);
					
					Vector3f bindNormal = new Vector3f();
					bindNormal.cross(v3, v2);
					
					if (meshes[i].vertexes[tri.v1].bindNormal == null) {
						meshes[i].vertexes[tri.v1].bindNormal = new Vector3f();
					}
					meshes[i].vertexes[tri.v1].bindNormal.add(bindNormal);
					
					if (meshes[i].vertexes[tri.v2].bindNormal == null) {
						meshes[i].vertexes[tri.v2].bindNormal = new Vector3f();
					}
					meshes[i].vertexes[tri.v2].bindNormal.add(bindNormal);
					
					if (meshes[i].vertexes[tri.v3].bindNormal == null) {
						meshes[i].vertexes[tri.v3].bindNormal = new Vector3f();
					}
					meshes[i].vertexes[tri.v3].bindNormal.add(bindNormal);
				}
				
				for (int j = 0; j < meshes[i].vertexes.length; j++) {
					meshes[i].vertexes[j].bindNormal.normalize();
				}
			
				ByteBuffer elementData = BufferUtils.createByteBuffer(elementCount * 2);
				
				for (int j = 0; j < meshes[i].traingles.length; j++) {
					elementData.putShort(meshes[i].traingles[j].v1);
					elementData.putShort(meshes[i].traingles[j].v2);
					elementData.putShort(meshes[i].traingles[j].v3);
				}
				elementData.flip();
											
				int vertexCount = meshes[i].vertexes.length;
				
				ByteBuffer data = BufferUtils.createByteBuffer(vertexCount * 88);

				for (int j = 0; j < meshes[i].vertexes.length; j++) {
					
					Vector3f position = new Vector3f();
					Vector3f temp = new Vector3f();
					int k;
					/*
					for (k = 0; k < meshes[i].vertexes[j].weights.length; k++) {
						temp.set(meshes[i].vertexes[j].weights[k].position);
						temp.scale(meshes[i].vertexes[j].weights[k].bias);
						position.add(temp);
					}
					
					data.putFloat(position.x);
					data.putFloat(position.z);
					data.putFloat(position.y);
					*/
					
					for (k = 0; k < meshes[i].vertexes[j].weights.length; k++) {
						data.putFloat(meshes[i].vertexes[j].weights[k].position.x);
						data.putFloat(meshes[i].vertexes[j].weights[k].position.z);
						data.putFloat(meshes[i].vertexes[j].weights[k].position.y);
					}
					for (; k < RiggedModel.WEIGHTS_PER_VERTEX; k++) {
						data.putFloat(0f);
						data.putFloat(0f);
						data.putFloat(0f);
					}
					for (k = 0; k < meshes[i].vertexes[j].weights.length; k++) {
						data.put(meshes[i].vertexes[j].weights[k].jointIndex);
					}
					// fill unused weights
					for (; k < RiggedModel.WEIGHTS_PER_VERTEX; k++) {
						data.put((byte) 0);
					}
					for (k = 0; k < meshes[i].vertexes[j].weights.length; k++) {
						data.putFloat(meshes[i].vertexes[j].weights[k].bias);
					}
					for (; k < RiggedModel.WEIGHTS_PER_VERTEX; k++) {
						data.putFloat(0);
					}
					
					data.putFloat(meshes[i].vertexes[j].bindNormal.x);
					data.putFloat(meshes[i].vertexes[j].bindNormal.y);
					data.putFloat(meshes[i].vertexes[j].bindNormal.z);

					data.putFloat(meshes[i].vertexes[j].textureCoordinate.x);
					data.putFloat(meshes[i].vertexes[j].textureCoordinate.y);
				}
				data.flip();
				
				models[i] = new RiggedModel(data, elementData, t, vertexCount, elementCount);
			}
			RiggedModel.addLoadedModelArray(name, models);
			
			vertexWriter.close();
			triangleWriter.close();
			jointWriter.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	public static void convertAnimation(String filePath) {
		convertAnimation(filePath, filePath.substring(0, filePath.lastIndexOf('.')) + ".animationdata");
	}
	
	public static void convertAnimation(String filePath, String destinationPath) {
				
		DataOutputStream writer = null;
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			File outFile = new File(destinationPath);
			outFile.createNewFile();
			writer = new DataOutputStream(new FileOutputStream(outFile));
		}
		catch (Exception e) {
			System.err.println("File could not be opened");
			e.printStackTrace();
		}
		
		try {
			
			String line = nextLine(reader);
			if (!line.contains("MD5Version") || !wordNumber(2, line).equals("10")) {
				throw new Exception("Unsupported MD5 Version");
			}
			line = nextLine(reader);
			line = nextLine(reader);
			
			int numFrames = 0;
			
			if (line.contains("numFrames")) {
				numFrames = Integer.parseInt(wordNumber(2, line));
			}
			else {
				error();
			}
						
			
			line = nextLine(reader);
			
			int numJoints = 0;
			
			if (line.contains("numJoints")) {
				numJoints = Integer.parseInt(wordNumber(2, line));
			}
			else {
				error();
			}
						
			Vector3f[][] framePositions = new Vector3f[numFrames + 1][numJoints];
			Quat4f[][] frameOrientations = new Quat4f[numFrames + 1][numJoints];

			line = nextLine(reader);
			
			int frameRate = 0;
			
			if (line.contains("frameRate")) {
				frameRate = Integer.parseInt(wordNumber(2, line));
			}
			else {
				error();
			}
			
			line = nextLine(reader);
			
			int numAnimatedComponents = 0;
			
			if (line.contains("numAnimatedComponents")) {
				numAnimatedComponents = Integer.parseInt(wordNumber(2, line));
			}
			else {
				error();
			}
			
			line = nextLine(reader);
						
			if (!line.contains("hierarchy")) {
				error();
			}
			
			int[] jointStartIndexes = new int[numJoints];
			int[] jointFlags = new int[numJoints];
			int[] parentJointIndexes = new int[numJoints];
			
			for (int i = 0; i < numJoints; i++) {
				line = nextLine(reader);
				
				String name = wordNumber(1, line);
				name = name.substring(1, name.length() - 1);
				parentJointIndexes[i] = Integer.parseInt(wordNumber(2, line));
				jointFlags[i] = Integer.parseInt(wordNumber(3, line));
				jointStartIndexes[i] = Integer.parseInt(wordNumber(4, line));
			}
			
			line = nextLine(reader);
			if (!line.contains("}")) {
				error();
			}
			
			line = nextLine(reader);
			
			if (!line.contains("bounds")) {
				error();
			}
			
			float[] frameRadii = new float[numFrames];
			
			for (int i = 0; i < numFrames; i++) {
				line = nextLine(reader);
				Vector3f boundMin = new Vector3f(Float.parseFloat(wordNumber(2, line)), Float.parseFloat(wordNumber(3, line)), Float.parseFloat(wordNumber(4, line)));
				Vector3f boundMax = new Vector3f(Float.parseFloat(wordNumber(7, line)), Float.parseFloat(wordNumber(8, line)), Float.parseFloat(wordNumber(9, line)));
				float minDistance = boundMin.length();
				float maxDistance = boundMax.length();
				frameRadii[i] = minDistance > maxDistance ? minDistance : maxDistance;
			}
			
			line = nextLine(reader);
			if (!line.contains("}")) {
				error();
			}
			
			line = nextLine(reader);
			
			if (!line.contains("baseframe")) {
				error();
			}
			
			for (int i = 0; i < numJoints; i++) {
				
				line = nextLine(reader);
				
				float px = Float.parseFloat(wordNumber(2, line));
				float py = Float.parseFloat(wordNumber(3, line));
				float pz = Float.parseFloat(wordNumber(4, line));
				float ox = Float.parseFloat(wordNumber(7, line));
				float oy = Float.parseFloat(wordNumber(8, line));
				float oz = Float.parseFloat(wordNumber(9, line));
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
				Vector3f position = new  Vector3f(px, py, pz);
				Quat4f orientation = new Quat4f(ox, oy, oz, ow);
				
				framePositions[0][i] = position;
				frameOrientations[0][i] = orientation;
			}
			
			line = nextLine(reader);
			if (!line.contains("}")) {
				error();
			}
			
			line = nextLine(reader);
			
			while (line != null) {
			
				if (!line.contains("frame")) {
					error();
				}
				int currentFrame = Integer.parseInt(wordNumber(2, line));
				
				line = nextLine(reader);
				
				float[] components = new float[numAnimatedComponents];
				
				int wordNumber = 1;
				
				for (int i = 1; i <= numAnimatedComponents; i++) {
					if (wordNumber(wordNumber, line) == null) {
						line = nextLine(reader);
						wordNumber = 1;
					}
					components[i - 1] = Float.parseFloat(wordNumber(wordNumber, line));
					wordNumber++;
				}
				
				for (int i = 0; i < numJoints; i++) {
					
					int currentComponent = jointStartIndexes[i];
					
					Vector3f position = new Vector3f(framePositions[0][i]);
					Quat4f orientation = new Quat4f(frameOrientations[0][i]);
					
					int flags = jointFlags[i];
					
					if ((flags & 1) != 0) {
						position.x = components[currentComponent];
						currentComponent++;
					}
					if ((flags & 2) != 0) {
						position.y = components[currentComponent];
						currentComponent++;
					}
					if ((flags & 4) != 0) {
						position.z = components[currentComponent];
						currentComponent++;
					}
					if ((flags & 8) != 0) {
						orientation.x = components[currentComponent];
						currentComponent++;
					}
					if ((flags & 16) != 0) {
						orientation.y = components[currentComponent];
						currentComponent++;
					}
					if ((flags & 32) != 0) {
						orientation.z = components[currentComponent];
						currentComponent++;
					}
					
					float t = 1.0f - (orientation.x * orientation.x) - (orientation.y * orientation.y) - (orientation.z * orientation.z);
					if (t < 0.0f)
					{
						orientation.w = 0.0f;
					}
					else
					{
						orientation.w = -MathF.sqrt(t);
					}
					
					framePositions[currentFrame][i] = position;
					frameOrientations[currentFrame][i] = orientation;
				}
				
				line = nextLine(reader);
				if (!line.contains("}")) {
					error();
				}
				
				line = nextLine(reader);
			}
			reader.close();
			
			
			writer.writeInt(frameRate);
			writer.writeInt(numFrames);
			writer.writeInt(numJoints);
			
			for (int i = 0; i < numFrames; i++) {
				
				writer.writeFloat(frameRadii[i]);
				
				for (int j = 0; j < numJoints; j++) {
					
					writer.writeFloat(framePositions[i][j].x);
					writer.writeFloat(framePositions[i][j].z);
					writer.writeFloat(framePositions[i][j].y);
					writer.writeFloat(frameOrientations[i][j].x);
					writer.writeFloat(frameOrientations[i][j].z);
					writer.writeFloat(frameOrientations[i][j].y);
					writer.writeFloat(-frameOrientations[i][j].w);
				}
			}
			
			for (int i = 0; i < numJoints; i++) {
				writer.writeInt(parentJointIndexes[i]);
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String wordNumber(int target, String str) {
		
		int index = 0;
		
		for (int i = 1; index >= 0 && index < str.length(); i++) {
			int nextSpaceIndex = str.indexOf(' ', index);
			if (nextSpaceIndex < 0) {
				nextSpaceIndex = str.length();
			}
			int nextTabIndex = str.indexOf('\t', index);
			if (nextTabIndex < 0) {
				nextTabIndex = str.length();
			}
			
			int nextIndex = (nextSpaceIndex < nextTabIndex)?nextSpaceIndex:nextTabIndex;
			
			String word = str.substring(index, nextIndex);
			if (word.equals("") || word.startsWith(" ") || word.startsWith("\t")) {
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
			
			int commentIndex = line.indexOf("//");
			if (commentIndex >= 0) {
				line = line.substring(0, commentIndex);
			}
			
			if (!line.equals("")) {
				return line;
			}
			line = reader.readLine();
		}
		return null;
	}
	
	private static void error() throws Exception {
		throw new Exception("Error when parsing file");
	}
}

class Joint {
	public String name;
	public byte parent;
	public Vector3f position;
	public Quat4f orientation;
	public float radius;
}

class Mesh {
	public String textureFile;
	public Triangle[] traingles;
	public Vertex[] vertexes;
}

class Triangle {
	public short v1;
	public short v2;
	public short v3;
}

class Vertex {
	public Vector2f textureCoordinate;
	public Weight[] weights;
	public Vector3f bindNormal;
	public int weightStartIndex;
}

class Weight {
	public Vector3f position;
	public float bias;
	public byte jointIndex;
}
