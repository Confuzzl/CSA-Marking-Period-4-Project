package com.vincent.libgdx.game.world.entity.mobs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;
import com.vincent.libgdx.game.collisionutils.shapes.BoxFactory;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;

public class Booleon extends HostileEntity {
	private static final CollidablePolyhedron DEFAULT_HITBOX = BoxFactory.collidableBox(0, 0, 0, 6, 8, 6, Vec3.ZERO,
			CoordinateCenter.atFoot());

	public Booleon(World world, Vec3 position) {
		this(world, position, true);
	}

	public Booleon(World world, Vec3 position, boolean active) {
		super(world, position, DEFAULT_HITBOX, CollisionType.RAY_ONLY, true, "mob_booleon", 8, 10, 4, 350, 4, 3, 4, 0.5,
				Weapon.DBG9000, 3, active);
	}
}
