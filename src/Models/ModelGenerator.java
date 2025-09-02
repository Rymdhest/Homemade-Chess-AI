package Models;

import org.lwjgl.util.vector.Vector3f;

public class ModelGenerator {


	public static RawModel generateBase() {
		RawModel rawModel = MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.35f, 0f, 0.35f), new Vector3f(0.25f, 0.1f, 0.25f), 13));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.1f, 0.25f), new Vector3f(0.4f, 0.17f, 0.4f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.4f, 0.17f, 0.4f), new Vector3f(0.25f, 0.25f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.25f, 0.25f), new Vector3f(0.3f, 0.3f, 0.3f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.3f, 0.3f, 0.3f), new Vector3f(0.25f, 0.32f, 0.25f), 13)));
		
		return rawModel;
	}
	public static Model generateBishop() {
		RawModel rawModel= generateBase();
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.32f, 0.25f), new Vector3f(0.15f, 0.7f, 0.15f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.15f, 0.7f, 0.15f), new Vector3f(0.25f, 0.74f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.74f, 0.25f), new Vector3f(0.15f, 0.78f, 0.15f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.15f, 0.78f, 0.15f), new Vector3f(0.1f, 0.85f, 0.1f), 13)));
		RawModel headModel = MeshGenerator.generateIcosahedron(new IcosahedronSettings(new Vector3f(0.3f, 0.55f, 0.3f)));
		RawModel headNob = MeshGenerator.generateIcosahedron(new IcosahedronSettings(0.1f));
		headNob.transform(0, 0.55f, 0, 0, 0, 0, 1);
		headModel.mergeModel(headNob);
		headModel.transform(0, 1.3f, 0, 0, 0, 0, 1);
		rawModel.mergeModel(headModel);
		return Loader.loadToVAO(rawModel);
	}
	public static Model generateKnight() {
		RawModel rawModel= generateBase();
		RawModel body = MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.35f, 0.32f, 0.35f), new Vector3f(0.15f, 1.4f, 0.15f), 5));
		body.transform(0, -0.1f, -0.2f, 17, 0, 0, 1);
		rawModel.mergeModel(body);
		

		RawModel head = MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.2f, 0.0f, 0.25f), new Vector3f(0.05f, 0.6f, 0.05f), 3));
		head.transform(0, 1.4f, 0, -115f, 0, 0, 1);
		rawModel.mergeModel(head);
		
		
		RawModel sphere = MeshGenerator.generateIcosahedron(new IcosahedronSettings(new Vector3f(0.25f, 0.35f, 0.4f)));
		sphere.transform(0, 1.4f, 0.2f, 0, 0, 0, 1);
		rawModel.mergeModel(sphere);
		
		RawModel mane = MeshGenerator.generateIcosahedron(new IcosahedronSettings(new Vector3f(0.08f, 0.45f, 0.42f)));
		mane.transform(0, 1.4f, 0.25f, 0, 0, 0, 1);
		rawModel.mergeModel(mane);
		
		RawModel ears = MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.07f, 0f, 0.05f), new Vector3f(0.04f, 0.17f, 0.04f), 3));
		ears.transform(0, 1.62f, 0, -25f, 0, 0, 1);

		ears.transform(-0.1f, 0, 0, 0, 0, 0, 1);
		rawModel.mergeModel(ears);
		ears.transform(0.2f, 0, 0, 0, 0, 0, 1);
		rawModel.mergeModel(ears);
		
		return Loader.loadToVAO(rawModel);
	}
	public static Model generatePawn() {
		RawModel rawModel= generateBase();
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.32f, 0.25f), new Vector3f(0.15f, 0.7f, 0.15f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.15f, 0.7f, 0.15f), new Vector3f(0.25f, 0.74f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.74f, 0.25f), new Vector3f(0.15f, 0.78f, 0.15f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.15f, 0.78f, 0.15f), new Vector3f(0.1f, 0.85f, 0.1f), 13)));
		RawModel headModel = MeshGenerator.generateIcosahedron(new IcosahedronSettings(0.3f));
		headModel.transform(0, 1.1f, 0, 0, 0, 0, 1);
		rawModel.mergeModel(headModel);
		return Loader.loadToVAO(rawModel);
	}
	public static Model generateRook() {
		RawModel rawModel= generateBase();
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.32f, 0.25f), new Vector3f(0.25f, 0.7f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.7f, 0.25f), new Vector3f(0.25f, 0.74f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.74f, 0.25f), new Vector3f(0.15f, 0.78f, 0.15f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.15f, 0.78f, 0.15f), new Vector3f(0.1f, 0.85f, 0.1f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.1f, 0.85f, 0.1f), new Vector3f(0.3f, 0.9f, 0.3f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.3f, 0.9f, 0.3f), new Vector3f(0.35f, 1.1f, 0.35f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.35f, 1.1f, 0.35f), new Vector3f(0.15f, 1.15f, 0.15f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.15f, 1.15f, 0.15f), new Vector3f(0.35f, 1.2f, 0.35f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.35f, 1.2f, 0.35f), new Vector3f(0.38f, 1.45f, 0.38f), 13)));
		return Loader.loadToVAO(rawModel);
	}
	public static Model generateQueen() {
		RawModel rawModel= generateBase();
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.32f, 0.25f), new Vector3f(0.2f, 0.9f, 0.2f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.2f, 0.9f, 0.2f), new Vector3f(0.25f, 1.f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 1.0f, 0.25f), new Vector3f(0.29f, 1.1f, 0.29f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.35f, 1.1f, 0.35f), new Vector3f(0.25f, 1.2f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 1.2f, 0.25f), new Vector3f(0.28f, 1.4f, 0.28f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.45f, 1.4f, 0.45f), new Vector3f(0.25f, 1.45f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 1.45f, 0.25f), new Vector3f(0.37f, 1.75f, 0.37f), 13)));
		
		RawModel head = MeshGenerator.generateIcosahedron(new IcosahedronSettings(0.3f));
		head.transform(0, 1.80f, 0, 0, 0, 0, 1);
		rawModel.mergeModel(head);
		
		RawModel nob = MeshGenerator.generateIcosahedron(new IcosahedronSettings(0.1f));
		nob.transform(0, 2.1f, 0, 0, 0, 0, 1);
		rawModel.mergeModel(nob);
		
		return Loader.loadToVAO(rawModel);
	}

	public static Model generateKing() {
		RawModel rawModel= generateBase();
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 0.32f, 0.25f), new Vector3f(0.2f, 0.9f, 0.2f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.2f, 0.9f, 0.2f), new Vector3f(0.25f, 1.f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 1.0f, 0.25f), new Vector3f(0.45f, 1.1f, 0.45f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.35f, 1.1f, 0.35f), new Vector3f(0.25f, 1.2f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 1.2f, 0.25f), new Vector3f(0.35f, 1.4f, 0.35f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.35f, 1.4f, 0.35f), new Vector3f(0.25f, 1.45f, 0.25f), 13)));
		rawModel.mergeModel(MeshGenerator.generateCylinder(new CylinderSettings(new Vector3f(0.25f, 1.45f, 0.25f), new Vector3f(0.45f, 1.75f, 0.45f), 13)));
		
		RawModel cross = MeshGenerator.generateBox(new BoxSettings(new Vector3f(0.1f, 0, 0.1f), new Vector3f(0.1f, 0.5f, 0.1f)));
		RawModel cross2 = MeshGenerator.generateBox(new BoxSettings(new Vector3f(0.1f, 0, 0.1f), new Vector3f(0.1f, 0.5f, 0.1f)));
		cross2.transform(0.25f, 0.25f, 0, 0, 0, 90f, 1);
		cross.mergeModel(cross2);
		cross.transform(0, 1.75f, 0, 0, 0, 0, 1);
		rawModel.mergeModel(cross);
		return Loader.loadToVAO(rawModel);
	}
}
