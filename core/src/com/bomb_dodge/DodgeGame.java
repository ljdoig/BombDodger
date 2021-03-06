package com.bomb_dodge;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class DodgeGame extends Game {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int GROUND_HEIGHT = 245;
	public static final int FONT_SIZE = 34;

	public Texture background;
	public OrthographicCamera camera;
	private FitViewport viewport;
	public SpriteBatch batch;
	private GlyphLayout glyphLayout;
	private BitmapFont font;

	@Override
	public void create() {
		background = new Texture("background.jpg");

		Scott.create();
		Bomb.create();
		LifeBar.create();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		viewport = new FitViewport(DodgeGame.WIDTH, DodgeGame.HEIGHT, camera);
		viewport.apply();
		batch = new SpriteBatch();
		glyphLayout = new GlyphLayout();
		font = FontLoader.load("Lotuscoder.ttf", FONT_SIZE);

		setScreen(new MenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void dispose() {
		Scott.dispose();
		Bomb.dispose();
		LifeBar.dispose();
		batch.dispose();
		background.dispose();
		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	public void drawCentredText(String s, float x, float y) {
		glyphLayout.setText(font, s);
		font.draw(
				batch,
				glyphLayout,
				x - glyphLayout.width / 2,
				y + glyphLayout.height / 2
		);
	}

}
