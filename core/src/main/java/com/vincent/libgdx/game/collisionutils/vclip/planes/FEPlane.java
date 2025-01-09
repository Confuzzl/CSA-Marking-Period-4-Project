package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.vclip.regions.VoronoiRegion;

public abstract class FEPlane<NeighborType extends Feature, RegionType extends VoronoiRegion>
		extends VoronoiPlane<NeighborType, RegionType> {
	private CollidableFace face;
	private CollidableEdge edge;

	protected FEPlane(RegionType region, CollidableFace f, CollidableEdge e, NeighborType n) {
		super(region, n, e.getTail());
		face = f;
		edge = e;
	}

	protected CollidableFace getFace() {
		return face;
	}

	protected CollidableEdge getEdge() {
		return edge;
	}

	@Override
	public String toString() {
		return String.format("%s FEPlane for %s", super.toString(), getRegion().getFeature());
	}
}
