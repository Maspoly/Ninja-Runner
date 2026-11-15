package br.edu.ufersa.ninja.entities.objects;

import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;

public class Spike extends Entity {
    public Spike(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    // Verifica se a entidade em movimento colidiu com o topo do Spike
    //  e ajusta sua posição para simular o pouso.
    public boolean resolveLanding(Entity movingEntity, float previousY, float attemptedY) {
        if (movingEntity == null || !hitbox.isActive) {
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

    //Causa um ponto de dano; a invulnerabilidade é controlada pelo Player. 
    public boolean damagePlayer(Player player) {
        if (player == null || !hitbox.isActive || !hitbox.box.overlaps(player.hitbox.box)) {
            return false;
        }
        return player.takeDamage(1);
    }

    // Espinhos também ferem inimigos ao encostar neles. 
    public boolean damageEnemy(Enemy enemy) {
        if (enemy == null || !hitbox.isActive || !hitbox.box.overlaps(enemy.hitbox.box)) {
            return false;
        }
        return enemy.takeDamage(1);
    }

    public void update(float delta, List<Entity> other) {
        //this.update(other);
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    }
    
}
