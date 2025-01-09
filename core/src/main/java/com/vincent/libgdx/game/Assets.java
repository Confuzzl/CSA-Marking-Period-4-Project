package com.vincent.libgdx.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;

public class Assets {
	private static final AssetManager manager = new AssetManager();

	public static void loadTexture(String fileName) {
//		System.out.println(fileName);
		manager.load(fileName, Texture.class);
	}

	public static void loadAudio(String fileName) {
		manager.load(fileName, Sound.class);
	}

	public static Texture getTexture(String fileName) {
		Texture texture = manager.get(fileName + ".png", Texture.class);
		texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		return texture;
	}

	public static Sound getSound(String fileName) {
		return manager.get(fileName + ".mp3");
	}

	public static Material[] namesToMaterial(String... names) {
		Material[] output = new Material[names.length];
		for (int i = 0; i < names.length; i++) {
			String name = names[i];
			if (name == null || name.equals("")) {
				output[i] = new Material(TextureAttribute.createDiffuse(getTexture("default")));
			} else {
				Material m = new Material(TextureAttribute.createDiffuse(getTexture(name)));
				m.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
				output[i] = m;
			}
		}
		return output;
	}

	public static void finish() {
		manager.finishLoading();
	}

	public static void dispose() {
		manager.dispose();
	}
}
