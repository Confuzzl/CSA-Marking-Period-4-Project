package com.vincent.libgdx.game.collisionutils.vclip;

import com.vincent.libgdx.game.collisionutils.Collidable;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.features.DifferentiableFeature;
import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.collisionutils.vclip.planes.FEPlaneEdge;
import com.vincent.libgdx.game.collisionutils.vclip.planes.FEPlaneFace;
import com.vincent.libgdx.game.collisionutils.vclip.planes.SupportPlane;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VEPlaneEdge;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VEPlaneVertex;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VoronoiPlane;

public class VClip {
	private static boolean debug = false;

	private static void debug(String msg) {
		if (debug)
			System.out.println(msg);
	}

	public static enum VClipState {
		OVERFLOW(true), OTHER(true), PENETRATION_VE(false), PENETRATION_VF(false), PENETRATION_EE(false),
		PENETRATION_EF(false), DONE_VV(), DONE_VE(), DONE_VF(), DONE_EE();

		private boolean allow;
		private boolean error;

		private VClipState(boolean error) {
			this.error = error;
		}

		private VClipState() {
			this.allow = true;
		}

		public boolean allowed() {
			return allow;
		}

		public boolean isError() {
			return error;
		}
	}

	private static enum ClipEdgeState {
		SIMPLE, COMPOUND, COMPLETE;
	}

	private static enum PostClipEdgeState {
		UPPER, UNDER, DEGENERATE, NONE;
	}

	public static enum DPrimeState {
		POSITIVE, ZERO, NEGATIVE, DEGENERATE;
	}

	public static class VClipInfo {
		private Feature<CollidablePolyhedron> n1;
		private Feature<CollidablePolyhedron> n2;
		private VClipState state;

		public VClipInfo(Feature<CollidablePolyhedron> n1, Feature<CollidablePolyhedron> n2, VClipState state) {
			this.n1 = n1;
			this.n2 = n2;
			this.state = state;
		}

		public Feature<CollidablePolyhedron> getN1() {
			return n1;
		}

		public Feature<CollidablePolyhedron> getN2() {
			return n2;
		}

		public VClipState getState() {
			return state;
		}

		public Feature<CollidablePolyhedron> get(Collidable c) {
			CollidablePolyhedron p = c.getCollider();
			if (n1.getOwner() == p)
				return n1;
			if (n2.getOwner() == p)
				return n2;
			return null;
		}

		@Override
		public String toString() {
			return String.format("[%s, %s]: %s", n1, n2, state);
		}
	}

	public static VClipInfo closestFeaturePair(Collidable collidableA, Collidable collidableB) {
		return closestFeaturePair(collidableA, collidableB, collidableA.getCollider().sampleVertex(),
				collidableB.getCollider().sampleVertex());
	}

	public static VClipInfo closestFeaturePair(Collidable collidableA, Collidable collidableB,
			Feature<CollidablePolyhedron> n1, Feature<CollidablePolyhedron> n2) {
		VClipInfo info = null;
		try {
			if (n1 instanceof CollidableVertex v1) {
				if (n2 instanceof CollidableVertex v2) {
					info = handleVVState(v1, v2);
				} else if (n2 instanceof CollidableEdge e2) {
					info = handleVEState(v1, e2);
				} else if (n2 instanceof CollidableFace f2) {
					info = handleVFState(v1, f2);
				}
			} else if (n1 instanceof CollidableEdge e1) {
				if (n2 instanceof CollidableEdge e2) {
					info = handleEEState(e1, e2);
				} else if (n2 instanceof CollidableFace f2) {
					info = handleEFState(e1, f2);
				}
			} else {
				System.out.println(n1 + " " + n2);
				return new VClipInfo(null, null, VClipState.OTHER);
			}
			return info;
		} catch (StackOverflowError e) {
			System.out.printf("ERROR: %s %s\n", n1, n2);
			System.out.println(collidableA.getCollider().generate());
			System.out.println(collidableA.getGlobalPosition().generate());
			System.out.println(collidableB.getCollider().generate());
			System.out.println(collidableB.getGlobalPosition().generate());
			return new VClipInfo(null, null, VClipState.OVERFLOW);
		}
	}

	public static class ClipEdgeInfo<N extends Feature<CollidablePolyhedron>> {
		private CollidableEdge edge;
		private double lUnder = 0, lUpper = 1;
		private N nUnder, nUpper;
		private ClipEdgeState state = ClipEdgeState.COMPLETE;

		private ClipEdgeInfo(CollidableEdge edge) {
			this.edge = edge;
		}

		public N getNUnder() {
			return nUnder;
		}

