package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.vclip.regions.VoronoiRegion;

public abstract class VoronoiPlane<NeighborType extends Feature, RegionType extends VoronoiRegion> {
	private RegionType region;
	private NeighborType neighbor;
	private CollidableVertex point;

	protected VoronoiPlane(RegionType r, NeighborType n, CollidableVertex p) {
		region = r;
		neighbor = n;
		point = p;
	}

	public double signedDistance(VectorLike v) {
		return getNormal().dot(v.sub(point));
	}

	public RegionType getRegion() {
		return region;
	}

	public NeighborType getNeighbor() {
		return neighbor;
	}

//	public Vertex getPoint() {
//		return point;
//	}

	public abstract Vec3 getNormal();

	@Override
	public String toString() {
		return String.format("{p: %s, n: %s}", point.asVec3(), getNormal());
	}

//	public static VoronoiPlane generateVertexEdgePlaneForVertex(Vertex v, Edge e) {
//		return new VoronoiPlane(v.getRegion(), e, v, v == e.getTail() ? e.getReverse() : e);
//	}
//
//	public static VoronoiPlane generateVertexEdgePlaneForEdge(Vertex v, Edge e) {
//		return new VoronoiPlane(e.getRegion(), v, v, v == e.getHead() ? e.getReverse() : e);
//	}
//
//	public static VoronoiPlane generateInOrderFaceEdgePlaneForEdge(Face f, Edge e) {
//		return new VoronoiPlane(e.getRegion(), f, e.getTail(), e.cross(f.getNormal()));
//	}
//
//	public static VoronoiPlane generateReverseFaceEdgePlaneForEdge(Face f, Edge e) {
//		return new VoronoiPlane(e.getRegion(), f, e.getTail(), e.getReverse().cross(f.getNormal()));
//	}
//
//	public static VoronoiPlane generateFaceEdgePlaneForFace(Face f, Edge e) {
//		return new VoronoiPlane(f.getRegion(), e, e.getTail(), f.getNormal().cross(e.inOrderOf(f)));
//	}

//	public static VoronoiPlane generateFaceSupportPlane(Face f) {
//		return new VoronoiPlane(f.getRegion(), null, f.sampleVertex(), f.getNormal());
//	}
}
