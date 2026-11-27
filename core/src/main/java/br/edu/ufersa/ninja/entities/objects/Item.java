package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public abstract class Item extends Entity {
    public Item(float x, float y, float width, float height) {
        super(x, y, width, height);
        hitbox.isActive = false;
    }

    public void update(MyList<Entity> other, float delta) {
    }

    public Entity verifyPlayer(Player player, Entity entity) {
        if (this.hitbox.box.overlaps(player.hitbox.box)) {
            return entity;
        }

        return null;
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    }
}
