package br.edu.ufersa.ninja.entities.collision;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import br.edu.ufersa.ninja.entities.Entity;

public class HitBox {
    public Rectangle box;
    public boolean isActive;

    public HitBox(float x, float y, float width, float height) {
        box = new Rectangle(x, y, width, height);
        isActive = true;
    }

    public Entity isOverlapping(List<Entity> other) {
        for (Entity entity : other) {
            if (entity.hitbox.isActive && box.overlaps(entity.hitbox.box)) {
                return entity;
            }
        }
        return null;
    }
}
