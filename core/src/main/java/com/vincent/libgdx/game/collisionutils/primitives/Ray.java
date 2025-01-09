package com.vincent.libgdx.game.collisionutils.primitives;

public class Ray {
	private Vec3 origin;
	private Vec3 direction;

	public Ray(Vec3 origin, Vec3 direction) {
		this.origin = origin;
		this.direction = direction.getNormalize();
	}

	public void setDirection(Vec3 d) {
		direction.set(d);
	}

	public Vec3 getOrigin() {
		return origin;
	}

	public Vec3 getDirection() {
		return direction;
	}

	public Vec3 evalAt(double l) {
		return origin.add(direction.mult(l));
	}
}
