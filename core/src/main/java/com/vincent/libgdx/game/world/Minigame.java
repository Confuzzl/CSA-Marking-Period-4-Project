package com.vincent.libgdx.game.world;

import com.badlogic.gdx.Gdx;

public abstract class Minigame {
	private World world;
	private int score = 0;
	private double elapsed = 0;

	public Minigame(World world) {
		this.world = world;
	}

	public World getWorld() {
		return world;
	}

	public int getScore() {
		return score;
	}

	protected void addScore(int n) {
		score += n;
	}

	public double getElapsed() {
		return elapsed;
	}

	public abstract void start();

	public void update() {
		if (endCondition())
			end();
		else
			elapsed += Gdx.graphics.getDeltaTime();
	}

	protected abstract boolean endCondition();

	protected abstract void end();

//	public static Minigame createGridShot(World world) {
//		return new Minigame(world) {
//			private int width = 4;
//			private int height = 4;
//			private int size = 3;
//			private int buffer = 1;
//			private int count = 3;
//
//			private IntArray remaining = new IntArray();
//			private int[] selected = new int[count];
//			private int IDs;
//
//			private WorldObject[] targets = new WorldObject[count];
//
//			@Override
//			public void start() {
//				setRemaining();
//				generateSelected();
//				for (int i = 0; i < count; i++) {
//					int value = selected[i];
//					int row = value / width;
//					int column = value % width;
//					Polyhedron cube = new Hexahedron(new double[3], size);
//					WorldObject target = new WorldObject(getWorld(),
//							new Vec3(25, (size + buffer) * row,
//									(size + buffer) * (column - (width - 1) / 2d) - buffer / 2d),
//							cube, RenderMode.UNSHADED, cube, "target", "target", "target", "target", "target",
//							"target") {
//						private int selectedIndex = IDs++;
//
//						@Override
//						public void onRay(Entity caster, boolean shoot) {
//							if (caster != getWorld().getPlayer())
//								return;
//							getHitboxPart().setRender(true);
//						}
//
//						@Override
//						public void onRayClosest(Entity caster, boolean shoot) {
//							if (caster != getWorld().getPlayer())
//								return;
//							if (shoot) {
//								addScore(1);
//								int newRemaining = remaining.get(randomIndexRemaining());
//								int val = selected[selectedIndex];
//								remaining.add(val);
//								remaining.removeValue(newRemaining);
//								selected[selectedIndex] = newRemaining;
//								int row = newRemaining / width;
//								int column = newRemaining % width;
//								setPosition(new Vec3(25, (size + buffer) * row,
//										(size + buffer) * (column - (width - 1) / 2d) - buffer / 2d));
//							}
//						}
//					};
//					targets[i] = target;
//				}
//			}
//
//			private void setRemaining() {
//				remaining.clear();
//				for (int i = 0; i < height; i++)
//					for (int j = 0; j < width; j++)
//						remaining.add(i * height + j);
//			}
//
//			private void generateSelected() {
//				for (int i = 0; i < count; i++) {
//					int select = randomIndexRemaining();
//					selected[i] = remaining.get(select);
//					remaining.removeIndex(select);
//				}
//			}
//
//			private int randomIndexRemaining() {
//				return (int) (Math.random() * remaining.size);
//			}
//
//			@Override
//			protected boolean endCondition() {
//				return this.getElapsed() > 60;
//			}
//
//			@Override
//			protected void end() {
//				for (WorldObject wo : targets)
//					wo.disable();
//			}
//		};
//	}
}
