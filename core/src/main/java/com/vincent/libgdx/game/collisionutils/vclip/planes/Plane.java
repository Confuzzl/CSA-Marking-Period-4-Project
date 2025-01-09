package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;

public class Plane {
	private VectorLike point;
	private VectorLike normal;

	public Plane(VectorLike point, VectorLike normal) {
		this.point = point;
		this.normal = normal.getNormalize();
	}

	public static double signedDistance(VectorLike p, VectorLike n, VectorLike v) {
		return n.dot(v.sub(p));
	}

	public static Vec3 reflectToBehind(VectorLike p, Vec3 n, Vec3 v) {
		Vec3 norm = n.getNormalize();
		double distance = signedDistance(p, norm, v);
		if (distance > 0)
			return v.sub(norm.mult(2 * distance));
		return v;
	}

	public double signedDistance(VectorLike v) {
		return signedDistance(point, normal, v);
	}

	public Vec3 getPoint() {
		return point.asVec3();
	}

	public Vec3 getNormal() {
		return normal.asVec3();
	}
}
