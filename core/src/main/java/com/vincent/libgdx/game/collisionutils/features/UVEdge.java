package com.vincent.libgdx.game.collisionutils.features;

import com.vincent.libgdx.game.collisionutils.shapes.UVPolyhedron;

public class UVEdge extends BaseEdge<UVPolyhedron, UVVertex> {
	public UVEdge(UVPolyhedron parent, int ID, UVVertex tail, UVVertex head) {
		super(parent, ID, tail, head);
	}
}
