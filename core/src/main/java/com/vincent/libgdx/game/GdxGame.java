package com.vincent.libgdx.game;

import java.io.File;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.vincent.libgdx.game.collisionutils.primitives.Vec3;
import com.vincent.libgdx.game.world.LegendOfHolmer;
import com.vincent.libgdx.game.world.LegendOfHolmer.GameState;
import com.vincent.libgdx.game.world.LegendOfHolmer.GameState.SkyColor;
import com.vincent.libgdx.game.world.World;
import com.vincent.libgdx.game.world.entity.CameraController;
import com.vincent.libgdx.game.world.entity.Entity;
import com.vincent.libgdx.game.world.entity.Player;
import com.vincent.libgdx.game.world.entity.PlayerController;
import com.vincent.libgdx.game.world.entity.Weapons.Weapon;
import com.vincent.libgdx.game.world.entity.npcs.NPC;
import com.vincent.libgdx.game.world.utils.Renderable;
import com.vincent.libgdx.game.world.utils.WorldObject;

public class GdxGame extends ApplicationAdapter {
	private World world;

	private Environment environment;
	private ModelBatch modelBatch;
	private DecalBatch decalBatch;

	private OrthographicCamera guiCamera;
	private SpriteBatch spriteBatch;
	private static BitmapFont font;
	private static GlyphLayout layout = new GlyphLayout();

	private Player player;
	private PlayerController controller;
	private CameraController cameraController;

	private static Texture crosshair;

	private static Texture titleTexture;
	private static double titleScale = 2.5;
	private Stage stage;
	private boolean titleScreen = true;
	private TextButton startButton;
	private static Texture buttonTexture;
	private int startButtonWidth = 480, startButtonHeight = 160;

	private Texture healthBarEmpty, healthBarFull;

	@Override
	public void create() {
		loadWorldTextures();
		titleTexture = Assets.getTexture("title");
		buttonTexture = Assets.getTexture("button");
		healthBarEmpty = Assets.getTexture("health_bar_empty");
		healthBarFull = Assets.getTexture("health_bar_full");

		Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);

		Gdx.gl20.glLineWidth(5);

		guiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.getData().markupEnabled = true;

		spriteBatch = new SpriteBatch();
		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.5f, 0.5f, 0.5f, 2f));
		environment.add(new DirectionalLight().set(1, 1, 1, -1f, -0.6f, -0.2f));

		stage = new Stage();
