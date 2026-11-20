package br.edu.ufersa.ninja.entities.objects;

import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;

public class Heart extends Entity {
    public Heart(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public boolean collect(Player player) {
        if (player == null || !hitbox.isActive || !hitbox.box.overlaps(player.hitbox.box)) {
            return false;
        }

        if (player.heal(1)) {
            hitbox.isActive = false;
            return true;
        }
        return false;
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
