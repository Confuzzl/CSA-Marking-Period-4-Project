package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.vclip.regions.VertexRegion;

public class VEPlaneVertex extends VEPlane<CollidableEdge, VertexRegion> {
	public VEPlaneVertex(VertexRegion r, CollidableVertex v, CollidableEdge e) {
		super(r, v, e, e);
	}

	@Override
	public Vec3 getNormal() {
		return getVertex() == getEdge().getTail() ? getEdge().getReverse() : getEdge().asVec3();
	}
}
