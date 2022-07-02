package com.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class RunGame extends Game {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int GROUND_HEIGHT = 245;

	private Scott scott;
	private OrthographicCamera camera;
	private SpriteBatch batch;

	@Override
	public void create() {
		Scott.create();
		scott = new Scott();

		Bomb.create();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);

		batch = new SpriteBatch();
	}

	@Override
	public void render() {
		ScreenUtils.clear(1f, 1f, 1f, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		scott.update(batch);
		Bomb.updateBombs(batch, scott.rectangle);
		batch.end();
	}

	@Override
	public void dispose() {
		Bomb.dispose();
		Scott.dispose();
		batch.dispose();
	}

}