		public N getNUpper() {
			return nUpper;
		}
	}

	private static <N extends Feature<CollidablePolyhedron>> ClipEdgeInfo<N> clipEdge(ClipEdgeInfo<N> info,
			VoronoiPlane<N, ?>[] planes) {
		for (VoronoiPlane<N, ?> plane : planes) {
			N n = plane.getNeighbor();
			double tailDistance = plane.signedDistance(info.edge.getTail());
			double headDistance = plane.signedDistance(info.edge.getHead());
			if (tailDistance < 0 && headDistance < 0) {
				info.nUnder = n;
				info.nUpper = n;
				info.state = ClipEdgeState.SIMPLE;
				return info;
			} else if (tailDistance < 0) {
				double l = tailDistance / (tailDistance - headDistance);
				if (l > info.lUnder) {
					info.lUnder = l;
					info.nUnder = n;
					if (info.lUnder > info.lUpper) {
						info.state = ClipEdgeState.COMPOUND;
						return info;
					}
				}
			} else if (headDistance < 0) {
				double l = tailDistance / (tailDistance - headDistance);
				if (l < info.lUpper) {
					info.lUpper = l;
					info.nUpper = n;
					if (info.lUnder > info.lUpper) {
						info.state = ClipEdgeState.COMPOUND;
						return info;
					}
				}
			}
		}
		return info;
	}

	private static PostClipEdgeState postClipEdge(ClipEdgeInfo<?> info, DifferentiableFeature xUnder,
			DifferentiableFeature xUpper) {
		if (info.nUnder != null) {
			DPrimeState state = xUnder.signDPrime(info.edge, info.lUnder);
			if (state == DPrimeState.DEGENERATE)
				return PostClipEdgeState.DEGENERATE;
			if (state == DPrimeState.POSITIVE)
				return PostClipEdgeState.UNDER;
		}
		if (info.nUpper != null) {
			DPrimeState state = xUpper.signDPrime(info.edge, info.lUpper);
			if (state == DPrimeState.DEGENERATE)
				return PostClipEdgeState.DEGENERATE;
			if (state == DPrimeState.NEGATIVE)
				return PostClipEdgeState.UPPER;
		}
		return PostClipEdgeState.NONE;
	}

	private static VClipInfo handleLocalMinimum(CollidableVertex v, CollidableFace f) {
		double maxSignedDistance = Double.NEGATIVE_INFINITY;
		CollidableFace closestFace = null;
		for (CollidableFace face : f.getOwner().getFaces()) {
			SupportPlane facePlane = face.getRegion().getSupportPlane();
			double distance = facePlane.signedDistance(v);
			if (distance > maxSignedDistance) {
				maxSignedDistance = distance;
				closestFace = face;
			}
		}
		if (maxSignedDistance <= 0)
			return new VClipInfo(v, f, VClipState.PENETRATION_VF);
		return handleVFState(v, closestFace);
	}

	private static VClipInfo handleVVState(CollidableVertex v1, CollidableVertex v2) {
		debug(String.format("VV state: %s %s", v1, v2));
		VClipInfo info;
		if ((info = handleVVStateSub(v1, v2)) != null)
			return info;
		if ((info = handleVVStateSub(v2, v1)) != null)
			return info;
		return new VClipInfo(v1, v2, VClipState.DONE_VV);
	}

	private static VClipInfo handleVVStateSub(CollidableVertex a, CollidableVertex b) {
		for (VEPlaneVertex plane : a.getRegion().getPlanes())
			if (plane.signedDistance(b) < 0)
				return handleVEState(b, plane.getNeighbor());
		return null;
	}

	private static VClipInfo handleVEState(CollidableVertex v, CollidableEdge e) {
		debug(String.format("VE state: %s %s", v, e));
		for (VEPlaneEdge plane : e.getRegion().getVEPlanes())
			if (plane.signedDistance(v) < 0)
				return handleVVState(v, plane.getNeighbor());

		for (FEPlaneEdge plane : e.getRegion().getFEPlanes())
			if (plane.signedDistance(v) < 0)
				return handleVFState(v, plane.getNeighbor());

		ClipEdgeInfo<CollidableEdge> info = clipEdge(new ClipEdgeInfo<CollidableEdge>(e), v.getRegion().getPlanes());
		if (info.nUnder == info.nUpper && info.nUnder != null)
			return handleEEState(e, info.nUnder);

		return switch (postClipEdge(info, v, v)) {
		case UNDER -> handleEEState(e, info.nUnder);
		case UPPER -> handleEEState(e, info.nUpper);
		case DEGENERATE -> new VClipInfo(v, e, VClipState.PENETRATION_VE);
		case NONE -> new VClipInfo(v, e, VClipState.DONE_VE);
		};
	}

