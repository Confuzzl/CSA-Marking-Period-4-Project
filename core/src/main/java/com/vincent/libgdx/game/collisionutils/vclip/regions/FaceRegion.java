package com.vincent.libgdx.game.collisionutils.vclip.regions;

import com.vincent.libgdx.game.collisionutils.features.CollidableFace;
import com.vincent.libgdx.game.collisionutils.vclip.planes.FEPlaneFace;
import com.vincent.libgdx.game.collisionutils.vclip.planes.SupportPlane;

public class FaceRegion extends VoronoiRegion<CollidableFace, FEPlaneFace> {
	private SupportPlane supportPlane;

	public FaceRegion(CollidableFace f) {
		super(f);
		supportPlane = new SupportPlane(f);
		FEPlaneFace[] planes = new FEPlaneFace[f.getEdges().length];
		for (int i = 0; i < planes.length; i++)
			planes[i] = new FEPlaneFace(this, f, f.getEdges()[i]);
		setPlanes(planes);
	}

	public SupportPlane getSupportPlane() {
		return supportPlane;
	}
}
