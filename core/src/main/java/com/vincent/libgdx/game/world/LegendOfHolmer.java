package com.vincent.libgdx.game.world;

import java.util.function.Function;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.vincent.libgdx.game.Assets;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;
import com.vincent.libgdx.game.collisionutils.shapes.BoxFactory;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.entity.Player;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;
import com.vincent.libgdx.game.world.entity.mobs.BinBug;
import com.vincent.libgdx.game.world.entity.mobs.Booleon;
import com.vincent.libgdx.game.world.entity.mobs.Boss;
import com.vincent.libgdx.game.world.entity.mobs.CodeSlop;
import com.vincent.libgdx.game.world.entity.mobs.Eggception;
import com.vincent.libgdx.game.world.entity.mobs.Guardian;
import com.vincent.libgdx.game.world.entity.npcs.Wave1NPC;
import com.vincent.libgdx.game.world.entity.npcs.Wave2NPC;
import com.vincent.libgdx.game.world.entity.npcs.Wave3NPC;
import com.vincent.libgdx.game.world.entity.npcs.Wave4NPC;
import com.vincent.libgdx.game.world.utils.WorldObject;
import com.vincent.libgdx.game.world.utils.rparts.RenderPart;
import com.vincent.libgdx.game.world.utils.rparts.RenderPart.RenderMode;

public class LegendOfHolmer {
	public static enum GameState {
		//@formatter:off
		START(0,SkyColor.DAY), WAVE_1(1,SkyColor.DAY),
		INTERMISSION_1_2(1,SkyColor.CIVIL_TWILIGHT), WAVE_2(2,SkyColor.CIVIL_TWILIGHT), 
		INTERMISSION_2_3(2,SkyColor.NAUTICAL_TWILIGHT), WAVE_3(3,SkyColor.NAUTICAL_TWILIGHT),
		INTERMISSION_3_4(3,SkyColor.ASTRONOMICAL_TWILIGHT), WAVE_4(4,SkyColor.ASTRONOMICAL_TWILIGHT),
		INTERMISSION_4_BOSS(4,SkyColor.NIGHT), BOSS(4,SkyColor.NIGHT),
		TEST(4, SkyColor.DAY),
		ENDING(4,SkyColor.DAY, true), GAME_OVER(0,SkyColor.DAY,true);
		//@formatter:on

		public static enum SkyColor {
			DAY(132, 191, 247), CIVIL_TWILIGHT(62, 128, 212), NAUTICAL_TWILIGHT(174, 83, 88),
			ASTRONOMICAL_TWILIGHT(82, 73, 136), NIGHT(36, 28, 46);

			private int r, g, b;

			private SkyColor(int r, int g, int b) {
				this.r = r;
				this.g = g;
				this.b = b;
			}

			public int r() {
				return r;
			}

			public int g() {
				return g;
			}

			public int b() {
				return b;
			}
		}

		private int weaponFlag;
		private SkyColor skyColor;
		private boolean isScreen;

		private GameState(int weaponFlag, SkyColor skyColor) {
			this(weaponFlag, skyColor, false);
		}

		private GameState(int weaponFlag, SkyColor skyColor, boolean isScreen) {
			this.weaponFlag = weaponFlag;
			this.skyColor = skyColor;
			this.isScreen = isScreen;
		}

		public SkyColor getSkyColor() {
			return skyColor;
		}

		public boolean isScreen() {
			return isScreen;
		}
	}

	public static GameState currentState;
	public static GameState nextState;
	public static World world;
	public static double elapsed;
	public static int enemyCount, enemiesLeft;
	public static int weaponFlag = 0;
	public static Boss boss;

	public static void start() {
		world.setPlayer(new Player(world, new Vec3(-90, 10, 0)));
		setState(GameState.START);

		{
			double size = 200;
			CollidablePolyhedron floor = BoxFactory.collidableBox(-size / 2, -5, -size / 2, size, 5, size, Vec3.ZERO,
					CoordinateCenter.atDefined(Vec3.ZERO));
			new WorldObject(world, Vec3.ZERO, floor, Color.WHITE, new RenderPart(Vec3.ZERO, floor.toUV(),
					RenderMode.NORMAL, Assets.namesToMaterial("", "", "", "", "arena", "")));

			CollidablePolyhedron wall1 = BoxFactory.collidableBox(0, 0, 0, 5, 10, 200, Vec3.ZERO,
					CoordinateCenter.atFoot());
			new WorldObject(world, new Vec3(100, 0, 0), wall1, Color.WHITE, new RenderPart(Vec3.ZERO, wall1.toUV(),
					RenderMode.NORMAL, Assets.namesToMaterial("wall", "", "wall", "", "", "")));
			new WorldObject(world, new Vec3(-100, 0, 0), wall1, Color.WHITE, new RenderPart(Vec3.ZERO, wall1.toUV(),
					RenderMode.NORMAL, Assets.namesToMaterial("wall", "", "wall", "", "", "")));
			CollidablePolyhedron wall2 = BoxFactory.collidableBox(0, 0, 0, 200, 10, 5, Vec3.ZERO,
					CoordinateCenter.atFoot());
			new WorldObject(world, new Vec3(0, 0, 100), wall2, Color.WHITE, new RenderPart(Vec3.ZERO, wall2.toUV(),
					RenderMode.NORMAL, Assets.namesToMaterial("", "wall", "", "wall", "", "")));
			new WorldObject(world, new Vec3(0, 0, -100), wall2, Color.WHITE, new RenderPart(Vec3.ZERO, wall2.toUV(),
					RenderMode.NORMAL, Assets.namesToMaterial("", "wall", "", "wall", "", "")));

			CollidablePolyhedron crate = BoxFactory.collidableBox(0, 0, 0, 10, 10, 10, Vec3.ZERO,
					CoordinateCenter.atFoot());
			for (int i = -1; i < 2; i += 2) {
				for (int j = -1; j < 2; j += 2) {
					new WorldObject(world, new Vec3(i * 70, 0, j * 70), crate, Color.WHITE,
							new RenderPart(Vec3.ZERO, crate.toUV(), RenderMode.NORMAL,
									Assets.namesToMaterial("crate", "crate", "crate", "crate", "crate", "crate")));
				}
			}
			for (int i = -1; i < 2; i += 2) {
				for (int j = -1; j < 2; j += 2) {
					new WorldObject(world, new Vec3(i * 40, 0, j * 40), crate, Color.WHITE,
							new RenderPart(Vec3.ZERO, crate.toUV(), RenderMode.NORMAL,
									Assets.namesToMaterial("crate", "crate", "crate", "crate", "crate", "crate")));
				}
			}

		}
	}

