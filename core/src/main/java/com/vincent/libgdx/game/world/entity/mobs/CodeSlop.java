package com.vincent.libgdx.game.world.entity.mobs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;
import com.vincent.libgdx.game.collisionutils.shapes.BoxFactory;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;

public class CodeSlop extends HostileEntity {
	private static final CollidablePolyhedron DEFAULT_HITBOX = BoxFactory.collidableBox(0, 0, 0, 5, 7, 5, Vec3.ZERO,
			CoordinateCenter.atFoot());

	public CodeSlop(World world, Vec3 position) {
		this(world, position, true);
	}

	public CodeSlop(World world, Vec3 position, boolean active) {
		super(world, position, DEFAULT_HITBOX, CollisionType.NORMAL, false, "mob_code_slop", 10, 10, 4, 100, 1, 6, 2,
				0.25, null, 1, active);
	}
}
