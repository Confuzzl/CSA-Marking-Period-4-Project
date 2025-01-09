package com.vincent.libgdx.game.world.entity.mobs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;
import com.vincent.libgdx.game.collisionutils.shapes.BoxFactory;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;

public class BinBug extends HostileEntity {
	private static final CollidablePolyhedron DEFAULT_HITBOX = BoxFactory.collidableBox(0, 0, 0, 4, 10, 4, Vec3.ZERO,
			CoordinateCenter.atFoot());

	public BinBug(World world, Vec3 position) {
		this(world, position, true);
	}

	public BinBug(World world, Vec3 position, boolean active) {
		super(world, position, DEFAULT_HITBOX, CollisionType.NORMAL, false, "mob_bin_bug", 8, 12, 6, 250, 2, 10, 3, 0.2,
				Weapon.THE_MORE_GUN, 2, active);
	}
}
