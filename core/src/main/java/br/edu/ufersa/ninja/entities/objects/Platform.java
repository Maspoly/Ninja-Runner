
package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class Platform extends Entity {

    public Platform(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void update(MyList<Entity> other, float delta) {
        // Platform doesn't need any update logic, but the method is required for consistency.
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
                hitbox.box.x,
                hitbox.box.y,
                hitbox.box.width,
                hitbox.box.height);
    }

}
