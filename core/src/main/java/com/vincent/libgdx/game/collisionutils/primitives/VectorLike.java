package com.vincent.libgdx.game.collisionutils.primitives;

import com.badlogic.gdx.math.Vector3;

public interface VectorLike {
	public Vec3 asVec3();

	public default Vector3 toGDXVector3() {
		return new Vector3((float) x(), (float) y(), (float) z());
	}

	public default double x() {
		return asVec3().x();
	}

	public default double y() {
		return asVec3().y();
	}

	public default double z() {
		return asVec3().z();
	}

	public default Vec3 add(VectorLike other) {
		Vec3 s = asVec3();
		Vec3 o = other.asVec3();
		return new Vec3(s.x() + o.x(), s.y() + o.y(), s.z() + o.z());
	}

	public default Vec3 add(double x, double y, double z) {
		Vec3 s = asVec3();
		return new Vec3(s.x() + x, s.y() + y, s.z() + z);
	}

	public default Vec3 sub(VectorLike other) {
		Vec3 s = asVec3();
		Vec3 o = other.asVec3();
		return new Vec3(s.x() - o.x(), s.y() - o.y(), s.z() - o.z());
	}

	public default Vec3 sub(double x, double y, double z) {
		Vec3 s = asVec3();
		return new Vec3(s.x() - x, s.y() - y, s.z() - z);
	}

	public default Vec3 mult(double amt) {
		Vec3 s = asVec3();
		return new Vec3(s.x() * amt, s.y() * amt, s.z() * amt);
	}

	public default Vec3 div(double amt) {
		Vec3 s = asVec3();
		if (amt == 0)
			return s;
		return new Vec3(s.x() / amt, s.y() / amt, s.z() / amt);
	}

	public default double getMagnitude() {
		Vec3 s = asVec3();
		return Math.sqrt(s.x() * s.x() + s.y() * s.y() + s.z() * s.z());
	}

	public default Vec3 getNormalize() {
		return div(getMagnitude());
	}

	public default Vec3 getNormalizeTo(double m) {
		return div(getMagnitude() / m);
	}

	public default Vec3 getReverse() {
		return asVec3().mult(-1);
	}

	public default double dot(VectorLike other) {
		Vec3 s = asVec3();
		Vec3 o = other.asVec3();
		return s.x() * o.x() + s.y() * o.y() + s.z() * o.z();
	}

	public default Vec3 cross(VectorLike other) {
		Vec3 s = asVec3();
		Vec3 o = other.asVec3();
		return new Vec3(s.y() * o.z() - s.z() * o.y(), s.z() * o.x() - s.x() * o.z(), s.x() * o.y() - s.y() * o.x());
	}

	public default Vec3 getRotateAroundCentroid(VectorLike c, Vec3 r) {
		double sinX = Math.sin(Math.toRadians(r.x())), cosX = Math.cos(Math.toRadians(r.x()));
		double sinY = Math.sin(Math.toRadians(r.y())), cosY = Math.cos(Math.toRadians(r.y()));
		double sinZ = Math.sin(Math.toRadians(r.z())), cosZ = Math.cos(Math.toRadians(r.z()));
		Vec3 n = asVec3().copy();
		// x rotate y, z
		n.set(n.x(), (n.y() - c.y()) * cosX - (n.z() - c.z()) * sinX + c.y(),
				(n.y() - c.y()) * sinX + (n.z() - c.z()) * cosX + c.z());
		// y rotate z, x
		n.set((n.z() - c.z()) * sinY + (n.x() - c.x()) * cosY + c.x(), n.y(),
				(n.z() - c.z()) * cosY - (n.x() - c.x()) * sinY + c.z());
		// z rotate x, y
		n.set((n.x() - c.x()) * cosZ - (n.y() - c.y()) * sinZ + c.x(),
				(n.x() - c.x()) * sinZ + (n.y() - c.y()) * cosZ + c.y(), n.z());
		return n;
	}

	public default Vec3 getRotateAroundAxis(VectorLike axis, double deg) {
		Vec3 s = asVec3();
		Vec3 norm = axis.getNormalize();
		double sin = Math.sin(Math.toRadians(deg));
		double cos = Math.cos(Math.toRadians(deg));
		return s.mult(cos).add(norm.cross(s).mult(sin)).add(norm.mult(norm.dot(s) * (1 - cos)));
	}

	public default Vec3 getRotateQuaternion(Quat q) {
		Quat p = new Quat(x(), y(), z(), 0);
		return q.mult(p).mult(q.getConjugate()).getXYZ();
	}

	public default Vec3 getRotateXYZExtrinsic(Vec3 r) {
		return getRotateXYZExtrinsic(r.x(), r.y(), r.z());
	}

	public default Vec3 getRotateXYZExtrinsic(double x, double y, double z) {
		return asVec3().getRotateAroundAxis(Vec3.X_PLUS, x).getRotateAroundAxis(Vec3.Y_PLUS, y)
				.getRotateAroundAxis(Vec3.Z_PLUS, z);
	}

	public default boolean isCollinear(VectorLike other) {
		return cross(other).equals(Vec3.ZERO);
	}

	public default boolean isSameDirection(VectorLike other) {
		return dot(other) > 0;
	}

	public default double[] toArray() {
		Vec3 s = asVec3();
		return new double[] { s.x(), s.y(), s.z() };
	}
}
