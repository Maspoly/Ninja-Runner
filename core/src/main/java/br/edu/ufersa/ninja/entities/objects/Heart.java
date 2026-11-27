package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class Heart extends Entity {

    private static final int MAX_LIVES = 3;

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

    public void update(float delta, MyList<Entity> other) {
        // this.update(other);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (hitbox.isActive) {
            shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
        }
    }

    public static void updateHearts(MyList<Heart> hearts, int lives) {
        if (hearts == null) {
            return;
        }

        int activeCount = Math.max(0, Math.min(MAX_LIVES, lives));

        for (int i = 0; i < hearts.size(); i++) {
            Heart heart = hearts.get(i);
            if (heart == null || heart.hitbox == null) {
                continue;
            }
        }

        int index = 0;
        for (int i = 0; i < hearts.size(); i++) {
            Heart heart = hearts.get(i);
            if (heart != null && heart.hitbox != null) {
                heart.hitbox.isActive = index < activeCount;
                index++;
            }
        }
    }
}
