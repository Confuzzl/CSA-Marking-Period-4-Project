package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.collisionutils.vclip.regions.EdgeRegion;

public class CollidableEdge extends BaseEdge<CollidablePolyhedron, CollidableVertex> implements CollidableFeature {
	private EdgeRegion region;

//	private CollidableVertex tail, head;
	private CollidableFace faceInOrder, faceReverse;

	public CollidableEdge(CollidablePolyhedron parent, int ID, CollidableVertex tail, CollidableVertex head) {
		super(parent, ID, tail, head);
	}

	public Vec3 inOrderOf(CollidableFace f) {
		if (f == faceInOrder)
			return asVec3();
		if (f == faceReverse)
			return getReverse();
		return null;
	}

	public boolean isInOrderOf(CollidableFace f) {
		return f == faceInOrder;
	}

	public void setNeighbors(CollidableFace neighbor0, CollidableFace neighbor1) {
		faceInOrder = neighbor0;
		faceReverse = neighbor1;
	}

//	public VoronoiPlane[] getVertexEdgePlanes() {
//		return vertexEdgePlanes;
//	}
//
//	public VoronoiPlane[] getFaceEdgePlanes() {
//		return faceEdgePlanes;
//	}

	public CollidableFace getFaceInOrder() {
		return faceInOrder;
	}

	public CollidableFace getFaceReverse() {
		return faceReverse;
	}

	@Override
	public EdgeRegion getRegion() {
		return region;
	}

	@Override
	public double distanceTo(VectorLike p) {
		Vec3 tail = getTail().asVec3();
		Vec3 head = getHead().asVec3();
		Vec3 dir = head.sub(tail);
		Vec3 closestPoint = evalAt(Math.max(0, Math.min(p.sub(tail).dot(dir) / dir.dot(dir), 1)));
		return p.sub(closestPoint).getMagnitude();
	}

	@Override
	public void generateRegion() {
		region = new EdgeRegion(this);
	}
}
