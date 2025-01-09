package com.vincent.libgdx.game.collisionutils;

import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.collisionutils.vclip.planes.SupportPlane;

public interface Collidable extends Movable/* , Rotatable */ {
	public CollidablePolyhedron getCollider();

//	@Override
//	public default Quat getQuat() {
//		return getCollider().getQuat();
//	}
//
//	@Override
//	public default void setRotation(Vec3 r) {
//		getQuat().setRotation(r);
//		getCollider().setRotation(r);
//	}

//	public default double rayIntersectClosest(Ray ray) {
//		return rayIntersectClosest(ray.getOrigin(), ray.getDirection());
//	}
//
//	public default boolean rayIntersect(Ray ray) {
//		return rayIntersect(ray.getOrigin(), ray.getDirection());
//	}

	public default boolean rayIntersect(Vec3 origin, Vec3 direction) {
		CollidablePolyhedron p = getCollider();
		for (CollidableFace face : p.getFaces()) {
			if (direction.dot(face.getNormal()) > 0)
				continue;
			SupportPlane facePlane = face.getRegion().getSupportPlane();
			double numer = facePlane.signedDistance(origin);
			double denom = facePlane.getNormal().dot(direction);
			if (numer * denom < 0) {
				double l = -numer / denom;
				Vec3 intersection = origin.add(direction.mult(l));
				if (face.getRegion().pointInRegion(intersection))
					return true;
			}
		}
		return false;
	}

	public default double rayIntersectClosest(Vec3 origin, Vec3 direction) {
		CollidablePolyhedron p = getCollider();
		double minL = Double.POSITIVE_INFINITY;
		for (CollidableFace face : p.getFaces()) {
			SupportPlane facePlane = face.getRegion().getSupportPlane();
			double numer = facePlane.signedDistance(origin);
			double denom = facePlane.getNormal().dot(direction);
			if (numer * denom < 0) {
				double l = -numer / denom;
				Vec3 intersection = origin.add(direction.mult(l));
				if (face.getRegion().pointInRegion(intersection))
					minL = Math.min(l, minL);
			}
		}
		return minL;
	}

//	public default VClipInfo vClip(Collidable other) {
//		return VClip.closestFeaturePair(this, other);
//	}
//
//	public default VClipInfo vClip(Collidable other, Feature[] startingPair) {
//		return VClip.closestFeaturePair(this, other, startingPair[0], startingPair[1]);
//	}
}
