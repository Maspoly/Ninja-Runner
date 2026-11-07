package br.edu.ufersa.ninja.entities;

import java.util.List;
import java.util.ArrayList; //*

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Entity{
    private float speed = 100.0F;
    private float stateTime;
    private Texture idleSheet;
    private Animation<TextureRegion> idleAnimation;
    // Física do pulo
    private float velocityY = 0;
    private final float GRAVITY = -800;
    private final float JUMP_SPEED = 180;
    private final float JUMP_FORCE = 500;
    private final float MAX_JUMP_TIME = 0.15f;
    private float jumpTime = 0;
    private boolean jumping = false;
    private boolean spaceWasPressed = false;
    private boolean onGround = true;

    public Player(float x, float y, float width, float heigth) {
        super(x, y, width, heigth);
        this.idleSheet = new Texture("player/player_idle-Sheet.png");
        TextureRegion[][] frames = TextureRegion.split(
                this.idleSheet,
                this.idleSheet.getWidth() / 4,
                this.idleSheet.getHeight());
        this.idleAnimation = new Animation<>(0.14F, frames[0]);
        this.idleAnimation.setPlayMode(PlayMode.LOOP);
        this.stateTime = 0.0F;
    }


    // Modificar o pulo usando Entity
    public void update(float delta, List<Entity> other) {
        this.stateTime += delta;    

        List<Entity> otherWithoutPlayer = new ArrayList<>(other);
        otherWithoutPlayer.remove(this);

        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx -= (this.speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx += (this.speed * delta);
        }

        // Gravidade
        velocityY += GRAVITY * delta;

        // PULO - modificar a colisão com o cão usando a Entity
        boolean spacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
        // detectou espaço precionado e se não tinha sido precionado
        if (spacePressed && !spaceWasPressed) {
            if (onGround) {
                velocityY = JUMP_SPEED; // 180
                jumpTime = 0;
                jumping = true;
                onGround = false;
            }
        }
        // Se está precionando o espaço
        if (spacePressed) {
            if (jumping && jumpTime < MAX_JUMP_TIME) {
                velocityY += JUMP_FORCE * delta; // Aumenta a força do pulo
                jumpTime += delta;
            }
        }
        // Não está apertando o espaço e tinha apertado o espaço antes
        if (!spacePressed && spaceWasPressed) {
            if (jumping) {
                if (velocityY > 0) {
                    velocityY *= 0.5f;  // Diminuindo a velocidade
                }
                jumping = false;
            }
        }
        // Guarda o estado do SPACE
        spaceWasPressed = spacePressed;
        
        // Calcula a posição tentada Y com base na velocidade vertical e no delta
        float attemptedY = currentPos.y + velocityY * delta;

        this.update(currentPos.x + dx, attemptedY);
        this.update(otherWithoutPlayer);

        // Se a posição atual Y for diferente da posição tentada Y, significa que houve uma colisão vertical
        if (Math.abs(currentPos.y - attemptedY) > 0.001f) {
            if (velocityY < 0) {
                // Colidiu com a parte de cima da plataforma
                onGround = true;
            } else {
                // Bateu a cabeça na plataforma
                onGround = false;
            }

            velocityY = 0;
            jumping = false;
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, (int) currentPos.x, (int) currentPos.y);
    }

    public void dispose() {
        idleSheet.dispose();
    }
}
