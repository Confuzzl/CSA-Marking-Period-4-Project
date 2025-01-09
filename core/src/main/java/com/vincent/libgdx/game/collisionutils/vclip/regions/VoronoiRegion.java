package com.vincent.libgdx.game.collisionutils.vclip.regions;

import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.primitives.VectorLike;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VoronoiPlane;

public abstract class VoronoiRegion<ParentType extends Feature, PlaneType extends VoronoiPlane> {
	private ParentType parent;
	private PlaneType[] planes;

	protected VoronoiRegion(ParentType p) {
		parent = p;
	}

	protected void setPlanes(PlaneType[] p) {
		planes = p;
	}

	public ParentType getFeature() {
		return parent;
	}

	public PlaneType[] getPlanes() {
		return planes;
	}

	public boolean pointInRegion(VectorLike v) {
		for (VoronoiPlane plane : getPlanes())
			if (plane.signedDistance(v) < 0)
				return false;
		return true;
	}
}