	private static VClipInfo handleVFState(CollidableVertex v, CollidableFace f) {
		debug(String.format("VF state: %s %s", v, f));
		double maximalViolatingDistance = 0;
		FEPlaneFace maximalViolatingPlane = null;
		for (FEPlaneFace plane : f.getRegion().getPlanes()) {
			double distance = plane.signedDistance(v);
			if (distance < maximalViolatingDistance) {
				maximalViolatingDistance = distance;
				maximalViolatingPlane = plane;
			}
		}
		if (maximalViolatingPlane != null)
			return handleVEState(v, maximalViolatingPlane.getNeighbor());

		SupportPlane supportPlane = f.getRegion().getSupportPlane();
		double aDistance = supportPlane.signedDistance(v);
		for (CollidableEdge e : v.getNeighbors()) {
			CollidableVertex o = e.getHead() == v ? e.getTail() : v;
			double oDistance = supportPlane.signedDistance(o);
			if (aDistance * oDistance < 0)
				return handleEFState(e, f);
			if (Math.abs(oDistance) < Math.abs(aDistance))
				return handleEFState(e, f);
		}
		if (aDistance > 0)
			return new VClipInfo(v, f, VClipState.DONE_VF);
		return handleLocalMinimum(v, f);
	}

	public static VClipInfo handleEEState(CollidableEdge e1, CollidableEdge e2) {
		debug(String.format("EE state: %s %s", e1, e2));
		VClipInfo info;
		if ((info = handleEEStateSub(e1, e2)) != null)
			return info;
		if ((info = handleEEStateSub(e2, e1)) != null)
			return info;
		return new VClipInfo(e1, e2, VClipState.DONE_EE);
	}

	private static VClipInfo handleEEStateSub(CollidableEdge a, CollidableEdge b) {
		ClipEdgeInfo<CollidableVertex> clipInfoV = new ClipEdgeInfo<CollidableVertex>(b);
		if (clipEdge(clipInfoV, a.getRegion().getVEPlanes()).state == ClipEdgeState.SIMPLE)
			return handleVEState(clipInfoV.nUnder, b);
		return switch (postClipEdge(clipInfoV, clipInfoV.nUnder, clipInfoV.nUpper)) {
		case UNDER -> handleVEState(clipInfoV.nUnder, b);
		case UPPER -> handleVEState(clipInfoV.nUpper, b);
		case DEGENERATE -> new VClipInfo(a, b, VClipState.PENETRATION_EE);
		case NONE -> {
			ClipEdgeInfo<CollidableFace> clipInfoF = new ClipEdgeInfo<CollidableFace>(b);
			clipInfoF.lUnder = clipInfoV.lUnder;
			clipInfoF.lUpper = clipInfoV.lUpper;
			if (clipEdge(clipInfoF, a.getRegion().getFEPlanes()).state == ClipEdgeState.SIMPLE)
				yield handleEFState(b, clipInfoF.nUnder);
			yield switch (postClipEdge(clipInfoF, clipInfoF.nUnder, clipInfoF.nUpper)) {
			case UNDER -> handleEFState(b, clipInfoF.nUnder);
			case UPPER -> handleEFState(b, clipInfoF.nUpper);
			case DEGENERATE -> new VClipInfo(a, b, VClipState.PENETRATION_EE);
			case NONE -> null;
			};
		}
		};
	}

	private static VClipInfo handleEFState(CollidableEdge e, CollidableFace f) {
		debug(String.format("EF state: %s %s", e, f));
		ClipEdgeInfo<CollidableEdge> clipInfo = new ClipEdgeInfo<CollidableEdge>(e);
		if (clipEdge(clipInfo, f.getRegion().getPlanes()).state != ClipEdgeState.COMPLETE) {
			return handleEEState(e, clipInfo.nUnder);
		}

		SupportPlane supportPlane = f.getRegion().getSupportPlane();
		double dUnder = supportPlane.signedDistance(e.evalAt(clipInfo.lUnder));
		double dUpper = supportPlane.signedDistance(e.evalAt(clipInfo.lUpper));
		if (dUnder * dUpper <= 0)
			return new VClipInfo(e, f, VClipState.PENETRATION_EF);
		if (f.signDPrime(e, clipInfo.lUnder) == DPrimeState.POSITIVE)
			return clipInfo.nUnder != null ? handleEEState(e, clipInfo.nUnder) : handleVFState(e.getTail(), f);
		return clipInfo.nUpper != null ? handleEEState(e, clipInfo.nUpper) : handleVFState(e.getHead(), f);
	}
}
