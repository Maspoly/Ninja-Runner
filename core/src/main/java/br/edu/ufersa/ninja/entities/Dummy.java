package br.edu.ufersa.ninja.entities;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Dummy extends Entity {
    public Dummy(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void update(float delta, List<Entity> other) {
        rectangleMove(delta);
        this.update(other);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    }

    public void rectangleMove(float delta) {
        float dx = 0;
        float dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dx += 100 * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dx -= 100 * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dy += 100 * delta;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dy -= 100 * delta;
        }

        this.update(hitbox.box.x + dx, hitbox.box.y + dy);
    }

}
