package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.collisionutils.vclip.VClip.DPrimeState;
import com.vincent.libgdx.game.collisionutils.vclip.planes.FEPlaneFace;
import com.vincent.libgdx.game.collisionutils.vclip.regions.FaceRegion;

public class CollidableFace extends BaseFace<CollidablePolyhedron> implements CollidableFeature, DifferentiableFeature {
	private FaceRegion region;

	private CollidableEdge[] edges;

	public CollidableFace(CollidablePolyhedron parent, int ID, CollidableEdge[] edges) {
		super(parent, ID);
		this.edges = edges;
	}

	public CollidableEdge[] getEdges() {
		return edges;
	}

	@Override
	protected Vec3 e0() {
		return edges[0].inOrderOf(this);
	}

	@Override
	protected Vec3 e1() {
		return edges[1].inOrderOf(this);
	}

	public CollidableVertex sampleVertex() {
		return edges[0].getTail();
	}

	@Override
	public double distanceTo(VectorLike p) {
		double minimumViolatedDistance = Double.NEGATIVE_INFINITY;
		CollidableEdge closestEdge = null;
		for (FEPlaneFace plane : getRegion().getPlanes()) {
			double distance = plane.signedDistance(p);
			if (distance < 0 && distance > minimumViolatedDistance) {
				minimumViolatedDistance = distance;
				closestEdge = plane.getNeighbor();
			}
		}
		if (closestEdge == null)
			return getRegion().getSupportPlane().signedDistance(p);
		return closestEdge.distanceTo(p);
	}

	@Override
	public FaceRegion getRegion() {
		return region;
	}

	@Override
	public void generateRegion() {
		region = new FaceRegion(this);
	}

	@Override
	public DPrimeState signDPrime(CollidableEdge e, double l) {
		double distance = getRegion().getSupportPlane().signedDistance(e.evalAt(l));
		double dot = e.dot(getNormal());
		if (distance == 0)
			return DPrimeState.DEGENERATE;
		double s = dot * distance;
		if (s > 0)
			return DPrimeState.POSITIVE;
		if (s < 0)
			return DPrimeState.NEGATIVE;
		return DPrimeState.ZERO;

	}
}
