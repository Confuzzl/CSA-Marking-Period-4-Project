package com.vincent.libgdx.game.world.entity;

import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.collisionutils.shapes.BasePolyhedron.CoordinateCenter;
import com.vincent.libgdx.game.collisionutils.shapes.BoxFactory;
import com.vincent.libgdx.game.collisionutils.shapes.CollidablePolyhedron;
import com.vincent.libgdx.game.world.LegendOfHolmer;
import com.vincent.libgdx.game.world.LegendOfHolmer.GameState;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;
import com.vincent.libgdx.game.world.entity.mobs.HostileEntity;

public class Player extends GameEntity {
	public static final double DEFAULT_HEIGHT = 8, DEFAULT_WIDTH = 3;
	public static final CollidablePolyhedron DEFAULT_HITBOX = BoxFactory.collidableBox(0, 0, 0, DEFAULT_WIDTH,
			DEFAULT_HEIGHT, DEFAULT_WIDTH, Vec3.ZERO, CoordinateCenter.atFoot());
	public static final double DEFAULT_RAY_OFFSET = 7;

	private CameraController cameraController;
	private PlayerController controller;

	public static Weapon[] order = { Weapon.FISTS, Weapon.M4MATTER, Weapon.THE_MORE_GUN, Weapon.DBG9000,
			Weapon.TRI_CATCHER };
	private Weapon weapon = Weapon.M4MATTER;

	public Player(World world, Vec3 position) {
		super(world, position, DEFAULT_HITBOX, CollisionType.NORMAL, false, null, 0, 0, 0, 100);
		setRayEmitter(cameraController = new CameraController(new Vec3(0, DEFAULT_RAY_OFFSET, 0)));
		controller = new PlayerController(this);

		Weapons.owner = this;
		Weapons.ownerCamera = cameraController;
		HostileEntity.target = this;
	}

	public Weapon getWeapon() {
		return weapon;
	}

	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
	}

	@Override
	public void update() {
		super.update();
		controller.update();
		weapon.update();
	}

	@Override
	public void onDeath() {
		LegendOfHolmer.setState(GameState.GAME_OVER);
	}

	public CameraController getCameraController() {
		return cameraController;
	}

	public PlayerController getController() {
		return controller;
	}
}
