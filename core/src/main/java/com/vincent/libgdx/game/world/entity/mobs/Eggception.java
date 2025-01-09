package com.vincent.libgdx.game.world.entity.mobs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;
import com.vincent.libgdx.game.collisionutils.shapes.BoxFactory;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;

public class Eggception extends HostileEntity {
	private static final CollidablePolyhedron DEFAULT_HITBOX = BoxFactory.collidableBox(0, 0, 0, 4, 4, 4, Vec3.ZERO,
			CoordinateCenter.atFoot());

	public Eggception(World world, Vec3 position) {
		this(world, position, true);
	}

	public Eggception(World world, Vec3 position, boolean active) {
		super(world, position, DEFAULT_HITBOX, CollisionType.NORMAL, true, "mob_eggception", 8, 12, 0.4, 500, 3, 2, 5,
				0.4, Weapon.TRI_CATCHER, 2.5, active);
	}
}
