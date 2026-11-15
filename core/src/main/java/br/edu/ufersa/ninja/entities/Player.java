
package br.edu.ufersa.ninja.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.objects.Enemy;
import br.edu.ufersa.ninja.entities.objects.Grass;
import br.edu.ufersa.ninja.entities.objects.Heart;
import br.edu.ufersa.ninja.entities.objects.Jutsu;
import br.edu.ufersa.ninja.entities.objects.Shield;
import br.edu.ufersa.ninja.entities.objects.Spike;

public class Player extends Entity {

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

    // Controle de vida.
    private static final int MAX_LIVES = 5;
    private int lives = MAX_LIVES;

    // Controle de dano e proteção.
    private static final float DAMAGE_COOLDOWN = 0.75f;
    private float damageCooldownRemaining = 0.0f;
    private float shieldTimeRemaining = 0.0f;

    public Player(float x, float y, float width, float heigth) {
        super(x, y, width, heigth);

        this.idleSheet = new Texture("player/player_idle-Sheet.png");

        TextureRegion[][] frames = TextureRegion.split(
                this.idleSheet,
                this.idleSheet.getWidth() / 4,
                this.idleSheet.getHeight()
        );

        this.idleAnimation = new Animation<>(0.14F, frames[0]);
        this.idleAnimation.setPlayMode(PlayMode.LOOP);
        this.stateTime = 0.0F;
    }

    // Atualiza o jogador.
    public void update(float delta, List<Entity> other) {

        this.stateTime += delta;

        // Atualiza os temporizadores de dano e escudo.
        updateTimers(delta);

        List<Entity> otherWithoutPlayer = new ArrayList<>(other);
        otherWithoutPlayer.remove(this);

        float dx = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)
                || Gdx.input.isKeyPressed(Input.Keys.A)) {
            dx -= (this.speed * delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)
                || Gdx.input.isKeyPressed(Input.Keys.D)) {
            dx += (this.speed * delta);
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

        // Guarda a posição anterior para verificar o pouso em Spike.
        float previousPlayerY = currentPos.y;

        float attemptedY = currentPos.y + velocityY * delta;
        float attemptedX = currentPos.x + dx;

        // Atualiza a posição tentada.
        this.update(attemptedX, attemptedY);

        // Executa as interações com objetos não sólidos.
        handleObjectInteractions(otherWithoutPlayer);

        // Separa os objetos que participam da colisão física.
        List<Entity> solidEntities = getSolidEntities(otherWithoutPlayer);

        // Mantém a resolução de colisões herdada de Entity.
        this.update(solidEntities);

        // Verifica se o jogador pousou sobre um Spike.
        boolean landedOnSpike = false;

        for (Entity entity : otherWithoutPlayer) {
            if (entity instanceof Spike) {
                Spike spike = (Spike) entity;

                if (spike.resolveLanding(this, previousPlayerY, attemptedY)) {
                    landedOnSpike = true;
                }
            }
        }

        // Atualiza o estado do pulo após uma colisão vertical.
        if (Math.abs(currentPos.y - attemptedY) > 0.001f
                || landedOnSpike) {

            if (velocityY < 0 || landedOnSpike) {
                onGround = true;
            } else {
                onGround = false;
            }

            velocityY = 0;
            jumping = false;
        }
    }

    // Atualiza os temporizadores de dano e proteção.
    private void updateTimers(float delta) {

        if (damageCooldownRemaining > 0) {
            damageCooldownRemaining = Math.max(
                    0,
                    damageCooldownRemaining - delta
            );
        }

        if (shieldTimeRemaining > 0) {
            shieldTimeRemaining = Math.max(
                    0,
                    shieldTimeRemaining - delta
            );
        }
    }

    // Trata as interações com objetos que não devem bloquear o jogador.
    private void handleObjectInteractions(List<Entity> other) {

        for (Entity entity : other) {

            if (!entity.hitbox.isActive
                    || !hitbox.box.overlaps(entity.hitbox.box)) {
                continue;
            }

            if (entity instanceof Heart) {
                ((Heart) entity).collect(this);

            } else if (entity instanceof Shield) {
                ((Shield) entity).collect(this);

            } else if (entity instanceof Enemy) {
                ((Enemy) entity).contactPlayer(this);

            } else if (entity instanceof Jutsu) {
                ((Jutsu) entity).hitPlayer(this);

            } else if (entity instanceof Spike) {
                ((Spike) entity).damagePlayer(this);
            }
        }
    }

    // Retorna somente as entidades que participam da colisão física.
    private List<Entity> getSolidEntities(List<Entity> other) {

        List<Entity> solidEntities = new ArrayList<>();

        for (Entity entity : other) {

            if (entity instanceof Heart
                    || entity instanceof Shield
                    || entity instanceof Enemy
                    || entity instanceof Jutsu
                    || entity instanceof Spike
                    || entity instanceof Grass) {
                continue;
            }

            if (entity.hitbox.isActive) {
                solidEntities.add(entity);
            }
        }

        return solidEntities;
    }

    // Aplica dano ao jogador.
    public boolean takeDamage(int damage) {

        if (damage <= 0
                || lives <= 0
                || hasShield()
                || damageCooldownRemaining > 0) {
            return false;
        }

        lives = Math.max(0, lives - damage);
        damageCooldownRemaining = DAMAGE_COOLDOWN;

        return true;
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

    // Ativa ou renova a proteção temporária.
    public void activateShield(float duration) {

        if (duration > 0) {
            shieldTimeRemaining = Math.max(
                    shieldTimeRemaining,
                    duration
            );
        }
    }

    // Verifica se o escudo está ativo.
    public boolean hasShield() {
        return shieldTimeRemaining > 0;
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

        batch.draw(
                currentFrame,
                (int) currentPos.x,
                (int) currentPos.y
        );
    }

    // Desenha a hitbox do jogador.
    public void render(ShapeRenderer shapeRenderer) {

        shapeRenderer.rect(
                hitbox.box.x,
                hitbox.box.y,
                hitbox.box.width,
                hitbox.box.height
        );
    }

    // Libera a textura utilizada pelo jogador.
    public void dispose() {
        idleSheet.dispose();
    }
}
