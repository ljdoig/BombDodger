package com.run;

import com.badlogic.gdx.Screen;

public class GameScreen implements Screen {
    private final RunGame game;
    private final Scott scott;

    public GameScreen(RunGame game) {
        this.game = game;
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
        scott.update(game.batch);
        boolean collision = Bomb.updateBombs(game.batch, scott.rectangle);
        if (collision) {
            scott.takeDamage();
            if (scott.isDead()) {
                System.out.println("DEAD");
            }
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
