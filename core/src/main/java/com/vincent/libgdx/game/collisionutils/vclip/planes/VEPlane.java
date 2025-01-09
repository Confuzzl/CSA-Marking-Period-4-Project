package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.vclip.regions.VoronoiRegion;

public abstract class VEPlane<NeighborType extends Feature, RegionType extends VoronoiRegion>
		extends VoronoiPlane<NeighborType, RegionType> {
	private CollidableVertex vertex;
	private CollidableEdge edge;

	protected VEPlane(RegionType region, CollidableVertex v, CollidableEdge e, NeighborType n) {
		super(region, n, v);
		vertex = v;
		edge = e;
	}

	protected CollidableVertex getVertex() {
		return vertex;
	}

	protected CollidableEdge getEdge() {
		return edge;
	}

	@Override
	public String toString() {
		return String.format("%s VEPlane for %s", super.toString(), getRegion().getFeature());
	}
}
