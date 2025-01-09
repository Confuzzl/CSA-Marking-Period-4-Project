package com.vincent.libgdx.game.collisionutils.vclip.regions;

import com.vincent.libgdx.game.collisionutils.features.CollidableVertex;
import com.vincent.libgdx.game.collisionutils.vclip.planes.VEPlaneVertex;

public class VertexRegion extends VoronoiRegion<CollidableVertex, VEPlaneVertex> {
	public VertexRegion(CollidableVertex v) {
		super(v);
		VEPlaneVertex[] planes = new VEPlaneVertex[v.getNeighbors().length];
		for (int i = 0; i < planes.length; i++)
			planes[i] = new VEPlaneVertex(this, v, v.getNeighbors()[i]);
		setPlanes(planes);
	}
}
