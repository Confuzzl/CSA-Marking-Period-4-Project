package com.vincent.libgdx.game.world.entity.mobs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.World;

public class Boss extends HostileEntity {
	public Boss(World world, Vec3 position, CollidablePolyhedron hitbox, String decalName, double decalWidth,
			double decalHeight, double decalOffset, int health, int baseDamage, double rayOffset, double cooldown,
			double speed, boolean active) {
		super(world, position, hitbox, CollisionType.NORMAL, false, decalName, decalWidth, decalHeight, decalOffset,
				health, baseDamage, rayOffset, cooldown, speed, null, 0, active);
	}

	@Override
	public void onDeath() {
		super.onDeath();
		System.out.println("you win");
	}
}
