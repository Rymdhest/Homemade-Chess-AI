package Models;
import org.lwjgl.util.vector.Vector3f;

import Models.Model;
import Models.RawModel;
import Util.Maths;

public class MeshGenerator {
	public static RawModel generateCylinder(CylinderSettings s) {
		Loader loader = new Loader();
		int numVerts = s.detail*2;
		if (s.sealTop) {
			numVerts++;
			numVerts += s.detail;
		}
		float[] positions = new float[numVerts*3];
		for (int i = 0 ; i<s.detail ; i++) {
			float percent = ((float)i)/s.detail;
			percent *= Math.PI*2f;

			//top
			positions[i*6] = Maths.sin(percent)*s.topSize.x;
			positions[i*6+1] = s.topSize.y;
			positions[i*6+2] = Maths.cos(percent)*s.topSize.z;
			//bot
			positions[i*6+3] = Maths.sin(percent)*s.botSize.x;
			positions[i*6+4] = s.botSize.y;
			positions[i*6+5] = Maths.cos(percent)*s.botSize.z;
			
		}
		if (s.sealTop) {
			int offset = s.detail*6;
			for (int i = 0 ; i<s.detail ; i++) {
				float percent = ((float)i)/s.detail;
				percent *= Math.PI*2f;

				//top
				positions[offset+i*3] = Maths.sin(percent)*s.topSize.x;
				positions[offset+i*3+1] = s.topSize.y;
				positions[offset+i*3+2] = Maths.cos(percent)*s.topSize.z;
				
			}
			positions[numVerts*3-3] = 0;
			positions[numVerts*3-2] = s.topSize.y;
			positions[numVerts*3-1] = 0;
		}
		float[] normals = new float[numVerts*3];
		for (int i = 0 ; i<s.detail ; i++) {
			float angle = 2 * ((float) Math.PI) * (i / (float) s.detail);
            float cos = ((float) Math.cos(angle));
            float sin = ((float) Math.sin(angle));
			
			float height = s.topSize.y-s.botSize.y;
			float r = s.botSize.x-s.topSize.x;
			float triangleAngle = (float) Math.asin(height/r);
			
			float hypo = r/Maths.cos(triangleAngle);
			float normalY = r/hypo;
			float flank_len = (float) Math.sqrt(r*r + height*height); 
			float cone_x = r / flank_len; 
			float cone_y = -height / flank_len;
			Vector3f newNormal= new Vector3f(-cone_y*sin, cone_x, -cone_y*cos);
			Vector3f normalTop = new Vector3f(positions[i*6], 0.1f,positions[i*6+2]);
			normalTop.normalise();
			//top
			normals[i*6] = newNormal.x;
			normals[i*6+1] = newNormal.y;
			normals[i*6+2] = newNormal.z;
			//bot
			Vector3f normalBot = new Vector3f(positions[i*6+3],0.1f,positions[i*6+5]);
			normalBot.normalise();
			normals[i*6+3] = newNormal.x;
			normals[i*6+4] = newNormal.y;
			normals[i*6+5] = newNormal.z;
			
		}
		if (s.sealTop) {
			for (int i = 0 ; i<s.detail ; i++) {
				int offset = s.detail*6;
				normals[offset+i*3] = 0;
				normals[offset+i*3+1] = 1f;
				normals[offset+i*3+2] = 0f;
			}

			normals[numVerts*3-3] = 0;
			normals[numVerts*3-2] = 1f;
			normals[numVerts*3-1] = 0;
		}
		float[] colours = new float[numVerts*3];
		for (int i = 0 ; i<numVerts ; i++) {
			colours[i*3]=1f;
			colours[i*3+1]=1f;
			colours[i*3+2]=1f;
		}
		int numIndices = s.detail*6;
		if(s.sealTop) numIndices+=s.detail*3;
		int[] indices = new int[numIndices];
		for(int i = 0 ; i <s.detail ; i++) {
			if (i == s.detail-1) {
				indices[i*6] = i*2+0;
				indices[i*6+1] = i*2+1;
				indices[i*6+2] = 0*2+0;
				indices[i*6+3] = i*2+1;
				indices[i*6+4] = 0*2+1;
				indices[i*6+5] = 0*2+0;
			} else {
				indices[i*6] = i*2+0;
				indices[i*6+1] = i*2+1;
				indices[i*6+2] = i*2+2;
				indices[i*6+3] = i*2+1;
				indices[i*6+4] = i*2+3;
				indices[i*6+5] = i*2+2;
			}

		}
		if (s.sealTop) {
			for(int i = 0 ; i <s.detail ; i++) {
				int offset = s.detail*6;
				int offsetVert = s.detail*2;
				if (i == s.detail-1) {
					indices[offset+i*3+0] = offsetVert+i+0;
					indices[offset+i*3+1] = offsetVert+0;
					indices[offset+i*3+2] = numVerts-1;

				} else {
					indices[offset+i*3+0] = offsetVert+i+0;
					indices[offset+i*3+1] = offsetVert+i+1;
					indices[offset+i*3+2] = numVerts-1;
				}
			}
		}

		
		return new RawModel(positions, normals, colours, indices);
	}
	public static RawModel generateIcosahedron(IcosahedronSettings s) {
		Loader loader = new Loader();
		float[] positions = {
		 0.000000f, -1.000000f, 0.000000f,
		 0.723607f, -0.447220f, 0.525725f,
		 -0.276388f, -0.447220f, 0.850649f,
		 -0.894426f, -0.447216f, 0.000000f,
		 -0.276388f, -0.447220f, -0.850649f,
		 0.723607f, -0.447220f, -0.525725f,
		 0.276388f, 0.447220f, 0.850649f,
		 -0.723607f, 0.447220f, 0.525725f,
		 -0.723607f, 0.447220f, -0.525725f,
		 0.276388f, 0.447220f, -0.850649f,
		  0.894426f, 0.447216f, 0.000000f,
		 0.000000f, 1.000000f, 0.000000f,
		 -0.162456f, -0.850654f, 0.499995f,
		 0.425323f, -0.850654f, 0.309011f,
		 0.262869f, -0.525738f, 0.809012f,
		 0.850648f, -0.525736f, 0.000000f,
		 0.425323f, -0.850654f, -0.309011f,
		 -0.525730f, -0.850652f, 0.000000f,
		 -0.688189f, -0.525736f, 0.499997f,
		 -0.162456f, -0.850654f, -0.499995f,
		 -0.688189f, -0.525736f, -0.499997f,
		 0.262869f, -0.525738f, -0.809012f,
		 0.951058f, 0.000000f, 0.309013f,
		 0.951058f, 0.000000f, -0.309013f,
		 0.000000f, 0.000000f, 1.000000f,
		 0.587786f, 0.000000f, 0.809017f,
		 -0.951058f, 0.000000f, 0.309013f,
		 -0.587786f, 0.000000f, 0.809017f,
		 -0.587786f, 0.000000f, -0.809017f,
		 -0.951058f, 0.000000f, -0.309013f,
		 0.587786f, 0.000000f, -0.809017f,
		 0.000000f, 0.000000f, -1.000000f,
		 0.688189f, 0.525736f, 0.499997f,
		 -0.262869f, 0.525738f, 0.809012f,
		 -0.850648f, 0.525736f, 0.000000f,
		 -0.262869f, 0.525738f, -0.809012f,
		 0.688189f, 0.525736f, -0.499997f,
		 0.162456f, 0.850654f, 0.499995f,
		 0.525730f, 0.850652f, 0.000000f,
		 -0.425323f, 0.850654f, 0.309011f,
		 -0.425323f, 0.850654f, -0.309011f,
		 0.162456f, 0.850654f, -0.499995f,};

		float[] normals = new float[positions.length];
		for (int i = 0 ; i<normals.length/3 ; i++) {
			Vector3f norm = new Vector3f(positions[i*3],positions[i*3+1],positions[i*3+2]);
			norm.normalise();
			normals[i*3] = norm.x;
			normals[i*3+1] = norm.y;
			normals[i*3+2] = norm.z;
		}
		
		float[] colours = new float[positions.length];
		int[] indices = {	 
				1, 14, 13,
				 2, 14, 16,
				 1, 13, 18,
				 1, 18, 20,
				 1, 20, 17,
				 2, 16, 23,
				 3, 15, 25,
				 4, 19, 27,
				 5, 21, 29,
				 6, 22, 31,
				 2, 23, 26,
				 3, 25, 28,
				 4, 27, 30,
				 5, 29, 32,
				 6, 31, 24,
				 7, 33, 38,
				 8, 34, 40,
				 9, 35, 41,
				 10, 36, 42,
				 11, 37, 39,
				 39, 42, 12,
				 39, 37, 42,
				 37, 10, 42,
				 42, 41, 12,
				 42, 36, 41,
				 36, 9, 41,
				 41, 40, 12,
				 41, 35, 40,
				 35, 8, 40,
				 40, 38, 12,
				 40, 34, 38,
				 34, 7, 38,
				 38, 39, 12,
				 38, 33, 39,
				 33, 11, 39,
				 24, 37, 11,
				 24, 31, 37,
				 31, 10, 37,
				 32, 36, 10,
				 32, 29, 36,
				 29, 9, 36,
				 30, 35, 9,
				 30, 27, 35,
				 27, 8, 35,
				 28, 34, 8,
				 28, 25, 34,
				 25, 7, 34,
				 26, 33, 7,
				 26, 23, 33,
				 23, 11, 33,
				 31, 32, 10,
				 31, 22, 32,
				 22, 5, 32,
				 29, 30, 9,
				 29, 21, 30,
				 21, 4, 30,
				 27, 28, 8,
				 27, 19, 28,
				 19, 3, 28,
				 25, 26, 7,
				 25, 15, 26,
				 15, 2, 26,
				 23, 24, 11,
				 23, 16, 24,
				 16, 6, 24,
				 17, 22, 6,
				 17, 20, 22,
				 20, 5, 22,
				 20, 21, 5,
				 20, 18, 21,
				 18, 4, 21,
				 18, 19, 4,
				 18, 13, 19,
				 13, 3, 19,
				 16, 17, 6,
				 16, 14, 17,
				 14, 1, 17,
				 13, 15, 3,
				 13, 14, 15,
				 14, 2, 15,
		};
		for (int i = 0 ; i<indices.length ; i++) {
			indices[i] -= 1;
		}
		
		for (int i = 0 ; i<positions.length ; i+=3) {
			positions[i] *= s.size.x;
			positions[i+1] *= s.size.y;
			positions[i+2] *= s.size.z;
		}

		for (int i = 0 ; i<colours.length ; i+=3) {
			colours[i] = 1f;
			colours[i+1] = 1f;
			colours[i+2] = 1f;
		}
		return new RawModel(positions, normals, colours, indices);
	}
	
