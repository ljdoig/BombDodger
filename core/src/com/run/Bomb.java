package com.run;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class Bomb {
    private static final float BOMB_SPEED_PER_S = RunGame.HEIGHT / 2f;
    private static final long BOMB_SPAWN_INTERVAL_NS = 100000000;
    private static final float EXPLOSION_FRAME_TIME_S = 0.02f;
    private static final float TOTAL_EXPLOSION_TIME = EXPLOSION_FRAME_TIME_S*48;
    private static final int EXPLOSION_SIZE = 240;
    private static final Array<Bomb> bombs = new Array<>();
    private static long lastBombTime;
    private static Texture IMAGE;
    private static Sound sound;
    private static Animation<TextureRegion> explosion;

    private final Rectangle rectangle;
    private boolean exploded;
    private float timeSinceExploded;

    public Bomb(Rectangle rectangle) {
        this.rectangle = rectangle;
        exploded = false;
        timeSinceExploded = 0;
    }

    public static void create() {
        sound = Gdx.audio.newSound(Gdx.files.internal("bomb_sound.wav"));
        IMAGE = new Texture("bomb.png");
        TextureRegion[][] explosionSheet = TextureRegion.split(
                new Texture("explosion.png"),
                EXPLOSION_SIZE,
                EXPLOSION_SIZE
        );
        explosion = new Animation<>(EXPLOSION_FRAME_TIME_S, flatten(explosionSheet));
        spawnBomb();
    }

    public static void dispose() {
        sound.dispose();
        IMAGE.dispose();
    }

    /*
    Update bomb array, return true if a bomb collides with supplied rectangle
    */
    public static boolean updateBombs(SpriteBatch batch, Rectangle rectangle) {
        if (TimeUtils.nanoTime() - lastBombTime > BOMB_SPAWN_INTERVAL_NS) {
            spawnBomb();
        }
        for (Iterator<Bomb> iter = bombs.iterator(); iter.hasNext();) {
            Bomb bomb = iter.next();
            if (bomb.exploded) {
                bomb.timeSinceExploded += Gdx.graphics.getDeltaTime();
                if (bomb.timeSinceExploded >= TOTAL_EXPLOSION_TIME) {
                    iter.remove();
                } else {
                    TextureRegion frame = explosion.getKeyFrame(bomb.timeSinceExploded);
                    batch.draw(
                        frame,
                        bomb.rectangle.x -
                                (EXPLOSION_SIZE - bomb.rectangle.height) / 2.0f,
                        bomb.rectangle.y -
                                (EXPLOSION_SIZE - bomb.rectangle.width) / 2.0f
                    );
                }
            } else {
                batch.draw(IMAGE, bomb.rectangle.x, bomb.rectangle.y);
                bomb.rectangle.y -= BOMB_SPEED_PER_S * Gdx.graphics.getDeltaTime();
                if (bomb.rectangle.y + IMAGE.getHeight() < 0) {
                    iter.remove();
                } else if (bomb.rectangle.overlaps(rectangle)) {
                    sound.play(0.1f);
                    bomb.exploded = true;
                    return true;
                }
            }
        }
        return false;
    }

    private static void spawnBomb() {
        Rectangle newBombRect = new Rectangle(
                MathUtils.random(0, RunGame.WIDTH - IMAGE.getWidth()),
                RunGame.HEIGHT,
                IMAGE.getWidth(),
                IMAGE.getHeight()
        );
        bombs.add(new Bomb(newBombRect));
        lastBombTime = TimeUtils.nanoTime();
    }

    private static TextureRegion[] flatten(TextureRegion[][] textureRegions2D) {
        int copied = 0;
        TextureRegion[] textureRegions1D =
                new TextureRegion[textureRegions2D.length*textureRegions2D[0].length];
        for (TextureRegion[] textureRegions : textureRegions2D) {
            for (TextureRegion textureRegion : textureRegions) {
                textureRegions1D[copied++] = textureRegion;
            }
        }
        return textureRegions1D;
    }
}
