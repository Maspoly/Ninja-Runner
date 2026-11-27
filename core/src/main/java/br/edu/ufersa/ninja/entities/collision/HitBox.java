package br.edu.ufersa.ninja.entities.collision;

import com.badlogic.gdx.math.Rectangle;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class HitBox {
    public Rectangle box;
    public boolean isActive; // Colide

    public HitBox(float x, float y, float width, float height) {
        box = new Rectangle(x, y, width, height);
        isActive = true;
    }

    public void setisActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Entity isOverlapping(MyList<Entity> other) {
        if (other == null || !isActive) {
            return null;
        }

        for (int i = 0; i < other.size(); i++) {
            Entity entity = other.get(i);
            if (entity != null && entity.hitbox != null
                    && entity.hitbox.isActive && box.overlaps(entity.hitbox.box)) {
                return entity;
            }
        }
        return null;
    }
}
