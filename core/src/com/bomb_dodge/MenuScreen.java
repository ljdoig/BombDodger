package com.bomb_dodge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class MenuScreen implements Screen {
    private final DodgeGame game;

    public MenuScreen(DodgeGame game) {
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
                DodgeGame.WIDTH / 2f,
                DodgeGame.HEIGHT * 0.8f
        );
        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            game.setScreen(new PlayScreen(game));
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
