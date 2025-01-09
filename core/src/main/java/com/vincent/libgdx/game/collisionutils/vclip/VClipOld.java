package com.vincent.libgdx.game.collisionutils.vclip;

import com.vincent.libgdx.game.collisionutils.Collidable;
import com.vincent.libgdx.game.collisionutils.features.CollidableEdge;
import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VoronoiPlane;
import com.vincent.libgdx.game.collisionutils.vclip.regions.VoronoiRegion;

public class VClipOld {
	private static boolean debug = false;

	public static enum State {
		SUB_CONTINUE, CONTINUE, PENETRATION, DONE, ERROR;
	}

	private static enum ClipEdgeState {
		SIMPLE, COMPOUND, COMPLETE;
	}

	public static void debug(String msg) {
		if (debug)
			System.out.println(msg);
	}

	public static class VClipInfo {
		private Feature[] pair;
		private State state;

		public VClipInfo(Feature[] pair, State state) {
			this.pair = pair;
			this.state = state;
		}

		public Feature[] getPair() {
			return pair;
		}

		public State getState() {
			return state;
		}

		public Feature get(Collidable c) {
			CollidablePolyhedron p = c.getCollider();
			if (pair[0].getOwner() == p) {
				return pair[0];
			}
			if (pair[1].getOwner() == p) {
				return pair[1];
			}
			return null;
		}
	}

	public static VClipInfo closestFeaturePair(Collidable collidableA, Collidable collidableB) {
		return closestFeaturePair(collidableA, collidableB,
				new Feature[] { collidableA.getCollider().sampleVertex(), collidableB.getCollider().sampleVertex() });
	}

	public static VClipInfo closestFeaturePair(Collidable collidableA, Collidable collidableB, Feature[] startingPair) {
		Feature[] output = startingPair;
		int iterations = 0;
		State state = State.CONTINUE;
		while (state == State.CONTINUE) {
			if (iterations > 20) {
				debug("end early");
				state = State.ERROR;
				break;
			}
			if (output[0].getOwner() == output[1].getOwner()) {
				debug("something wrong");
				state = State.ERROR;
				break;
			}
			iterations++;
//			debug("count: " + iterations++);
			validatePair(output);
//			if (validatePair(output))
//				debug("feature validated to: " + Arrays.toString(output));
//			else
//				debug("feature is: " + Arrays.toString(output));

			if (output[0] instanceof CollidableVertex vertexA) {
				if (output[1] instanceof CollidableVertex vertexB) {
					debug(String.format("VV state: %s %s", vertexA, vertexB));
					state = handleVVState(vertexA, vertexB, output);
				} else if (output[1] instanceof CollidableEdge edgeB) {
					debug(String.format("VE state: %s %s", vertexA, edgeB));
					state = handleVEState(vertexA, edgeB, output);
				} else if (output[1] instanceof CollidableFace faceB) {
					debug(String.format("VF state: %s %s", vertexA, faceB));
					state = handleVFState(vertexA, faceB, output);
				}
			} else if (output[0] instanceof CollidableEdge edgeA) {
				if (output[1] instanceof CollidableEdge edgeB) {
					debug(String.format("EE state: %s %s", edgeA, edgeB));
					state = handleEEState(edgeA, edgeB, output);
				} else if (output[1] instanceof CollidableFace faceB) {
					debug(String.format("EF state: %s %s", edgeA, faceB));
					state = handleEFState(edgeA, faceB, output);
				} else {
					debug("messed up edge");
					return null;
				}
			} else {
				debug("messed up face");
				return null;
			}
//			debug("feature changed to: " + Arrays.toString(output));
//			debug("state is now: " + state);
//			debug("");
		}

		VClipInfo info = new VClipInfo(output, state);
//		if (info.pair[0].getParent() == collidableA.getCollider()) {
//			collidableA.onVClip(info.pair[0]);
//			collidableB.onVClip(info.pair[1]);
//		} else {
//			collidableA.onVClip(info.pair[1]);
//			collidableB.onVClip(info.pair[0]);
//		}
		return info;
	}

	public static boolean validatePair(Feature[] result) {
		if (result[0] instanceof CollidableVertex)
			return false;
		if (result[0] instanceof CollidableEdge edge) {
			if (result[1] instanceof CollidableVertex vertex) {
				result[0] = vertex;
				result[1] = edge;
				return true;
			}
		}
		if (result[0] instanceof CollidableFace face) {
			Feature b = result[1];
			result[0] = b;
			result[1] = face;
			return true;
		}
		return false;
	}

