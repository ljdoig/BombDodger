package com.run;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class RunGame extends Game {
	public static final int WIDTH = 1920;
	public static final int HEIGHT = 1080;
	public static final int GROUND_HEIGHT = 245;

	public Texture background;
	public OrthographicCamera camera;
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
		batch = new SpriteBatch();
		glyphLayout = new GlyphLayout();
		font = FontLoader.load("Lotuscoder.ttf", 34);

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

	public void drawCentredText(String s, float x, float y) {
		glyphLayout.setText(font, s);
		float drawX = x - glyphLayout.width / 2;
		float drawY = y + glyphLayout.height / 2;
		font.draw(batch, glyphLayout, drawX, drawY);
	}

}
