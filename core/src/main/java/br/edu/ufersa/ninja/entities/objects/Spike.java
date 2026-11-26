package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class Spike extends Entity {
    public Spike(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    // Verifica se a entidade em movimento colidiu com o topo do Spike
    // e ajusta sua posição para simular o pouso.
    public boolean resolveLanding(Entity movingEntity, float previousY, float attemptedY) {
        if (movingEntity == null || !hitbox.isActive || movingEntity.hitbox == null) {
            return false;
        }

        // Verifica se a entidade em movimento está acima do Spike e se está tentando atravessá-lo
        float spikeTop = hitbox.box.y + hitbox.box.height;
        float entityLeft = movingEntity.hitbox.box.x;
        float entityRight = entityLeft + movingEntity.hitbox.box.width;
        boolean overlapsHorizontally = entityRight > hitbox.box.x
                && entityLeft < hitbox.box.x + hitbox.box.width;
        boolean crossedFromAbove = previousY >= spikeTop && attemptedY <= spikeTop;

        // Se a entidade em movimento colidiu com o topo do Spike,
        // ajusta sua posição para simular o pouso
        if (overlapsHorizontally && crossedFromAbove) {
            movingEntity.update(movingEntity.hitbox.box.x,
                    spikeTop - movingEntity.hitbox.box.height);
            return true;
        }
        return false;
    }

    // Causa 1 ponto de dano reduzindo as vidas do Player ao encostar.
    public boolean damagePlayer(Player player) {
        if (player == null || !hitbox.isActive || player.hitbox == null || !player.hitbox.isActive) {
            return false;
        }

        if (!hitbox.box.overlaps(player.hitbox.box)) {
            return false;
        }

        player.reduceLives(1);
        return true;
    }

    public void update(float delta, MyList<Entity> other) {
        //this.update(other);
    }

    public void render(ShapeRenderer shapeRenderer) {
        if (hitbox.isActive) {
            shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
        }
    }
}