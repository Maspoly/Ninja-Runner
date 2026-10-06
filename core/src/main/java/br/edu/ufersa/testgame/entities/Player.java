package br.edu.ufersa.testgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private float speed = 100.0F;
    private float x;
    private float y;
    private float stateTime;
    private Texture idleSheet;
    private Animation<TextureRegion> idleAnimation;
    // Física do pulo
    private float velocityY = 0;
    private final float GRAVITY = -800;
    private final float JUMP_SPEED = 250;
    private final float JUMP_FORCE = 500;
    private final float MAX_JUMP_TIME = 0.15f;
    private float jumpTime = 0;
    private boolean jumping = false;
    private boolean spaceWasPressed = false;
    private boolean onGround = true;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.idleSheet = new Texture("player_idle-Sheet.png");
        TextureRegion[][] frames = TextureRegion.split(
                this.idleSheet,
                this.idleSheet.getWidth() / 4,
                this.idleSheet.getHeight());
        this.idleAnimation = new Animation<>(0.14F, frames[0]);
        this.idleAnimation.setPlayMode(PlayMode.LOOP);
        this.stateTime = 0.0F;
    }

    public void update(float delta) {
        this.stateTime += delta;
        // =========================
        // MOVIMENTO HORIZONTAL
        // =========================
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
                Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.x -= (this.speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
                Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.x += (this.speed * delta);
        }
        System.out.println("FPS: " + Gdx.graphics.getFramesPerSecond());
        // =========================
        // PULO
        // =========================
        boolean spacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        // Começou a apertar SPACE
        if (spacePressed && !spaceWasPressed) {
            if (onGround) {
                velocityY = JUMP_SPEED;
                jumpTime = 0;
                jumping = true;
                onGround = false;
            }
        }
        // Está segurando SPACE
        if (spacePressed) {
            if (jumping && jumpTime < MAX_JUMP_TIME) {
                velocityY += JUMP_FORCE * delta;
                jumpTime += delta;
            }
        }
        // Soltou SPACE
        if (!spacePressed && spaceWasPressed) {
            if (jumping) {
                if (velocityY > 0) {
                    velocityY *= 0.5f;
                }
                jumping = false;
            }
        }
        // Guarda o estado do SPACE
        spaceWasPressed = spacePressed;
        // =========================
        // GRAVIDADE
        // =========================
        velocityY += GRAVITY * delta;
        // =========================
        // MOVIMENTO VERTICAL
        // =========================
        this.y += velocityY * delta;
        // =========================
        // CHÃO
        // =========================
        if (this.y <= 50) {
            this.y = 50;
            velocityY = 0;
            onGround = true;
            jumping = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, (int) x, (int) y);
    }

    public void dispose() {
        idleSheet.dispose();
    }
}