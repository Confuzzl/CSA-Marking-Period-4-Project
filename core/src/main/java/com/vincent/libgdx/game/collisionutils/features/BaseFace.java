package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron;

public abstract class BaseFace<T extends BasePolyhedron> extends Feature<T> {
	private Vec3 normal;

	public BaseFace(T parent, int ID) {
		super(parent, ID);
	}

	protected abstract Vec3 e0();

	protected abstract Vec3 e1();

	public void initializeNormal() {
		normal = e0().cross(e1()).getNormalize();
	}

	public Vec3 getNormal() {
		return normal;
	}

	@Override
	public String toString() {
		return String.format("%s #%d of %s: %s", getClass().getSimpleName(), getID(), getOwner().getID(), normal);
	}
}
