package com.bomb_dodge;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;

public class GameOverScreen implements Screen {
    private static final float finalExplosionDurationMultiplier = 2f;
    private final DodgeGame game;
    private final int finalWave;

    public GameOverScreen(DodgeGame game, int finalWave) {
        this.game = game;
        this.finalWave = finalWave;
        Bomb.multiplyExplosionDuration(finalExplosionDurationMultiplier);
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
        boolean stillExploding = Bomb.afterDeathUpdate(game.batch);
        if (!stillExploding) {
            game.drawCentredText(
                    String.format("Game Over! You made it to wave %d", finalWave),
                    DodgeGame.WIDTH / 2f,
                    DodgeGame.HEIGHT * 0.85f
            );
            game.drawCentredText(
                    "Press ENTER to play again",
                    DodgeGame.WIDTH / 2f,
                    DodgeGame.HEIGHT * 0.75f
            );
        }
        game.batch.end();
        if (!stillExploding) {
            if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
                Bomb.reset(1);
                Bomb.multiplyExplosionDuration(1/finalExplosionDurationMultiplier);
                game.setScreen(new PlayScreen(game));
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        game.resize(width, height);
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
