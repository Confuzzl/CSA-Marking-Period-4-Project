package com.vincent.libgdx.game.world.entity.mobs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;
import com.vincent.libgdx.game.collisionutils.shapes.BoxFactory;
import com.vincent.libgdx.game.world.LegendOfHolmer;
import com.vincent.libgdx.game.world.World;

public class Guardian extends Boss {
	public Guardian(World world, Vec3 position) {
		this(world, position, true);
	}

	public Guardian(World world, Vec3 position, boolean active) {
		super(world, position, BoxFactory.collidableBox(0, 0, 0, 30, 20, 30, Vec3.ZERO, CoordinateCenter.atFoot()),
				"boss_guardian", 50, 25, 12, 5000, 5, 16, 6, 0.1, active);
		LegendOfHolmer.boss = this;
	}
}
