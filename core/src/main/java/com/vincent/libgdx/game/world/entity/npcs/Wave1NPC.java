package com.vincent.libgdx.game.world.entity.npcs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Player;

public class Wave1NPC extends NPC {
	public Wave1NPC(World world, Vec3 position) {
		super(world, position, Player.DEFAULT_HITBOX, "npc_astolfo", 3, 8, 4, 15,
				"There's [RED]code slop[] on their way here! I tried fighting them off, but there were too many and I was forced to retreat back here.",
				"They're probably following me here as we speak. I'm too weak to fight now, so just take my [GREEN]M4Matter[]. This thing is a [SKY]jack of all trades[]. I'm sure it'll serve you well.");
	}
}
