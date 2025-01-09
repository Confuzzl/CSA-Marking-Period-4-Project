package com.vincent.libgdx.game.world.utils;

import com.vincent.libgdx.game.collisionutils.Movable;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.utils.rparts.RPart;

public interface Nestable extends Movable, Renderable {
	@Override
	public default void setLocalPosition(Vec3 v) {
		Movable.super.setLocalPosition(v);
		getModelInstance().transform.setTranslation((float) v.x(), (float) v.y(), (float) v.z());
	}

	public default void addRPart(RPart rPart) {
		rPart.setParent(this);
		rPart.init();
	}

	public abstract void refreshInstance();

	public abstract void kill();
}
