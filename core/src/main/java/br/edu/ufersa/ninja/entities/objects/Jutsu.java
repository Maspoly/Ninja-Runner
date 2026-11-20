package br.edu.ufersa.ninja.entities.objects;

import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;

public class Jutsu extends Item {
    private static final float DEFAULT_SPEED = 250.0f;

    private float shieldDuration;
    private float delta = 0.016f; // Valor padrão de delta (aproximadamente 60 FPS)
    private boolean isCollected = false;
    private float stateTime;
    private Texture idleSheet;
    private Animation<TextureRegion> idleAnimation;
    private float speed = DEFAULT_SPEED;
    private int direction = 1;
    private Entity owner;

    public Jutsu(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public Jutsu(float x, float y, float width, float height, int direction, Entity owner) {
        this(x, y, width, height);
        setDirection(direction);
        this.owner = owner;

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

    public Jutsu() {
        this(0, 0, 16, 16, 1, null);
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

    /** Atualiza o projétil e verifica alvos sem aplicar colisão física. */
    public void update(float delta, List<Entity> other, Player player) {
        this.stateTime += delta;

        if (!isCollected) {
            Jutsu object = new Jutsu();
            addBag(object, player);
        } 

        if (!hitbox.isActive || delta <= 0) {
            return;
        }

        this.update(hitbox.box.x + direction * speed * delta, hitbox.box.y);

        for (Entity entity : other) {
            if (!hitbox.isActive || entity == this || entity == owner
                    || !entity.hitbox.isActive || !hitbox.box.overlaps(entity.hitbox.box)) {
                continue;
            }

            if (entity instanceof Player) {
                hitPlayer((Player) entity);
            } else if (entity instanceof Enemy) {
                hitEnemy((Enemy) entity);
            }
        }
    }

    /** Aplica dano ao Player; o método do Player respeita o escudo. */
    public boolean hitPlayer(Player player) {
        if (player == null || player == owner || !hitbox.isActive
                || !hitbox.box.overlaps(player.hitbox.box)) {
            return false;
        }

        boolean causedDamage = player.takeDamage(1);
        // O projétil é absorvido tanto pelo corpo quanto pelo escudo.
        hitbox.isActive = false;
        return causedDamage;
    }

    /** Aplica um ponto de dano ao inimigo. */
    public boolean hitEnemy(Enemy enemy) {
        if (enemy == null || !hitbox.isActive || !enemy.isAlive()
                || !hitbox.box.overlaps(enemy.hitbox.box)) {
            return false;
        }

        boolean causedDamage = enemy.takeDamage(1);
        hitbox.isActive = false;
        return causedDamage;
    }

    public void setOwner(Entity owner) {
        this.owner = owner;
    }

    public Entity getOwner() {
        return owner;
    }

    public void setDirection(int direction) {
        if (direction != 0) {
            this.direction = direction > 0 ? 1 : -1;
        }
    }

    public void setSpeed(float speed) {
        this.speed = Math.max(0, speed);
    }

    /// Adicionar a Item
    public void addBag(Item object, Player player){
        Entity dummy = verifyPlayer(player, object);
        if (dummy != null) {
            player.bag.add(object);

            System.out.println(player.bag.peek().getClass().getSimpleName() + " adicionado à bolsa do jogador.");

            isCollected = true;

        }
    }

    // Desenha a animação do jogador.
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, (int) (currentPos.x - hitbox.box.width / 2), (int) currentPos.y - 3);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    }
}