	public static void update() {
		elapsed += Gdx.graphics.getDeltaTime();
		if (enemyCount > 0 && enemiesLeft == 0) {
			enemyCount = 0;
			enemiesLeft = 0;
			setState(nextState);
		}
	}

	public static void toNextState() {
		setState(nextState);
	}

//	private static <T extends HostileEntity> void spawnCluster(Vec3 center, int size, double spacing,
//			Function<Vec3, T> constructor) {
//		if (size % 2 == 0) {
//			for (int i = -size / 2; i < size / 2; i++) {
//				for (int j = -size / 2; j < size / 2; j++) {
//					constructor.apply(center.add((i + 0.5) * spacing, 0, (j + 0.5) * spacing));
//				}
//			}
//		} else {
//			for (int i = -size / 2; i < size / 2 + 1; i++) {
//				for (int j = -size / 2; j < size / 2 + 1; j++) {
//					constructor.apply(center.add(i * spacing, 0, j * spacing));
//				}
//			}
//		}
//	}

	public static void setState(GameState state) {
		currentState = state;
		weaponFlag = state.weaponFlag;
		Weapon weapon = Player.order[weaponFlag];
		weapon.reset();
		world.getPlayer().setWeapon(weapon);

		switch (state) {
		case TEST -> {
			new Guardian(world, Vec3.ZERO, false);
			new CodeSlop(world, new Vec3(30, 0, 30), false);
			new BinBug(world, new Vec3(30, 0, -30), false);
			new Booleon(world, new Vec3(-30, 10, 30), false);
			new Eggception(world, new Vec3(-30, 10, -30), false);
			nextState = null;
		}
		case START -> {
			new Wave1NPC(world, Vec3.ZERO);
			nextState = GameState.WAVE_1;
		}
		case WAVE_1 -> {
			Function<Vec3, CodeSlop> cons = (n) -> new CodeSlop(world, n);
			for (int i = -1; i < 2; i += 2) {
				for (int j = -1; j < 2; j += 2) {
					new CodeSlop(world, new Vec3(90 * i, 1, 90 * j));
				}
			}
			for (int i = -1; i < 2; i += 2) {
				new CodeSlop(world, new Vec3(90 * i, 1, 0));
			}
			for (int i = -1; i < 2; i += 2) {
				new CodeSlop(world, new Vec3(0, 1, 90 * i));
			}

			nextState = GameState.INTERMISSION_1_2;
		}
		case INTERMISSION_1_2 -> {
			new Wave2NPC(world, Vec3.ZERO);
			nextState = GameState.WAVE_2;
		}
		case WAVE_2 -> {
			for (int i = -1; i < 2; i += 2) {
				for (int j = -1; j < 2; j += 2) {
					new CodeSlop(world, new Vec3(55 * i, 1, 55 * j));
				}
			}
			for (int i = -1; i < 2; i += 2) {
				new BinBug(world, new Vec3(90 * i, 1, 0));
			}
			for (int i = -1; i < 2; i += 2) {
				new BinBug(world, new Vec3(0, 1, 90 * i));
			}

			nextState = GameState.INTERMISSION_2_3;
		}
		case INTERMISSION_2_3 -> {
			new Wave3NPC(world, Vec3.ZERO);
			nextState = GameState.WAVE_3;
		}
		case WAVE_3 -> {
			for (int i = -1; i < 2; i += 2) {
				for (int j = -1; j < 2; j += 2) {
					new BinBug(world, new Vec3(55 * i, 1, 55 * j));
				}
			}
			for (int i = -1; i < 2; i += 2) {
				new Booleon(world, new Vec3(90 * i, 10, 0));
			}
			for (int i = -1; i < 2; i += 2) {
				new Booleon(world, new Vec3(0, 10, 90 * i));
			}

			nextState = GameState.INTERMISSION_3_4;
		}
		case INTERMISSION_3_4 -> {
			new Wave4NPC(world, Vec3.ZERO);

			nextState = GameState.WAVE_4;

		}
		case WAVE_4 -> {
			for (int i = -1; i < 2; i += 2) {
				for (int j = -1; j < 2; j += 2) {
					new Booleon(world, new Vec3(55 * i, 10, 55 * j));
				}
			}
			for (int i = -1; i < 2; i += 2) {
				new Eggception(world, new Vec3(90 * i, 10, 0));
			}
			for (int i = -1; i < 2; i += 2) {
				new Eggception(world, new Vec3(0, 10, 90 * i));
			}

			nextState = GameState.BOSS;
		}
		case INTERMISSION_4_BOSS -> {

			nextState = GameState.BOSS;
		}
		case BOSS -> {
			new Guardian(world, new Vec3(0, 100, 0));
			nextState = GameState.ENDING;
		}
		case ENDING -> {
			nextState = null;
		}
		case GAME_OVER -> {
			nextState = null;
		}
		}
	}
}
