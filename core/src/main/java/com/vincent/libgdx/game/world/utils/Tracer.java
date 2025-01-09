package com.vincent.libgdx.game.world.utils;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;

public class Tracer implements Nestable {
	public static Set<Renderable> set;
	public static Set<Renderable> removalSet;
	public static float upTime = 0.1f;

	private Vec3 localPosition = new Vec3();

	private ModelBuilder modelBuilder;
	private Model model;
	private ModelInstance modelInstance;

	private float time;

	public Tracer() {
		modelBuilder = new ModelBuilder();
		modelBuilder.begin();
//		addRPart(new TracerPart(origin, direction, l, color));
//		model = modelBuilder.end();
//		modelInstance = new ModelInstance(model);
//
//		set.add(this);
	}

	@Override
	public void update() {
		time += Gdx.graphics.getDeltaTime();
		if (time >= upTime)
			kill();
	}

	public void finishTracer() {
		model = modelBuilder.end();
		modelInstance = new ModelInstance(model);

		set.add(this);
	}

	@Override
	public ModelBuilder getModelBuilder() {
		return modelBuilder;
	}

	@Override
	public ModelInstance getModelInstance() {
		return modelInstance;
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public Vec3 getGlobalPosition() {
		return localPosition;
	}

	@Override
	public Vec3 getLocalPosition() {
		return localPosition;
	}

	@Override
	public void refreshInstance() {
		modelInstance = new ModelInstance(model, (float) localPosition.x(), (float) localPosition.y(),
				(float) localPosition.z());
	}

	@Override
	public void kill() {
		removalSet.add(this);
	}

}
