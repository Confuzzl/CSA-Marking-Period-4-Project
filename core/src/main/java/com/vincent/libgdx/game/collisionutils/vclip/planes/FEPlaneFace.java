package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.vclip.regions.FaceRegion;

public class FEPlaneFace extends FEPlane<CollidableEdge, FaceRegion> {
	public FEPlaneFace(FaceRegion r, CollidableFace f, CollidableEdge e) {
		super(r, f, e, e);
	}

	@Override
	public Vec3 getNormal() {
		return getFace().getNormal().cross(getEdge().inOrderOf(getFace()));
	}
}
