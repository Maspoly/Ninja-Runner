package br.edu.ufersa.ninja.entities.objects;

import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;

public class Shield extends Entity {
    private static final float DEFAULT_SHIELD_DURATION = 5.0f;

    private float shieldDuration; 

    public Shield(float x, float y, float width, float height) {
        this(x, y, width, height, DEFAULT_SHIELD_DURATION);
    }

    public Shield(float x, float y, float width, float height, float shieldDuration) {
        super(x, y, width, height);
        this.shieldDuration = Math.max(0, shieldDuration);
    }

    //Coleta o item e ativa a proteção temporária do jogador.
    public boolean collect(Player player) {
        if (player == null || !hitbox.isActive || !hitbox.box.overlaps(player.hitbox.box)) {
            return false;
        }
        if (shieldDuration <= 0) {
            return false;
        }

        player.activateShield(shieldDuration);
        hitbox.isActive = false;
        return true;
    }

    public float getShieldDuration() {
        return shieldDuration;
    }

    public void update(float delta, List<Entity> other) {
        //this.update(other);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (hitbox.isActive) {
            shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
        }
    }
}