	public static RawModel generateBox(BoxSettings s) {
		Loader loader = new Loader();
		float[] positions = {
				-s.topSize.x, s.topSize.y, -s.topSize.z, 
				-s.topSize.x, s.topSize.y, s.topSize.z, 
				s.topSize.x, s.topSize.y, s.topSize.z,  //top
				s.topSize.x, s.topSize.y, -s.topSize.z, 
				
				-s.topSize.x, s.topSize.y, s.topSize.z, 
				-s.botSize.x, -s.botSize.y, s.botSize.z, 
				s.botSize.x, -s.botSize.y, s.botSize.z,  //front
				s.topSize.x, s.topSize.y, s.topSize.z,
				
				-s.topSize.x, s.topSize.y, s.topSize.z, 
				-s.botSize.x, -s.botSize.y, s.botSize.z, 
				-s.botSize.x, -s.botSize.y, -s.botSize.z, //left
				-s.topSize.x, s.topSize.y, -s.topSize.z,
				
				s.topSize.x, s.topSize.y, -s.topSize.z, 
				s.botSize.x, -s.botSize.y, -s.botSize.z, 
				s.botSize.x, -s.botSize.y, s.botSize.z, //right
				s.topSize.x, s.topSize.y, s.topSize.z,
				
				s.topSize.x, s.topSize.y, -s.topSize.z, 
				s.botSize.x, -s.botSize.y, -s.botSize.z, //back
				-s.botSize.x, -s.botSize.y, -s.botSize.z, 
				-s.topSize.x, s.topSize.y, -s.topSize.z,
				
				-s.botSize.x, -s.botSize.y, s.botSize.z, 
				-s.botSize.x, -s.botSize.y, -s.botSize.z, //bot
				s.botSize.x, -s.botSize.y, -s.botSize.z, 
				s.botSize.x, -s.botSize.y, s.botSize.z,
				};

		int[] indices = {
				0,1,2, 2,3,0, // top
				4,5,6, 6,7,4,	//front
				10,9,8, 8,11,10,  //left
				14,13,12, 12,15,14,//right
				16,17,18, 18,19,16,//back
				20,21,22, 22,23,20//bot
				};
		float[] normals = {
				0, 1, 0, 
				0, 1, 0, 
				0, 1, 0, 
				0, 1, 0, 
				
				0, 0, 1, 
				0, 0, 1, 
				0, 0, 1, 
				0, 0, 1, 
				
				-1, 0, 0, 
				-1, 0, 0, 
				-1, 0, 0, 
				-1, 0, 0, 
				
				1, 0, 0, 
				1, 0, 0, 
				1, 0, 0, 
				1, 0, 0, 
		
				0, 0, -1, 
				0, 0, -1, 
				0, 0, -1, 
				0, 0, -1,
				
				0, -1, 0, 
				0, -1, 0, 
				0, -1, 0, 
				0, -1, 0, 
		 
		};
		float[] colours = new float[positions.length];
		for (int i = 0 ; i<colours.length ; i+=3) {
			colours[i]=1f;
			colours[i+1]=1f;
			colours[i+2]=1f;
		}

		return new RawModel(positions, normals, colours, indices);
	}
}