	public static ClipEdgeState clipEdgeRegion(double[] ls, Feature[] ns, CollidableEdge e, VoronoiRegion region) {
		for (VoronoiPlane plane : region.getPlanes()) {
			ClipEdgeState state = clipEdgePlane(ls, ns, e, plane);
			if (state != ClipEdgeState.COMPLETE)
				return state;
		}
		return ClipEdgeState.COMPLETE;
	}

	public static ClipEdgeState clipEdgePlane(double[] ls, Feature[] ns, CollidableEdge e, VoronoiPlane plane) {
		Feature n = plane.getNeighbor();
		double tailDistance = plane.signedDistance(e.evalAt(ls[0]));
		double headDistance = plane.signedDistance(e.evalAt(ls[1]));

		double l = tailDistance / (tailDistance - headDistance);
		if (tailDistance < 0 && headDistance < 0) {
			ns[0] = n;
			ns[1] = n;
			return ClipEdgeState.SIMPLE;
		} else if (tailDistance < 0) {
			if (l > ls[0]) {
				ls[0] = l;
				ns[0] = n;
				if (ls[0] > ls[1])
					return ClipEdgeState.COMPOUND;
			}
		} else if (headDistance < 0) {
			if (l < ls[1]) {
				ls[1] = l;
				ns[1] = n;
				if (ls[0] > ls[1])
					return ClipEdgeState.COMPOUND;
			}
		}
		return ClipEdgeState.COMPLETE;
	}

	public static boolean postClipCheck(double[] ls, Feature[] ns, Feature[] result, int xIndex) {
		Feature x = result[xIndex];
		CollidableEdge e = (CollidableEdge) result[1 - xIndex];
		Feature s0 = x, s1 = x;
		if (x instanceof CollidableEdge) {
			s0 = ns[0];
			s1 = ns[1];
		}
		if (ns[0] != null && dPrime(e, s0, ls[0]) > 0) {
			result[xIndex] = ns[0];
			return true;
		} else if (ns[1] != null && dPrime(e, s1, ls[1]) < 0) {
			result[xIndex] = ns[1];
			return true;
		}
		return false;
	}

	public static double d(CollidableEdge e, CollidableFace f, double l) {
		return f.getRegion().getSupportPlane().signedDistance(e.evalAt(l));
	}

	public static int dPrime(CollidableEdge e, Feature x, double l) {
		if (x instanceof CollidableVertex v)
			return dPrime(e, v, l);
		if (x instanceof CollidableFace f)
			return dPrime(e, f, l);
		return 0;
	}

	public static int dPrime(CollidableEdge e, CollidableVertex v, double l) {
		return (int) Math.signum(e.dot(e.evalAt(l).sub(v)));
	}

	public static int dPrime(CollidableEdge e, CollidableFace f, double l) {
		double distance = f.getRegion().getSupportPlane().signedDistance(e.evalAt(l));
		return (int) (Math.signum(e.dot(f.getNormal())) * Math.signum(distance));
	}

	public static State handleLocalMinimum(Feature[] result) {
		double maxSignedDistance = Double.NEGATIVE_INFINITY;
		CollidableFace closestFace = null;
		for (CollidableFace face : ((CollidablePolyhedron) result[1].getOwner()).getFaces()) {
			VoronoiPlane facePlane = face.getRegion().getSupportPlane();
			double distance = facePlane.signedDistance((CollidableVertex) result[0]);
			if (distance > maxSignedDistance) {
				maxSignedDistance = distance;
				closestFace = face;
			}
		}
		if (maxSignedDistance <= 0)
			return State.PENETRATION;
		result[1] = closestFace;
		return State.CONTINUE;
	}

	public static State handleVVState(CollidableVertex v1, CollidableVertex v2, Feature[] result) {
		if (handleVVStateSub(result, false) == State.CONTINUE)
			return State.CONTINUE;
		if (handleVVStateSub(result, true) == State.CONTINUE)
			return State.CONTINUE;
		return State.DONE;
	}

	public static State handleVVStateSub(Feature[] result, boolean flipped) {
		int i = flipped ? 1 : 0;
		CollidableVertex a = (CollidableVertex) result[i];
		CollidableVertex b = (CollidableVertex) result[1 - i];
		for (VoronoiPlane plane : a.getRegion().getPlanes()) {
			double distance = plane.signedDistance(b);
			if (distance < 0) {
				result[i] = plane.getNeighbor();
				result[1 - i] = b;
				return State.CONTINUE;
			}
		}
		return State.SUB_CONTINUE;
	}

