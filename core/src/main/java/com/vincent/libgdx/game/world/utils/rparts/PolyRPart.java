package com.vincent.libgdx.game.world.utils.rparts;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.UVPolyhedron;

public abstract class PolyRPart extends RPart {
	private UVPolyhedron polyhedron;

	public PolyRPart(Vec3 position, UVPolyhedron polyhedron) {
		super(position);
		this.polyhedron = polyhedron;
	}

	public UVPolyhedron getPolyhedron() {
		return polyhedron;
	}

//	@Override
//	public Quat getQuat() {
//		return polyhedron.getQuat();
//	}
//
//	@Override
//	public void setRotation(Vec3 r) {
//		super.setRotation(r);
//		polyhedron.setRotation(r);
//	}
}
