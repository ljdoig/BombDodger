package com.run;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LifeBar {
    private static Texture HEART;
    private static Texture EMPTY_HEART;
    private static final float FIRST_HEART_X = 80;
    private static final float Y = RunGame.HEIGHT - FIRST_HEART_X;
    private static final float GAP = 50;
    private final int maxHealth;

    public LifeBar(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public static void create() {
        HEART = new Texture("fullLife.png");
        EMPTY_HEART = new Texture("noLife.png");
    }

    public static void dispose() {
        HEART.dispose();
        EMPTY_HEART.dispose();
    }

    public void render(SpriteBatch batch, int health) {
        int i = 0;
        for (; i < health; i++) {
            batch.draw(
                    HEART,
                    FIRST_HEART_X + i * GAP - HEART.getWidth() / 2f,
                    Y - HEART.getHeight() / 2f
            );
            System.out.println(HEART.getHeight());
            System.out.println(HEART.getWidth());
        }
        for (; i < maxHealth; i++) {
            batch.draw(
                    EMPTY_HEART,
                    FIRST_HEART_X + i * GAP - HEART.getWidth() / 2f,
                    Y - HEART.getHeight() / 2f
            );
        }
    }

}