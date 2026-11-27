
package br.edu.ufersa.ninja.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.MyList.MyList;
import br.edu.ufersa.ninja.entities.objects.Heart;
import br.edu.ufersa.ninja.entities.objects.Item;

public class Player extends Entity {

    private MyList<Heart> hearts;
    private float speed = 100.0F;
    private float stateTime;
    private Texture idleSheet;
    private Animation<TextureRegion> idleAnimation;

    // Física original do pulo.
    private float velocityY = 0;
    private final float GRAVITY = -800;
    private final float JUMP_SPEED = 180;
    private final float JUMP_FORCE = 500;
    private final float MAX_JUMP_TIME = 0.15f;
    private float jumpTime = 0;
    private boolean jumping = false;
    private boolean spaceWasPressed = false;
    private boolean onGround = true;
    public boolean falling = false;
    public boolean cutscene = false;

    // Controle de vida.
    private static final int MAX_LIVES = 5;
    private int lives = MAX_LIVES;

    // Controle de dano e proteção.
    private float shieldTimeRemaining = 0.0f;

    public Bag<Item> bag = new Bag<>();

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

    // Atualiza o jogador.
    public void update(float delta, MyList<Entity> other) {

        this.update(other);

        if (falling) {
            if (currentPos.y < 46) {
                hitbox.setisActive(false);
                jumping = false;
                onGround = false;
            } else {
                falling = false;
            }
        }
        if (currentPos.y < 46) {
            falling = true;
        }

        this.stateTime += delta;

        float dx = 0;

        if (!cutscene) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                    || Gdx.input.isKeyPressed(Input.Keys.A)) {
                dx -= (this.speed * delta);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                    || Gdx.input.isKeyPressed(Input.Keys.D)) {
                dx += (this.speed * delta);
            }
        }

        // Gravidade original.
        velocityY += GRAVITY * delta;

        // Verifica o acionamento do pulo.
        boolean spacePressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);

        if (spacePressed && !spaceWasPressed) {
            if (onGround) {
                velocityY = JUMP_SPEED;
                jumpTime = 0;
                jumping = true;
                onGround = false;
            }
        }

        // Sustentação do pulo enquanto o espaço está pressionado.
        if (spacePressed) {
            if (jumping && jumpTime < MAX_JUMP_TIME) {
                velocityY += JUMP_FORCE * delta;
                jumpTime += delta;
            }
        }

        // Interrompe parte da força do pulo ao soltar o espaço.
        if (!spacePressed && spaceWasPressed) {
            if (jumping) {
                if (velocityY > 0) {
                    velocityY *= 0.5f;
                }

                jumping = false;
            }
        }

        spaceWasPressed = spacePressed;

        // 1. Eixo X
        this.update(currentPos.x + dx, currentPos.y);
        this.update(other);

        // 2. Eixo Y
        if (cutscene) {
            velocityY = 0;
        }
        float attemptedY = currentPos.y + velocityY * delta;
        this.update(currentPos.x, attemptedY);
        this.update(other);

        // Verifica se o jogador pousou sobre um Spike.

        // Atualiza o estado do pulo após uma colisão vertical.
        if (Math.abs(currentPos.y - attemptedY) > 0.001f) {

            if (velocityY < 0) {
                onGround = true;
            } else {
                onGround = false;
            }

            velocityY = 0;
            jumping = false;
        }
    }

    // Ativa ou renova a proteção temporária.
    public void activateShield(float duration) {

        if (duration > 0) {
            shieldTimeRemaining = Math.max(
                    shieldTimeRemaining,
                    duration);
        }
    }

    // Verifica se o escudo está ativo.
    public boolean hasShield() {
        return shieldTimeRemaining > 0;
    }

    // Recupera vida sem ultrapassar o limite máximo.
    public boolean heal(int amount) {

        if (amount <= 0 || lives <= 0 || lives >= MAX_LIVES) {
            return false;
        }

        int previousLives = lives;

        lives = Math.min(MAX_LIVES, lives + amount);

        return lives > previousLives;
    }

    // Retorna a quantidade atual de vidas.
    public int getLives() {
        return lives;
    }

    // Retorna a quantidade máxima de vidas.
    public int getMaxLives() {
        return MAX_LIVES;
    }

    // Verifica se o jogador ainda está vivo.
    public boolean isAlive() {
        return lives > 0;
    }

    // Desenha a animação do jogador.
    public void render(SpriteBatch batch) {

        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, (int) (currentPos.x - hitbox.box.width / 2), (int) currentPos.y - 3);
    }

    // Desenha a hitbox do jogador.
    public void render(ShapeRenderer shapeRenderer) {

        shapeRenderer.rect(
                hitbox.box.x,
                hitbox.box.y,
                hitbox.box.width,
                hitbox.box.height);
    }

    // Libera a textura utilizada pelo jogador.
    public void dispose() {
        idleSheet.dispose();
    }

    public void setHearts(MyList<Heart> hearts) {
        this.hearts = hearts;
        Heart.updateHearts(this.hearts, this.lives);
    }

    // Reduz a quantidade de vidas do jogador e atualiza os corações
    public void reduceLives(int amount) {
        if (amount <= 0 || lives < 0) {
            return;
        }
        this.lives = this.lives - amount;
        Heart.updateHearts(this.hearts, this.lives);
    }
}
