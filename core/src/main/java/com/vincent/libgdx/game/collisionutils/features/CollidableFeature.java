package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.vclip.regions.VoronoiRegion;

public interface CollidableFeature {
	public VoronoiRegion getRegion();

	public void generateRegion();

	public double distanceTo(VectorLike p);
}
