package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron;

public class BaseVertex<T extends BasePolyhedron> extends Feature<T> implements VectorLike {
//	private VertexRegion region;

	private Vec3 localPosition = new Vec3();
//	private Vec3 rotationDisplacement = new Vec3();

//	private Edge[] neighbors;

	protected BaseVertex(T parent, int ID, Vec3 position) {
		super(parent, ID);
		this.localPosition.set(position);
	}

//	public Edge[] getNeighbors() {
//		return neighbors;
//	}
//
//	public void setNeighbors(Edge[] neighbors) {
//		this.neighbors = neighbors;
//	}

//	@Override
//	public VertexRegion getRegion() {
//		return region;
//	}

	public Vec3 getLocalPosition() {
		return localPosition;
	}

	@Override
	public Vec3 asVec3() {
		return localPosition.add(getOwner().getGlobalPosition());
	}

//	@Override
//	public double distanceTo(VectorLike p) {
//		return p.sub(this).getMagnitude();
//	}
//
//	@Override
//	public void generateRegion() {
//		region = new VertexRegion(this);
//	}

//	public void handleRotation(Vec3 r) {
//		Vec3 n = asVec3();
//		n.rotateAroundCentroid(getOwner().getGlobalPosition(), r);
//		rotationDisplacement.set(n.sub(asVec3()));
//	}

	@Override
	public String toString() {
		return String.format("%s #%d of %s: %s", getClass().getSimpleName(), getID(), getOwner().getID(), asVec3());
	}

//	@Override
//	public DPrimeState signDPrime(Edge e, double l) {
//		if (e.evalAt(l).equals(asVec3()))
//			return DPrimeState.DEGENERATE;
//		return e.dot(e.evalAt(l).sub(this)) > 0 ? DPrimeState.POSITIVE : DPrimeState.NEGATIVE;
//	}

}
