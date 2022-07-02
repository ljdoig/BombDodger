package com.bomb_dodge;

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
    // Game engine objects
    private static Texture IMAGE;
    private static Sound SOUND;
    private static Animation<TextureRegion> EXPLOSION;

    private static final float EXPLOSION_FRAME_TIME_S = 0.02f;
    private static final int EXPLOSION_SIZE = 240;

    // Difficulty parameters
    private static final float DEFAULT_BOMB_SPEED_PPS = DodgeGame.HEIGHT / 2f;
    private static final long DEFAULT_BOMB_SPAWN_INTERVAL_NS = 200000000;
    private static final int NUM_IN_WAVE = 30;
    private static final int NUM_LEVEL_INCREASE = 10;

    private static float bombSpeed;
    private static long bombSpawnIntervalNs = 1000000000;
    private static int numBombsInWave;
    private static int bombCount;
    private static Array<Bomb> bombs;
    private static long lastBombTime;
    private static boolean collisionJustOccurred;

    private final Rectangle rectangle;
    private boolean exploded;
    private float timeSinceExploded;

    public Bomb(Rectangle rectangle) {
        this.rectangle = rectangle;
        exploded = false;
        timeSinceExploded = 0;
    }

    public static void create() {
        SOUND = Gdx.audio.newSound(Gdx.files.internal("bomb_sound.wav"));
        IMAGE = new Texture("bomb.png");
        TextureRegion[][] explosionSheet = TextureRegion.split(
                new Texture("explosion.png"),
                EXPLOSION_SIZE,
                EXPLOSION_SIZE
        );
        EXPLOSION = new Animation<>(EXPLOSION_FRAME_TIME_S, flatten(explosionSheet));
        bombs = new Array<>();
        spawnBomb();
        // set difficulty
        bombSpeed = DEFAULT_BOMB_SPEED_PPS;
        bombSpawnIntervalNs = DEFAULT_BOMB_SPAWN_INTERVAL_NS;
        bombCount = 0;
        numBombsInWave = NUM_IN_WAVE;
    }

    public static void reset(int wave) {
        bombs = new Array<>();
        spawnBomb();
        float difficulty = wave / 8f + 1;
        bombSpeed = DEFAULT_BOMB_SPEED_PPS * difficulty;
        bombSpawnIntervalNs = (long) (DEFAULT_BOMB_SPAWN_INTERVAL_NS / difficulty);
        bombCount = 0;
        numBombsInWave = NUM_IN_WAVE + (wave - 1) * NUM_LEVEL_INCREASE;

    }

    public static void dispose() {
        SOUND.dispose();
        IMAGE.dispose();
    }

    /*
    Update bomb array, return true if a bomb collides with supplied rectangle
    */
    public static void updateBombs(SpriteBatch batch, Rectangle rectangle) {
        if (TimeUtils.nanoTime() - lastBombTime > bombSpawnIntervalNs
                && bombCount < numBombsInWave) {
            spawnBomb();
        }
        for (Iterator<Bomb> iter = bombs.iterator(); iter.hasNext();) {
            Bomb bomb = iter.next();
            if (bomb.exploded) {
                bomb.timeSinceExploded += Gdx.graphics.getDeltaTime();
                if (bomb.timeSinceExploded >= EXPLOSION.getAnimationDuration()) {
                    iter.remove();
                } else {
                    TextureRegion frame = EXPLOSION.getKeyFrame(bomb.timeSinceExploded);
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
                bomb.rectangle.y -= bombSpeed * Gdx.graphics.getDeltaTime();
                if (bomb.rectangle.y + IMAGE.getHeight() < 0) {
                    iter.remove();
                } else if (bomb.rectangle.overlaps(rectangle)) {
                    SOUND.play(0.1f);
                    bomb.exploded = true;
                    collisionJustOccurred = true;
                }
            }
        }
    }

    public static boolean afterDeathUpdate(SpriteBatch batch) {
        boolean stillExploding = false;
        for (Bomb bomb : bombs) {
            if (bomb.exploded) {
                bomb.timeSinceExploded += Gdx.graphics.getDeltaTime();
                if (bomb.timeSinceExploded < EXPLOSION.getAnimationDuration()) {
                    TextureRegion frame = EXPLOSION.getKeyFrame(bomb.timeSinceExploded);
                    batch.draw(
                            frame,
                            bomb.rectangle.x -
                                    (EXPLOSION_SIZE - bomb.rectangle.height) / 2.0f,
                            bomb.rectangle.y -
                                    (EXPLOSION_SIZE - bomb.rectangle.width) / 2.0f
                    );
                    stillExploding = true;
                }
            }
        }
        return stillExploding;
    }

    private static void spawnBomb() {
        bombCount++;
        Rectangle newBombRect = new Rectangle(
                MathUtils.random(0, DodgeGame.WIDTH - IMAGE.getWidth()),
                DodgeGame.HEIGHT,
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

    public static boolean collisionJustOccurred() {
        if (collisionJustOccurred) {
            collisionJustOccurred = false;
            return true;
        } else {
            return false;
        }
    }

    public static boolean waveFinished() {
        return bombs.size == 0;
    }

    public static void multiplyExplosionDuration(float factor) {
        EXPLOSION.setFrameDuration(EXPLOSION.getFrameDuration() * factor);
    }
}
