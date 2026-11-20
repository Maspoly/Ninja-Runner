package br.edu.ufersa.ninja.entities.objects;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;

public class Floor extends Entity {
    TextureRegion texture;

    public Floor(float x, float y, float width, float height, TextureRegion texture) {
        super(x, y, width, height);
        this.texture = texture;
    }

    public void update(float delta, List<Entity> other) {
        this.update(other);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, hitbox.box.x, hitbox.box.y);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    }
}
