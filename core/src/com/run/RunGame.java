package com.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class RunGame extends Game {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	private static final float SCOTT_SPEED = WIDTH / 3f;
	private static final float BOMB_SPEED = HEIGHT / 2f;
	private static final long BOMB_SPAWN_INTERVAL_NS = 100000000;

	// loaded assets
	private Texture bombImage;
	private Texture scottImage;
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
		scottImage = new Texture("scott.png");
		// audio
		bombSound = Gdx.audio.newSound(Gdx.files.internal("bomb_sound.wav"));

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		batch = new SpriteBatch();

		scottLocation = new Rectangle();
		scottLocation.x = (WIDTH - scottImage.getWidth()) / 2f;
		scottLocation.y = 20;
		scottLocation.width = scottImage.getWidth();
		scottLocation.height = scottImage.getHeight();

		bombs = new Array<>();
		spawnRaindrop();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0.4f, 0.4f, 0.4f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(scottImage, scottLocation.x, scottLocation.y);
		for (Rectangle bomb: bombs) {
			batch.draw(bombImage, bomb.x, bomb.y);
		}
		batch.end();

		if (Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPos);
			scottLocation.x = touchPos.x - scottImage.getWidth() / 2f;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			scottLocation.x -= SCOTT_SPEED * Gdx.graphics.getDeltaTime();
		}

		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			scottLocation.x += SCOTT_SPEED * Gdx.graphics.getDeltaTime();
		}
		if (scottLocation.x < 0) {
			scottLocation.x = 0;
		}
		if (scottLocation.x > WIDTH - scottImage.getWidth()) {
			scottLocation.x = WIDTH - scottImage.getWidth();
		}

		if (TimeUtils.nanoTime() - lastBombTime > BOMB_SPAWN_INTERVAL_NS) {
			spawnRaindrop();
		}
		for (Iterator<Rectangle> iter = bombs.iterator(); iter.hasNext();) {
			Rectangle raindrop = iter.next();
			raindrop.y -= BOMB_SPEED * Gdx.graphics.getDeltaTime();
			if (raindrop.y + bombImage.getHeight() < 0) {
				iter.remove();
			} else if (raindrop.overlaps(scottLocation)) {
				bombSound.play();
				iter.remove();
			}
		}
	}

	@Override
	public void dispose() {
		bombImage.dispose();
		scottImage.dispose();
		bombSound.dispose();
		batch.dispose();
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, WIDTH - bombImage.getWidth());
		raindrop.y = HEIGHT;
		raindrop.width = bombImage.getWidth();
		raindrop.height = bombImage.getHeight();
		bombs.add(raindrop);
		lastBombTime = TimeUtils.nanoTime();
	}

}
