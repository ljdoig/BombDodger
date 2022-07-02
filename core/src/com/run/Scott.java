package com.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Scott {
    private static final float SCOTT_SPEED = RunGame.WIDTH / 4f;
    private static final float RUN_FRAME_TIME_S = 0.07f;
    private static final int WIDTH = 108;
    private static final int HEIGHT = 140;
    private static Texture standingImage;
    private static Animation<TextureRegion> rightRun;
    private static Animation<TextureRegion> leftRun;

    // negative when running left, positive when running right, else 0
    private float runTimer;
    public final Rectangle rectangle;

    public Scott() {
        rectangle = new Rectangle(
                (RunGame.WIDTH - standingImage.getWidth()) / 2f,
                RunGame.GROUND_HEIGHT,
                standingImage.getWidth(),
                standingImage.getHeight()
        );
    }

    public static void create() {
        standingImage = new Texture("scott.png");
        TextureRegion[][] runSheet = TextureRegion.split(
                new Texture("scott_run.png"), WIDTH, HEIGHT
        );
        rightRun = new Animation<>(RUN_FRAME_TIME_S, runSheet[0]);
        leftRun = new Animation<>(RUN_FRAME_TIME_S, runSheet[1]);
    }

    public static void dispose() {
        standingImage.dispose();
    }

    public void update(SpriteBatch batch) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // Update location
            rectangle.x += SCOTT_SPEED * Gdx.graphics.getDeltaTime();
            if (rectangle.x > RunGame.WIDTH - standingImage.getWidth()) {
                rectangle.x = RunGame.WIDTH - standingImage.getWidth();
            }
            // Update run frame and render
            if (runTimer < 0) {
                runTimer = 0;
            }
            batch.draw(
                    rightRun.getKeyFrame(runTimer, true),
                    rectangle.x,
                    rectangle.y
            );
            runTimer += Gdx.graphics.getDeltaTime();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            // Update location
            rectangle.x -= SCOTT_SPEED * Gdx.graphics.getDeltaTime();
            if (rectangle.x < 0) {
                rectangle.x = 0;
            }
            // Update run frame and render
            if (runTimer > 0) {
                runTimer = 0;
            }
            batch.draw(
                    leftRun.getKeyFrame(-runTimer, true),
                    rectangle.x,
                    rectangle.y
            );
            runTimer -= Gdx.graphics.getDeltaTime();
        } else {
            batch.draw(standingImage, rectangle.x, rectangle.y);
            runTimer = 0;
        }
    }

}
