package com.vincent.libgdx.game.world.entity.npcs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Player;

public class Wave4NPC extends NPC {
	public Wave4NPC(World world, Vec3 position) {
		super(world, position, Player.DEFAULT_HITBOX, "npc_link", 2.5, 8, 4, 15,
				"[RED]Eggceptions[] have suddenly hatched out of nowhere! Their shells seem near impenetrable, even against my [SKY]heavy hitting [GREEN]Tri Catcher[], ",
				"Each [RED]Eggception[] has this big, oozing eye coming out from a crack in their shell. Maybe that could be their weakness...");
	}
}
