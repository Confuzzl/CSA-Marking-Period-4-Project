package com.vincent.libgdx.game.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector3;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.LegendOfHolmer;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;
import com.vincent.libgdx.game.world.entity.npcs.NPC;

public class PlayerController {
	private Player player;

	private static final double degreesPerPixel = 10;
	private double speed = 50;

	private boolean snapCompensation = false;

	public PlayerController(Player player) {
		this.player = player;
	}

	public void update() {
		detectMouseInput();
		detectKeyboardInput();
	}

	private void detectMouseInput() {
		double deltaX = Gdx.input.getDeltaX() * degreesPerPixel * Gdx.graphics.getDeltaTime();
		double deltaY = Gdx.input.getDeltaY() * degreesPerPixel * Gdx.graphics.getDeltaTime();
		if (!snapCompensation && deltaX != 0 && deltaY != 0) {
			snapCompensation = true;
		} else {
			player.getCameraController().rotate(-deltaX, -deltaY);
		}
		if (Gdx.input.isTouched()) {
			player.getWeapon().fire();
		}
	}

	private void detectKeyboardInput() {
		if (Gdx.input.isKeyJustPressed(Keys.R)) {
			player.getWeapon().initiateReload();
		}
		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			NPC.advanceSpeech();
		}

		if (LegendOfHolmer.weaponFlag > 0) {
			if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
				player.setWeapon(Weapon.M4MATTER);
			}
		}
		if (LegendOfHolmer.weaponFlag > 1) {
			if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
				player.setWeapon(Weapon.THE_MORE_GUN);
			}
		}
		if (LegendOfHolmer.weaponFlag > 2) {
			if (Gdx.input.isKeyPressed(Keys.NUM_3)) {
				player.setWeapon(Weapon.DBG9000);
			}
		}
		if (LegendOfHolmer.weaponFlag > 3) {
			if (Gdx.input.isKeyPressed(Keys.NUM_4)) {
				player.setWeapon(Weapon.TRI_CATCHER);
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}

		if (NPC.current != null) {
			player.getMovement().reset();
			return;
		}

		Vector3 cameraDirection = player.getCameraController().getCamera().direction;
		double dt = Gdx.graphics.getDeltaTime();

		{
			Vec3 net = new Vec3();
			if (Gdx.input.isKeyPressed(Keys.W)) {
				net.addEquals(cameraDirection.x, 0, cameraDirection.z);
			}
			if (Gdx.input.isKeyPressed(Keys.A)) {
				net.addEquals(cameraDirection.z, 0, -cameraDirection.x);
			}
			if (Gdx.input.isKeyPressed(Keys.S)) {
				net.addEquals(-cameraDirection.x, 0, -cameraDirection.z);
			}
			if (Gdx.input.isKeyPressed(Keys.D)) {
				net.addEquals(-cameraDirection.z, 0, cameraDirection.x);
			}
			if (dt != 0) {
				net.normalizeTo(speed * dt);
				player.getMovement().set(net);
			}
		}

		if (player.ignoreGravity()) {
			if (Gdx.input.isKeyPressed(Keys.SPACE)) {
				player.getUp().set(0, speed * dt, 0);
			} else {
				player.getUp().reset();
			}

			if (Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
				player.getDown().set(0, -speed * dt, 0);
			} else {
				player.getDown().reset();
			}
		} else {
			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				player.initiateJump();
			}
		}

	}
}
