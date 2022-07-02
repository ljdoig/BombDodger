package com.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class MenuScreen implements Screen {
    private final RunGame game;

    public MenuScreen(RunGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        
    }

    @Override
    public void render(float delta) {
        game.camera.update();
        game.batch.setProjectionMatrix(game.camera.combined);
        game.batch.begin();
        game.batch.draw(game.background, 0, 0);
        game.drawCentredText(
                "Press ENTER to begin",
                RunGame.WIDTH / 2f,
                RunGame.HEIGHT * 3 / 4f
        );
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new GameScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
