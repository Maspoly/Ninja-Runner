package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class Spike extends Entity {

    private float stateTime;
    private Texture idleSheet;
    private Animation<TextureRegion> idleAnimation;
    private int health = 1;
    private final int maxHealth = 1;
    private boolean hitPlayer = false;

    public Spike(float x, float y, float width, float height) {
        super(x, y, width, height);

         hitbox.isActive = true;

        this.idleSheet = new Texture("player/player_idle-Sheet.png");

        TextureRegion[][] frames = TextureRegion.split(
                this.idleSheet,
                this.idleSheet.getWidth() / 4,
                this.idleSheet.getHeight());

        this.idleAnimation = new Animation<>(0.14F, frames[0]);
        this.idleAnimation.setPlayMode(PlayMode.LOOP);
        this.stateTime = 0.0F;
    }

    

    // Atualiza a posição do inimigo.
    public void update(MyList<Entity> other, float delta) {

        Player player = null;
        for (int i = 0; i < other.size(); i++) {
            Entity entity = other.get(i);
            if (entity instanceof Player) {
                player = (Player) entity;
                damagePlayerOnContact(player);
            }
        }
    }

    // Reduz a vida do player ao encostar no Enemy.
    public boolean damagePlayerOnContact(Player player) {

        if (player == null) {
            return false;
        }

        if (!hitbox.isActive || player.hitbox == null || !player.hitbox.isActive) {
            return false;
        }

        if (hitbox.box.overlaps(player.hitbox.box)) {
            if (!player.isAlive()) {
                return false;
            }
            if (hitPlayer == false) {
                player.reduceLives();
                System.out.println("Player hit by Enemy! Lives remaining: " + player.getLives());
                hitPlayer = true;
                return true;
            }
        }

        return false;
    }

    // Retorna a vida atual.
    public int getHealth() {
        return health;
    }

    // Retorna a vida máxima.
    public int getMaxHealth() {
        return maxHealth;
    }

    // Verifica se o inimigo continua vivo.
    public boolean isAlive() {
        return health > 0 && hitbox.isActive;
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, (int) (currentPos.x - hitbox.box.width / 2), (int) currentPos.y - 3);
    }

    // Desenha o inimigo.
    public void render(ShapeRenderer shapeRenderer) {
        if (hitbox.isActive) {
            shapeRenderer.rect(
                    hitbox.box.x,
                    hitbox.box.y,
                    hitbox.box.width,
                    hitbox.box.height);
        }
    }
}

