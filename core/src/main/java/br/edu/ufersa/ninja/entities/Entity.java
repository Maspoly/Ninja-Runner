package br.edu.ufersa.ninja.entities;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import br.edu.ufersa.ninja.entities.collision.HitBox;

public abstract class Entity {
    public HitBox hitbox;
    private Vector2 previousPos;
    private Vector2 currentPos;

    public Entity(float x, float y, float width, float height) {
        this.hitbox = new HitBox(x, y, width, height);
        previousPos = new Vector2(x, y);
        currentPos = new Vector2(x, y);
    }

    public void update(List<Entity> other) {
        List<Entity> perfectList = other;
        perfectList.remove(this);
        HitBox collider = hitbox.isOverlapping(perfectList) != null ? hitbox.isOverlapping(perfectList).hitbox : null;
        if (collider != null && hitbox.isActive && collider.isActive) {
            Rectangle colliderRect = collider.box;
            Vector2 overlap = new Vector2();
            overlap = hitbox.box.getPosition(new Vector2()).sub(previousPos);
            if (overlap.x > 0 && previousPos.x + hitbox.box.width <= colliderRect.x) {
                this.update(colliderRect.x - hitbox.box.width, currentPos.y);
            } else if (overlap.x < 0 && previousPos.x >= colliderRect.x + colliderRect.width) {
                this.update(colliderRect.x + colliderRect.width, currentPos.y);
            } else if (overlap.y > 0 && previousPos.y + hitbox.box.height <= colliderRect.y) {
                this.update(currentPos.x, colliderRect.y - hitbox.box.height);
            } else if (overlap.y < 0 && previousPos.y >= colliderRect.y + colliderRect.height) {
                this.update(currentPos.x, colliderRect.y + colliderRect.height);
            }
        }
    }

    public void update(float x, float y) {
        previousPos.set(currentPos);
        currentPos.set(x, y);
        hitbox.box.setPosition(x, y);
    }

}
