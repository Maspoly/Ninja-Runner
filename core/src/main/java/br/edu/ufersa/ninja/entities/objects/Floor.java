package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class Floor extends Entity {
    TextureRegion texture;

    public Floor(float x, float y, float width, float height, TextureRegion texture) {
        super(x, y, width, height);
        this.texture = texture;
    }

    public void update(MyList<Entity> other, float delta) {
        // Floor não precisa de lógica de atualização, mas o método é necessário para
        // manter a consistência com outras entidades.
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, hitbox.box.x, hitbox.box.y);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    }
}
