
package br.edu.ufersa.ninja.entities.objects;

import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;

public class Plataform extends Entity {

    public Plataform(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void update(float delta, List<Entity> other) {
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
