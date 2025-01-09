package com.vincent.libgdx.game.world.entity.npcs;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Player;

public class Wave2NPC extends NPC {

	public Wave2NPC(World world, Vec3 position) {
		super(world, position, Player.DEFAULT_HITBOX, "npc_axel", 4, 8, 4, 15,
				"Thank god someone's here! A swarm of [RED]bin-bugs[] are heading our way. My [GREEN]The More-Gun[] seems to be effective against them, and luckily for you I have a spare.",
				"It fires [SKY]multiple pellets at once[], which will help shatter the [RED]bin-bugs'[] shells.");
	}

}
