package com.vincent.libgdx.game.collisionutils;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public interface Movable {
	public Vec3 getGlobalPosition();

	public Vec3 getLocalPosition();

	public default void setLocalPosition(Vec3 v) {
		getLocalPosition().set(v);
	}

//	public default void resetLocalPosition() {
//		setLocalPosition(Vec3.ZERO);
//	}

	public default void addLocalPosition(Vec3 v) {
		setLocalPosition(getLocalPosition().add(v));
	}
}
