package com.vincent.libgdx.game.collisionutils.primitives;

import com.badlogic.gdx.math.Quaternion;

public class Quat {
	private double x, y, z, w;
//	private Vec3 axis;
//	private double deg;
	private Vec3 rotation = new Vec3();

	public Quat() {
		set(0, 0, 0, 1);
	}

	public Quat(double x, double y, double z, double w) {
		set(x, y, z, w);
	}

	public Quat(double x, double y, double z) {
		Quat qX = new Quat(Vec3.X_PLUS, x);
		Quat qY = new Quat(Vec3.Y_PLUS, y);
		Quat qZ = new Quat(Vec3.Z_PLUS, z);
		set(qZ.mult(qY).mult(qX));
	}

	public Quat(Vec3 r) {
		this(r.x(), r.y(), r.z());
	}

	public Quat(VectorLike axis, double deg) {
		double s = Math.sin(Math.toRadians(deg / 2));
		set(s * axis.x(), s * axis.y(), s * axis.z(), Math.cos(Math.toRadians(deg / 2)));
	}

	private void set(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;

		rotation.set(asXYZExtrinsicTateBryan());
	}

	private void set(Quat q) {
		set(q.x, q.y, q.z, q.w);
	}

	public Quat getConjugate() {
		return new Quat(-x, -y, -z, w);
	}

	public Vec3 getXYZ() {
		return new Vec3(x, y, z);
	}

//	private void calculateAxisAngle() {
//		double theta = 2 * Math.acos(w);
//		axis = theta == 0 ? Vec3.X_PLUS.copy() : new Vec3(x, y, z).div(Math.sin(theta / 2));
//		deg = Math.toDegrees(theta);
//	}

//	public Vec3 getAxis() {
//		return axis;
//	}
//
//	public double getDegrees() {
//		return deg;
//	}

	public Vec3 getRotation() {
		return rotation;
	}

	public Quat mult(Quat other) {
		return new Quat(this.w * other.x + this.x * other.w + this.y * other.z - this.z * other.y,
				this.w * other.y + this.y * other.w + this.z * other.x - this.x * other.z,
				this.w * other.z + this.z * other.w + this.x * other.y - this.y * other.x,
				this.w * other.w - this.x * other.x - this.y * other.y - this.z * other.z);
	}

	private Vec3 asXYZExtrinsicTateBryan() {
		double a = w - y;
		double b = x + z;
		double c = y + w;
		double d = z - x;

		double theta1, theta3;
		double theta2 = Math.acos(2 * ((a * a + b * b) / (a * a + b * b + c * c + d * d)) - 1);
		double thetaPlus = Math.atan2(b, a);
		double thetaMinus = Math.atan2(d, c);
		if (theta2 == 0) {
			theta1 = 0;
			theta3 = 2 * thetaPlus - theta1;
		} else if (theta2 == Math.PI / 2) {
			theta1 = 0;
			theta3 = 2 * thetaMinus + theta1;
		} else {
			theta1 = thetaPlus - thetaMinus;
			theta3 = thetaPlus + thetaMinus;
		}
		theta2 -= Math.PI / 2;
		return new Vec3(Math.toDegrees(theta1), Math.toDegrees(theta2), Math.toDegrees(theta3));
	}

	public void setRotation(Quat q) {
		set(q);
	}

	public void setRotation(VectorLike axis, double deg) {
		setRotation(new Quat(axis, deg));
	}

	public void setRotation(double x, double y, double z) {
		Quat qX = new Quat(Vec3.X_PLUS, x);
		Quat qY = new Quat(Vec3.Y_PLUS, y);
		Quat qZ = new Quat(Vec3.Z_PLUS, z);
		setRotation(qZ.mult(qY).mult(qX));
	}

	public void setRotation(Vec3 r) {
		setRotation(r.x(), r.y(), r.z());
	}

	public void addRotation(Quat q) {
		set(q.mult(this));
	}

	public void addRotation(VectorLike axis, double deg) {
		addRotation(new Quat(axis, deg));
	}

	public void addRotation(double x, double y, double z) {
		Quat qX = new Quat(Vec3.X_PLUS, x);
		Quat qY = new Quat(Vec3.Y_PLUS, y);
		Quat qZ = new Quat(Vec3.Z_PLUS, z);
		addRotation(qZ.mult(qY).mult(qX));
	}

	public void addRotation(Vec3 r) {
		addRotation(r.x(), r.y(), r.z());
	}

	public Quaternion toGDXQuaternion() {
		return new Quaternion((float) x, (float) y, (float) z, (float) w);
	}

	@Override
	public String toString() {
		return String.format("(%.4f, %.4f, %.4f, %.4f)", x, y, z, w);
	}
}
