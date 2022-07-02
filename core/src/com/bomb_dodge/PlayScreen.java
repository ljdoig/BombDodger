package com.bomb_dodge;

import com.badlogic.gdx.Screen;

public class PlayScreen implements Screen {
    private final DodgeGame game;
    private int wave;
    private final Scott scott;

    public PlayScreen(DodgeGame game) {
        this.game = game;
        this.wave = 1;
        scott = new Scott();
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
                String.format("Wave: %d", wave),
                0.9f * DodgeGame.WIDTH,
                0.9f * DodgeGame.HEIGHT
        );
        scott.update(game.batch);
        Bomb.updateBombs(game.batch, scott.rectangle);
        if (Bomb.collisionJustOccurred()) {
            scott.takeDamage();
            if (scott.isDead()) {
                game.setScreen(new GameOverScreen(game, wave));
            }
        }
        if (Bomb.waveFinished()) {
            wave++;
            scott.incrementHealth();
            Bomb.reset(wave);
        }
        game.batch.end();
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
