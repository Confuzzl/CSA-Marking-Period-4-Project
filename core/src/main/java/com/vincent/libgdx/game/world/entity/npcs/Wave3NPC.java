package com.vincent.libgdx.game.world.entity.npcs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Player;

public class Wave3NPC extends NPC {
	public Wave3NPC(World world, Vec3 position) {
		super(world, position, Player.DEFAULT_HITBOX, "npc_steve", 4, 8, 4, 15,
				"We're in real big trouble! [RED]Booleons[] are starting to phase past our defenses. I was given this prototype called [GREEN]DBG9000[], but I'm having trouble using it.",
				"It fires really quick [SKY]plasma beams[] that should hurt the [RED]Booleons[] but it's too heavy for me.");
	}
}
