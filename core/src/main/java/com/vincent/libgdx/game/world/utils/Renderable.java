package com.vincent.libgdx.game.world.utils;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Disposable;

public interface Renderable extends Disposable {
	public ModelBuilder getModelBuilder();

	public ModelInstance getModelInstance();

	public Model getModel();

	public default void update() {
	}

	@Override
	public default void dispose() {
		getModel().dispose();
	}
}
