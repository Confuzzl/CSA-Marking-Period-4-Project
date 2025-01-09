package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.vclip.regions.EdgeRegion;

public class VEPlaneEdge extends VEPlane<CollidableVertex, EdgeRegion> {
	public VEPlaneEdge(EdgeRegion r, CollidableVertex v, CollidableEdge e) {
		super(r, v, e, v);
	}

	@Override
	public Vec3 getNormal() {
		return getVertex() == getEdge().getHead() ? getEdge().getReverse() : getEdge().asVec3();
	}
}
