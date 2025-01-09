package com.vincent.libgdx.game.collisionutils.vclip.regions;

import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.vclip.planes.FEPlaneEdge;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VEPlaneEdge;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VoronoiPlane;

public class EdgeRegion extends VoronoiRegion<CollidableEdge, VoronoiPlane> {
	private VEPlaneEdge[] VEPlanes;
	private FEPlaneEdge[] FEPlanes;

	public EdgeRegion(CollidableEdge e) {
		super(e);
		VEPlanes = new VEPlaneEdge[] { new VEPlaneEdge(this, e.getTail(), e), new VEPlaneEdge(this, e.getHead(), e) };
		FEPlanes = new FEPlaneEdge[] { new FEPlaneEdge(this, e.getFaceInOrder(), e, false),
				new FEPlaneEdge(this, e.getFaceReverse(), e, true) };
		setPlanes(new VoronoiPlane[] { VEPlanes[0], VEPlanes[1], FEPlanes[0], FEPlanes[1] });
	}

	public VEPlaneEdge[] getVEPlanes() {
		return VEPlanes;
	}

	public FEPlaneEdge[] getFEPlanes() {
		return FEPlanes;
	}
}
