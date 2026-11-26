
package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class Plataform extends Entity {

    public Plataform(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void update(float delta, MyList<Entity> other) {
        this.update(other);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(
            hitbox.box.x,
            hitbox.box.y,
            hitbox.box.width,
            hitbox.box.height
        );
    }
}