//		TextButtonStyle style = new TextButtonStyle();
//		style.font = font;
//		style.fontColor = Color.WHITE;
//		style.downFontColor = Color.GRAY;
//		startButton = new TextButton("START", style);
//		startButton.getLabel().setFontScale(4);
//		startButton.setSize(startButtonWidth, startButtonHeight);
//		startButton.setPosition(550, Gdx.graphics.getHeight() / 2 - 150, Align.center);
////		startButton.setDebug(true);
//
//		startButton.addListener(new ChangeListener() {
//			@Override
//			public void changed(ChangeEvent event, Actor actor) {
//				startGame();
//				startButton.setDisabled(true);
//			}
//		});
//
//		stage.addActor(startButton);
//		Gdx.input.setInputProcessor(stage);
		startGame();
	}

	private void startGame() {
		titleScreen = false;

		Gdx.input.setCursorCatched(true);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		crosshair = Assets.getTexture("crosshair");

		world = new World();
		player = world.getPlayer();
		controller = player.getController();
		cameraController = player.getCameraController();

		decalBatch = new DecalBatch(new CameraGroupStrategy(player.getCameraController().getCamera()));
	}

	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (titleScreen) {
			guiCamera.update();
			spriteBatch.setProjectionMatrix(guiCamera.combined);
			spriteBatch.enableBlending();
			spriteBatch.begin();
			spriteBatch.draw(Assets.getTexture("title_screen"), -Gdx.graphics.getWidth() / 2,
					-Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			spriteBatch.draw(titleTexture, -Gdx.graphics.getWidth() / 2 + 50,
					Gdx.graphics.getHeight() / 2 - (int) (titleTexture.getHeight() * titleScale) - 75,
					(int) (titleTexture.getWidth() * titleScale), (int) (titleTexture.getHeight() * titleScale));
			spriteBatch.draw(buttonTexture, -Gdx.graphics.getWidth() / 2 + 310, -Gdx.graphics.getHeight() / 2 + 310,
					startButtonWidth, startButtonHeight);
			spriteBatch.end();

			stage.draw();
		} else {
			if (LegendOfHolmer.currentState.isScreen()) {
				Gdx.gl.glClearColor(0, 0, 0, 0);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
				spriteBatch.setProjectionMatrix(guiCamera.combined);
				spriteBatch.enableBlending();
				spriteBatch.begin();

				font.getData().setScale(6);
				String str = LegendOfHolmer.currentState == GameState.ENDING ? "YOU WIN YIPPEE!!!" : "YOU DIED";
				layout.setText(font, str, 0, str.length(), font.getColor(), 0, Align.center, false, null);
				font.draw(spriteBatch, layout, 0, 0);

				spriteBatch.end();
			} else {
				SkyColor skyColor = LegendOfHolmer.currentState.getSkyColor();
				Gdx.gl.glClearColor(skyColor.r() / 255f, skyColor.g() / 255f, skyColor.b() / 255f, 0);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

				world.advance();

				Vec3 pos = player.getGlobalPosition();
				modelBatch.begin(cameraController.getCamera());

				for (WorldObject wo : world.getWorldObjects())
					modelBatch.render(wo.getModelInstance(), environment);
				for (Entity e : world.getEntities()) {
					modelBatch.render(e.getModelInstance());
					Decal decal = e.getDecal();
					if (decal == null)
						continue;
					decal.lookAt(new Vector3((float) pos.x(), decal.getY(), (float) pos.z()), Vector3.Y);
					decalBatch.add(decal);
				}
				for (Renderable r : world.getRenderables())
					modelBatch.render(r.getModelInstance());

				modelBatch.end();
				decalBatch.flush();

				updateGUI();
			}
		}
	}

	@Override
	public void dispose() {
		font.dispose();
		modelBatch.dispose();
		spriteBatch.dispose();
		stage.dispose();
		if (!titleScreen) {
			decalBatch.dispose();
			disposeRenders();
		}
		Assets.dispose();

		System.out.println("DISPOSED");
	}

	private void loadWorldTextures() {
		for (File file : new File("../assets").listFiles())
			if (file.isFile())
				switch (file.getName().substring(file.getName().length() - 3)) {
				case "png" -> Assets.loadTexture(file.getName());
				case "mp3" -> Assets.loadAudio(file.getName());
				}
		Assets.finish();
	}

	private void disposeRenders() {
		for (WorldObject o : world.getWorldObjects())
			o.dispose();
		for (Entity e : world.getEntities())
			e.dispose();
		for (Renderable r : world.getRenderables())
			r.dispose();
	}

	private void updateGUI() {
		guiCamera.update();
		spriteBatch.setProjectionMatrix(guiCamera.combined);
		spriteBatch.enableBlending();
		spriteBatch.begin();

		displayInfo(1, 10, 20, false, "FPS: " + Gdx.graphics.getFramesPerSecond(),
				String.format("Player Position: %s Camera Position: %s", player.getGlobalPosition(),
						cameraController.getGlobalPosition()),
				String.format("Camera Ray: %s Camera Degrees: %.2f, %.2f",
						vecString(cameraController.getCamera().direction), cameraController.getRotX(),
						cameraController.getRotY()),
				String.format("Movement Velocity: %s", player.getMovement()),
				String.format("Down Velocity: %s Up Velocity: %s", player.getDown(), player.getUp()),
				String.format("On Ground: %b Under Ceiling: %b", player.onFloor(), player.underCeiling()),
				String.format("Down: %d Up: %d", player.getFloorColliders().size, player.getCeilingColliders().size),
				String.format("In Jump: %b", player.inJump()),
				String.format("Interceptors: %d", cameraController.getInterceptors().size),
				String.format("Closest WorldObject: %d",
						cameraController.getClosestWorldObject().getInterceptor() == null ? null
								: cameraController.getClosestWorldObject().getInterceptor().ID()),
				String.format("Closest Entity: %d",
						cameraController.getClosestEntity().getInterceptor() == null ? null
								: cameraController.getClosestEntity().getInterceptor().ID()),
				String.format("Closest Interactable: %s",
						cameraController.getClosestInterceptor().getInterceptor() == null ? null
								: cameraController.getClosestInterceptor().getInterceptor()),
				String.format("CurrentState: %s Weapon Flag: %d", LegendOfHolmer.currentState,
						LegendOfHolmer.weaponFlag),
				String.format("Current NPC: %d Hover NPC : %d | %d", NPC.current == null ? null : NPC.current.ID(),
						NPC.hover == null ? null : NPC.hover.ID(), NPC.textFlag),
				String.format("Enemies: %d / %d", LegendOfHolmer.enemiesLeft, LegendOfHolmer.enemyCount));

		drawWeapon();
		spriteBatch.draw(crosshair, -crosshair.getWidth() / 2, -crosshair.getHeight() / 2);
		if (LegendOfHolmer.boss != null)
			drawHealthBar(0, 480, 1000, 50, LegendOfHolmer.boss.getPercentHealth());
		drawHealthBar(-660, -480, 500, 50, player.getPercentHealth());
		handleNPCGUI();
		spriteBatch.end();
	}

	private static String vecString(Vector3 v) {
		return String.format("(%.2f, %.2f, %.2f)", v.x, v.y, v.z);
	}

	private void displayInfo(float scale, int xOffset, int buffer, boolean centered, String... strs) {
		int yOffset = 10;
		for (String str : strs) {
			font.getData().setScale(scale);
			layout.setText(font, str, 0, str.length(), font.getColor(), 0, centered ? Align.center : Align.left, false,
					null);
			font.draw(spriteBatch, layout, -Gdx.graphics.getWidth() / 2 + xOffset,
					Gdx.graphics.getHeight() / 2 - yOffset);
			yOffset += buffer;
		}
	}

	private void drawWeapon() {
		Weapon weapon = player.getWeapon();
		Texture t = Assets.getTexture("weapon_" + weapon.name().toLowerCase());
		int width = t.getWidth(), height = t.getHeight();
		int scale = 8;
		spriteBatch.draw(t, -width / 2 * scale, -Gdx.graphics.getHeight() / 2 - (weapon.isReloading() ? 200 : 0),
				width * scale, height * scale);

		if (weapon != Weapon.FISTS) {
			font.getData().setScale(4);
			String str = String.format("%d / %d", weapon.getCurrentAmmo(), weapon.getCapacity());
			layout.setText(font, str, 0, str.length(), font.getColor(), 0, Align.right, false, null);
			font.draw(spriteBatch, layout, Gdx.graphics.getWidth() / 2 - 25, -Gdx.graphics.getHeight() / 2 + 75);
		}
	}

	private final static int yOffset = 384;
	private final static int xBuffer = 64, yBuffer = 32;

	private void handleNPCGUI() {
		if (NPC.current == null && NPC.hover != null) {
			font.getData().setScale(2);
			String msg = "Press 'E' to interact";
			layout.setText(font, msg, 0, msg.length(), font.getColor(), 0, Align.center, false, null);
			font.draw(spriteBatch, layout, 0, -50);
		}
		if (NPC.getText() != null)
			displayNPCMessage(1024, 256, 3, NPC.getText());
	}

	private void drawHealthBar(int x, int y, double scaleX, double scaleY, double ratio) {
		spriteBatch.draw(healthBarEmpty, (int) (x - scaleX / 2), (int) (y - scaleY / 2), (float) scaleX,
				(float) scaleY);
		int scaleX2 = (int) (scaleX * ratio);
		double half = ratio / 2;
		int offset = (int) ((half - (ratio - 0.5)) * scaleX);
		spriteBatch.draw(healthBarFull, x - scaleX2 / 2 - offset, (int) (y - scaleY / 2), scaleX2, (float) scaleY);

	}

//	private void drawBossHealthBar() {
//		int scaleX = 1000, scaleY = 25;
//		spriteBatch.draw(healthBarEmpty, -scaleX / 2, 480, scaleX, scaleY);
//		float ratio = (float) LegendOfHolmer.boss.getPercentHealth();
//		int scaleX2 = (int) (scaleX * ratio);
//		float half = ratio / 2;
//		int offset = (int) ((half - (ratio - 0.5)) * scaleX);
//		spriteBatch.draw(healthBarFull, -scaleX2 / 2 - offset, 480, scaleX2, scaleY);
//	}

	private void displayNPCMessage(int boxWidth, int boxHeight, int fontScale, String msg) {
		spriteBatch.draw(Assets.getTexture("textbox"), -boxWidth / 2, -boxHeight / 2 - yOffset, boxWidth, boxHeight);
		font.getData().setScale(fontScale);
		layout.setText(font, msg, 0, msg.length(), font.getColor(), boxWidth - 2 * xBuffer, Align.left, true, null);
		font.draw(spriteBatch, layout, -boxWidth / 2 + xBuffer, boxHeight / 2 - yBuffer - yOffset);
	}
}
