package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.UVPolyhedron;
import com.vincent.libgdx.game.world.utils.Tri;

public class UVFace extends BaseFace<UVPolyhedron> {
	private UVVertex[] vertices;
	private Vec3 centroid;

	private Tri[] tris;
	private float[][][] UVs;

	public UVFace(UVPolyhedron parent, int ID, UVVertex[] vertices, float[][][] UVs) {
		super(parent, ID);
		this.vertices = vertices;
		this.UVs = UVs;

		initializeNormal();
	}

	@Override
	protected Vec3 e0() {
		return vertices[1].sub(vertices[0]);
	}

	@Override
	protected Vec3 e1() {
		return vertices[2].sub(vertices[1]);
	}

	public void calculateRenderingData() {
		calculateCentroid();
		createTris();
	}

	private void calculateCentroid() {
		Vec3 sum = new Vec3();
		for (UVVertex v : vertices)
			sum.addEquals(v);
		centroid = sum.div(vertices.length);
	}

	private void createTris() {
		tris = new Tri[vertices.length - 2];
		UVVertex origin = vertices[0];
		for (int i = 0; i < tris.length; i++) {
			tris[i] = new Tri(this, origin, vertices[i + 1], vertices[i + 2], UVs[i][0], UVs[i][1], UVs[i][2]);
		}
	}

	public Tri[] getTris() {
		return tris;
	}

	public float[][][] getUVs() {
		return UVs;
	}

	public Vec3 getCentroid() {
		return centroid;
	}
}
