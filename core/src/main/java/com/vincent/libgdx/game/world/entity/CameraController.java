package com.vincent.libgdx.game.world.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public class CameraController extends RayEmitter {
	private PerspectiveCamera camera;

	private boolean attached = true;
	private double prevRotX, prevRotY;

	public CameraController(Vec3 position) {
		super(position);
		camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.near = 0.5f;
		camera.far = 300;
		camera.update();
	}

	@Override
	public void setRotation(double x, double y) {
		super.setRotation(x, y);
		setCameraAxes();
	}

	@Override
	public void rotate(double x, double y) {
		super.rotate(x, y);
		setCameraAxes();
	}

	private void setCameraAxes() {
		camera.direction.set(getDirection().toGDXVector3());
		camera.up.set(getVertical().toGDXVector3());
		camera.update();
	}

	public void toggleAttached() {
		if (!attached) {
//			snap();
			attached = true;
		} else {
			prevRotX = getRotX();
			prevRotY = getRotY();
			attached = false;
		}
	}

//	@Override
//	public void snap() {
//		super.snap();
//		setRotation(prevRotX, prevRotY);
//	}

	public boolean isAttached() {
		return attached;
	}

	public PerspectiveCamera getCamera() {
		return camera;
	}

	private void updateCameraPosition() {
		Vec3 globalPosition = getGlobalPosition();
		camera.position.set((float) globalPosition.x(), (float) globalPosition.y(), (float) globalPosition.z());
		camera.update();
	}

	@Override
	public void update() {
		super.update();
		updateCameraPosition();
	}

	@Override
	public void setLocalPosition(Vec3 v) {
		super.setLocalPosition(v);
		updateCameraPosition();
	}
}
