package com.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class RunGame extends Game {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	private static final float SCOTT_SPEED = WIDTH / 4f;
	private static final float BOMB_SPEED = HEIGHT / 2f;
	private static final float RUN_FRAME_TIME_S = 0.07f;
	private static final long BOMB_SPAWN_INTERVAL_NS = 100000000;

	// loaded assets
	private Texture bombImage;
	private Texture staticScottImage;
	// negative when running left, positive when running right, else 0
	private float runTimer;
	private Animation<TextureRegion> leftRun;
	private Animation<TextureRegion> rightRun;
	private Sound bombSound;

	private OrthographicCamera camera;
	private SpriteBatch batch;

	private Rectangle scottLocation;
	private Array<Rectangle> bombs;
	private long lastBombTime;

	@Override
	public void create() {
		// visual
		bombImage = new Texture("bomb.png");
		staticScottImage = new Texture("scott.png");
		TextureRegion[][] runSheet = TextureRegion.split(
				new Texture("scott_run.png"), 108, 140
		);
		runTimer = 0;
		rightRun = new Animation<>(RUN_FRAME_TIME_S, runSheet[0]);
		leftRun = new Animation<>(RUN_FRAME_TIME_S, runSheet[1]);
		// audio
		bombSound = Gdx.audio.newSound(Gdx.files.internal("bomb_sound.wav"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		batch = new SpriteBatch();

		scottLocation = new Rectangle();
		scottLocation.x = (WIDTH - staticScottImage.getWidth()) / 2f;
		scottLocation.y = 20;
		scottLocation.width = staticScottImage.getWidth();
		scottLocation.height = staticScottImage.getHeight();

		bombs = new Array<>();
		spawnBomb();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0.1f, 0.1f, 1f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for (Rectangle bomb: bombs) {
			batch.draw(bombImage, bomb.x, bomb.y);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			scottLocation.x += SCOTT_SPEED * Gdx.graphics.getDeltaTime();
			if (scottLocation.x > WIDTH - staticScottImage.getWidth()) {
				scottLocation.x = WIDTH - staticScottImage.getWidth();
			}
			if (runTimer < 0) {
				runTimer = 0;
			}
			batch.draw(
					rightRun.getKeyFrame(runTimer, true),
					scottLocation.x,
					scottLocation.y
			);
			runTimer += Gdx.graphics.getDeltaTime();
		} else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			scottLocation.x -= SCOTT_SPEED * Gdx.graphics.getDeltaTime();
			if (scottLocation.x < 0) {
				scottLocation.x = 0;
			}
			if (runTimer > 0) {
				runTimer = 0;
			}
			batch.draw(
					leftRun.getKeyFrame(-runTimer, true),
					scottLocation.x,
					scottLocation.y
			);
			runTimer -= Gdx.graphics.getDeltaTime();
		} else {
			batch.draw(staticScottImage, scottLocation.x, scottLocation.y);
			runTimer = 0;
		}
		batch.end();
		updateBombs();
	}

	@Override
	public void dispose() {
		bombImage.dispose();
		staticScottImage.dispose();
		bombSound.dispose();
		batch.dispose();
	}

	private void updateBombs() {
		if (TimeUtils.nanoTime() - lastBombTime > BOMB_SPAWN_INTERVAL_NS) {
			spawnBomb();
		}
		for (Iterator<Rectangle> iter = bombs.iterator(); iter.hasNext();) {
			Rectangle raindrop = iter.next();
			raindrop.y -= BOMB_SPEED * Gdx.graphics.getDeltaTime();
			if (raindrop.y + bombImage.getHeight() < 0) {
				iter.remove();
			} else if (raindrop.overlaps(scottLocation)) {
				bombSound.play(0.1f);
				iter.remove();
			}
		}
	}

	private void spawnBomb() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, WIDTH - bombImage.getWidth());
		raindrop.y = HEIGHT;
		raindrop.width = bombImage.getWidth();
		raindrop.height = bombImage.getHeight();
		bombs.add(raindrop);
		lastBombTime = TimeUtils.nanoTime();
	}

}
