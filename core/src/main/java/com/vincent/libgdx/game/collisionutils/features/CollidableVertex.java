package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.collisionutils.vclip.VClip.DPrimeState;
import com.vincent.libgdx.game.collisionutils.vclip.regions.VertexRegion;

public class CollidableVertex extends BaseVertex<CollidablePolyhedron>
		implements CollidableFeature, DifferentiableFeature {
	private VertexRegion region;
	private CollidableEdge[] neighbors;

	public CollidableVertex(CollidablePolyhedron parent, int ID, Vec3 position) {
		super(parent, ID, position);
	}

	public CollidableEdge[] getNeighbors() {
		return neighbors;
	}

	public void setNeighbors(CollidableEdge[] neighbors) {
		this.neighbors = neighbors;
	}

	@Override
	public VertexRegion getRegion() {
		return region;
	}

	@Override
	public double distanceTo(VectorLike p) {
		return p.sub(this).getMagnitude();
	}

	@Override
	public void generateRegion() {
		region = new VertexRegion(this);
	}

	@Override
	public DPrimeState signDPrime(CollidableEdge e, double l) {
		if (e.evalAt(l).equals(asVec3()))
			return DPrimeState.DEGENERATE;
		double s = e.dot(e.evalAt(l).sub(this));
		if (s > 0)
			return DPrimeState.POSITIVE;
		if (s < 0)
			return DPrimeState.NEGATIVE;
		return DPrimeState.ZERO;
	}
}
