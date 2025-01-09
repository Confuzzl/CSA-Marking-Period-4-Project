package com.vincent.libgdx.game.world.utils;

import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder.VertexInfo;
import com.badlogic.gdx.math.Vector3;
import com.vincent.libgdx.game.collisionutils.features.UVFace;
import com.vincent.libgdx.game.collisionutils.features.UVVertex;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public class Tri {
	private VertexInfo i0, i1, i2;

	public Tri(UVFace parent, UVVertex v0, UVVertex v1, UVVertex v2, float[] uv0, float[] uv1, float[] uv2) {
//		Vec3 centroid = parent.getOwner().getLocalPosition();
		Vec3 n = parent.getNormal();
		Vector3 normal = new Vector3((float) n.x(), (float) n.y(), (float) n.z());
		i0 = new VertexInfo().setPos((float) v0.x(), (float) v0.y(), (float) v0.z()).setUV(uv0[0], 1 - uv0[1])
				.setNor(normal);
		i1 = new VertexInfo().setPos((float) v1.x(), (float) v1.y(), (float) v1.z()).setUV(uv1[0], 1 - uv1[1])
				.setNor(normal);
		i2 = new VertexInfo().setPos((float) v2.x(), (float) v2.y(), (float) v2.z()).setUV(uv2[0], 1 - uv2[1])
				.setNor(normal);
	}

	public VertexInfo getI0() {
		return i0;
	}

	public VertexInfo getI1() {
		return i1;
	}

	public VertexInfo getI2() {
		return i2;
	}
}
