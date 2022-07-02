package com.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Scott {
    private static final int WIDTH = 108;
    private static final int HEIGHT = 140;
    private static final float SCOTT_SPEED = RunGame.WIDTH / 4f;
    private static final int MAX_HEALTH = 10;
    private static final float RUN_FRAME_TIME_S = 0.08f;
    private static final float HURT_TIME_S = 1;
    private static final int HURT_FLICKERS = 4;
    private static TextureRegion standingImage;
    private static Animation<TextureRegion> rightRun;
    private static Animation<TextureRegion> leftRun;

    // Negative when running left, positive when running right, else 0
    private float runTimer;
    public final Rectangle rectangle;
    private int health;
    private final LifeBar lifeBar;
    private boolean hurt = false;
    // Time remaining during hurt-period
    private float hurtTimer;

    public Scott() {
        runTimer = 0;
        rectangle = new Rectangle(
                (RunGame.WIDTH - standingImage.getRegionWidth()) / 2f,
                RunGame.GROUND_HEIGHT,
                standingImage.getRegionWidth(),
                standingImage.getRegionHeight()
        );
        health = MAX_HEALTH;
        lifeBar = new LifeBar(health);
    }

    public static void create() {
        standingImage = new TextureRegion(new Texture("scott.png"));
        TextureRegion[][] runSheet = TextureRegion.split(
                new Texture("scott_run.png"), WIDTH, HEIGHT
        );
        rightRun = new Animation<>(RUN_FRAME_TIME_S, runSheet[0]);
        leftRun = new Animation<>(RUN_FRAME_TIME_S, runSheet[1]);
    }

    public static void dispose() {}

    public void update(SpriteBatch batch) {
        TextureRegion renderedImage;
        lifeBar.render(batch, health);
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            // Update location
            rectangle.x += SCOTT_SPEED * Gdx.graphics.getDeltaTime();
            if (rectangle.x > RunGame.WIDTH - standingImage.getRegionWidth()) {
                rectangle.x = RunGame.WIDTH - standingImage.getRegionWidth();
            }
            // Update run frame and render
            if (runTimer < 0) {
                runTimer = 0;
            }
            renderedImage = rightRun.getKeyFrame(runTimer, true);
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
            renderedImage = leftRun.getKeyFrame(-runTimer, true);
            runTimer -= Gdx.graphics.getDeltaTime();
        } else {
            renderedImage = standingImage;
            runTimer = 0;
        }
        if (hurt) {
            hurtTimer -= Gdx.graphics.getDeltaTime();
            if (hurtTimer < 0) {
                hurt = false;
            }
            float hurtProportion = hurtTimer / HURT_TIME_S;
            if ((hurtProportion * HURT_FLICKERS) % 1 < 0.5f) {
                batch.draw(renderedImage, rectangle.x, rectangle.y);
            }
        } else {
            batch.draw(renderedImage, rectangle.x, rectangle.y);
        }
    }

    public void takeDamage() {
        if (!hurt) {
            health--;
            hurt = true;
            hurtTimer = HURT_TIME_S;
        }
    }

    public boolean isDead() {
        return health == 0;
    }
}
