package com.vincent.libgdx.game.collisionutils.vclip.planes;

import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.features.Feature;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.vclip.regions.FaceRegion;

public class SupportPlane extends VoronoiPlane<Feature, FaceRegion> {
	private CollidableFace face;

	public SupportPlane(CollidableFace f) {
		super(f.getRegion(), null, f.sampleVertex());
		face = f;
	}

	@Override
	public Vec3 getNormal() {
		return face.getNormal();
	}
}
