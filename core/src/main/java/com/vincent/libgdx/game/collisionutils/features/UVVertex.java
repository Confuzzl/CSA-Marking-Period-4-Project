package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.UVPolyhedron;

public class UVVertex extends BaseVertex<UVPolyhedron> {
	public UVVertex(UVPolyhedron parent, int ID, Vec3 position) {
		super(parent, ID, position);
	}
}
