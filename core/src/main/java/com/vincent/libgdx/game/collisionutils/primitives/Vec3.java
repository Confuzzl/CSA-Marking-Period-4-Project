package com.vincent.libgdx.game.collisionutils.primitives;

import java.util.Objects;

import com.badlogic.gdx.math.Vector3;
import com.vincent.libgdx.game.collisionutils.Generatable;

public class Vec3 implements VectorLike, Generatable {
	private double x, y, z;
	private double magnitude = -1;
	public boolean immutable = false;

	public static final Vec3 ZERO = new Vec3(0, 0, 0, true);
	public static final Vec3 X_PLUS = new Vec3(1, 0, 0, true);
	public static final Vec3 X_MINUS = new Vec3(-1, 0, 0, true);
	public static final Vec3 Y_PLUS = new Vec3(0, 1, 0, true);
	public static final Vec3 Y_MINUS = new Vec3(0, -1, 0, true);
	public static final Vec3 Z_PLUS = new Vec3(0, 0, 1, true);
	public static final Vec3 Z_MINUS = new Vec3(0, 0, -1, true);

	public Vec3(Vector3 v) {
		this(v.x, v.y, v.z);
	}

	public Vec3() {
		this(0, 0, 0);
	}

	public Vec3(double[] xyz) {
		this(xyz[0], xyz[1], xyz[2]);
	}

	public Vec3(double x, double y, double z) {
		this(x, y, z, false);
	}

	public Vec3(double x, double y, double z, boolean immutable) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.immutable = immutable;
	}

	public void reset() {
		set(0, 0, 0);
	}

	public void set(Vec3 v) {
		set(v.x, v.y, v.z);
	}

	public void set(Vector3 v) {
		set(v.x, v.y, v.z);
	}

	public void set(double x, double y, double z) {
		if (immutable) {
			System.err.println("modifying immutable vec3");
			return;
		}
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void setAndLock(Vec3 v) {
		set(v.x, v.y, v.z);
		this.immutable = true;
	}

	public void setAndLock(double x, double y, double z) {
		set(x, y, z);
		immutable = true;
	}

	@Override
	public double getMagnitude() {
		if (magnitude < 0)
			magnitude = VectorLike.super.getMagnitude();
		return magnitude;
	}

	@Override
	public double x() {
		return x;
	}

	@Override
	public double y() {
		return y;
	}

	@Override
	public double z() {
		return z;
	}

	public Vec3 copy() {
		return new Vec3(x, y, z);
	}

	public void reverse() {
		multEquals(-1);
	}

	public void normalize() {
		normalizeTo(1);
	}

	public void normalizeTo(double m) {
		if (equals(Vec3.ZERO))
			return;
		divEquals(getMagnitude() / m);
	}

	public void crossEquals(VectorLike other) {
		double tempX = y * other.asVec3().z - z * other.asVec3().y;
		double tempY = z * other.asVec3().x - x * other.asVec3().z;
		double tempZ = x * other.asVec3().y - y * other.asVec3().x;
		x = tempX;
		y = tempY;
		z = tempZ;
	}

	public void multEquals(double scl) {
		x *= scl;
		y *= scl;
		z *= scl;
	}

	public void divEquals(double scl) {
		if (scl == 0) {
			System.err.println("vec3 divide by 0");
			return;
		}
		x /= scl;
		y /= scl;
		z /= scl;
	}

	public void addEquals(VectorLike other) {
		x += other.asVec3().x;
		y += other.asVec3().y;
		z += other.asVec3().z;
	}

	public void addEquals(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
	}

	public void subEquals(VectorLike other) {
		x -= other.asVec3().x;
		y -= other.asVec3().y;
		z -= other.asVec3().z;
	}

	public void subEquals(double x, double y, double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
	}

	public void rotateAroundCentroid(VectorLike c, Vec3 r) {
		set(getRotateAroundCentroid(c, r));
	}

	public void rotateAroundAxis(VectorLike axis, double deg) {
		set(getRotateAroundAxis(axis, deg));
	}

	public void rotateQuaternion(Quat q) {
		set(getRotateQuaternion(q));
	}

	public void rotateXYZExtrinsic(Vec3 r) {
		set(getRotateXYZExtrinsic(r));
	}

	public void rotateXYZExtrinsic(double x, double y, double z) {
		set(getRotateXYZExtrinsic(x, y, z));
	}

	public static Vec3 randomVec3(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		return new Vec3(range(minX, maxX), range(minY, maxY), range(minZ, maxZ));
	}

	public static Vec3 randomVec3(double r) {
		return randomVec3(-r, -r, -r, r, r, r);
	}

	private static double range(double min, double max) {
		return Math.random() * (max - min) + min;
	}

	@Override
	public String toString() {
		return String.format("[%.2f, %.2f, %.2f]", x, y, z);
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Vec3 o) {
			return o.x == this.x && o.y == this.y && o.z == this.z;
		}
		return false;
	}

	@Override
	public Vec3 asVec3() {
		return this;
	}

	@Override
	public String generate() {
		return String.format("new Vec3(%f,%f,%f)", x, y, z);
	}
}
