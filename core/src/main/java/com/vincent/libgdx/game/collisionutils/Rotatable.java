package com.vincent.libgdx.game.collisionutils;

import com.vincent.libgdx.game.collisionutils.primitives.Quat;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public interface Rotatable {
	public Quat getQuat();

	public void setRotation(Vec3 r);

	public default void addRotation(Vec3 r) {
		setRotation(getQuat().getRotation().add(r));
	}
}