	public static State handleVEState(CollidableVertex v, CollidableEdge e, Feature[] result) {
		for (VoronoiPlane plane : e.getRegion().getPlanes()) {
			double distance = plane.signedDistance(v);
			if (distance < 0) {
				result[1] = plane.getNeighbor();
				return State.CONTINUE;
			}
		}

		double[] ls = { 0, 1 };
		Feature[] ns = new Feature[2];
		clipEdgeRegion(ls, ns, e, v.getRegion());
		if (ns[0] == ns[1] && ns[0] != null) {
			result[0] = ns[0];
			return State.CONTINUE;
		} else {
			if (postClipCheck(ls, ns, result, 0))
				return State.CONTINUE;
		}
		return State.DONE;
	}

	public static State handleVFState(CollidableVertex v, CollidableFace f, Feature[] result) {
		double maximalViolatingDistance = 0;
		VoronoiPlane maximalViolatingPlane = null;
		for (VoronoiPlane plane : f.getRegion().getPlanes()) {
			double distance = plane.signedDistance(v);
//			debug(String.format("%s against %s: %f", v.asVec3(), plane, distance));
			if (distance < maximalViolatingDistance) {
				maximalViolatingDistance = distance;
				maximalViolatingPlane = plane;
			}
		}
		if (maximalViolatingPlane != null) {
			result[1] = maximalViolatingPlane.getNeighbor();
			return State.CONTINUE;
		}

		double aDistance = f.getRegion().getSupportPlane().signedDistance(v);
//		debug(String.format("%s against support %s: %f", v.asVec3(), f.getRegion().getSupportPlane(), aDistance));
		CollidableEdge closerEdge = null;
		for (CollidableEdge e : v.getNeighbors()) {
			CollidableVertex o = e.getHead() == v ? e.getTail() : v;
			double oDistance = f.getRegion().getSupportPlane().signedDistance(o);
//			debug(String.format("%s against support %s: %f", o.asVec3(), f.getRegion().getSupportPlane(), oDistance));
			if (Math.abs(oDistance) < Math.abs(aDistance))
				closerEdge = e;
		}
		if (closerEdge != null) {
			result[0] = closerEdge;
			return State.CONTINUE;
		}
		if (aDistance > 0)
			return State.DONE;

		return handleLocalMinimum(result);
	}

	public static State handleEEState(CollidableEdge e1, CollidableEdge e2, Feature[] result) {
		if (handleEEStateSub(result, false) == State.CONTINUE)
			return State.CONTINUE;
		if (handleEEStateSub(result, true) == State.CONTINUE)
			return State.CONTINUE;
		return State.DONE;
	}

	public static State handleEEStateSub(Feature[] result, boolean flipped) {
		int i = flipped ? 1 : 0;
		CollidableEdge boundingEdge = (CollidableEdge) result[i];

		double[] ls = { 0, 1 };
		Feature[] ns = new Feature[2];

		if (handleEEStateSub2(ls, ns, result, flipped, boundingEdge.getRegion().getVEPlanes()) == State.CONTINUE)
			return State.CONTINUE;
		if (handleEEStateSub2(ls, ns, result, flipped, boundingEdge.getRegion().getFEPlanes()) == State.CONTINUE)
			return State.CONTINUE;

		return State.SUB_CONTINUE;
	}

	public static State handleEEStateSub2(double[] ls, Feature[] ns, Feature[] result, boolean flipped,
			VoronoiPlane[] planes) {
		int xIndex = flipped ? 1 : 0;
		CollidableEdge clippedEdge = (CollidableEdge) result[1 - xIndex];

		for (VoronoiPlane plane : planes) {
			if (clipEdgePlane(ls, ns, clippedEdge, plane) == ClipEdgeState.SIMPLE) {
				result[xIndex] = plane.getNeighbor();
				return State.CONTINUE;
			} else {
				if (postClipCheck(ls, ns, result, xIndex))
					return State.CONTINUE;
			}
		}
		return State.SUB_CONTINUE;
	}

	public static State handleEFState(CollidableEdge e, CollidableFace f, Feature[] result) {
		double[] ls = { 0, 1 };
		Feature[] ns = new Feature[2];
		if (clipEdgeRegion(ls, ns, e, f.getRegion()) != ClipEdgeState.COMPLETE) {
			result[1] = ns[0]; // ?
			return State.CONTINUE;
		}
		double dUnder = f.getRegion().getSupportPlane().signedDistance(e.evalAt(ls[0]));
		double dUpper = f.getRegion().getSupportPlane().signedDistance(e.evalAt(ls[1]));
		if (dUnder * dUpper < 0)
			return State.PENETRATION;
		if (dPrime(e, f, ls[0]) >= 0)
			if (ns[0] != null)
				result[1] = ns[0];
			else
				result[0] = e.getTail();
		else if (ns[1] != null)
			result[1] = ns[1];
		else
			result[0] = e.getHead();
		return State.CONTINUE;
	}
}
