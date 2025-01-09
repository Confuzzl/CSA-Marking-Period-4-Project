package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.vclip.regions.EdgeRegion;

public class FEPlaneEdge extends FEPlane<CollidableFace, EdgeRegion> {
	private boolean reversed;

	public FEPlaneEdge(EdgeRegion r, CollidableFace f, CollidableEdge e, boolean reversed) {
		super(r, f, e, f);
		this.reversed = reversed;
	}

	@Override
	public Vec3 getNormal() {
		return (reversed ? getEdge().getReverse() : getEdge().asVec3()).cross(getFace().getNormal());
	}
}
