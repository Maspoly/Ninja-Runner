
package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;

public class Enemy extends Entity {

    private int health = 1;
    private final int maxHealth = 1;

    public Enemy(float x, float y, float width, float height) {
        super(x, y, width, height);

        // Mantém a hitbox ativa para permitir detectar colisões
        // com Player, Jutsu e Spike.
        hitbox.isActive = true;
    }

    // Atualiza a posição do inimigo.
    public void update(float x, float y) {
        super.update(x, y);
    }

    // Reduz a vida do player ao encostar no Enemy.
    public boolean damagePlayerOnContact(Player player, int damage) {   
    if (player == null || damage <= 0) {
        return false;
    }

    if (!hitbox.isActive || player.hitbox == null || !player.hitbox.isActive) {
        return false;
    }

    if (hitbox.box.overlaps(player.hitbox.box)) {
        if (!player.isAlive()) {
            return false;
        }
        player.reduceLives(damage);
        return true;
    }

    return false;
    }

    // Retorna a vida atual.
    public int getHealth() {
        return health;
    }

    // Retorna a vida máxima.
    public int getMaxHealth() {
        return maxHealth;
    }

    // Verifica se o inimigo continua vivo.
    public boolean isAlive() {
        return health > 0 && hitbox.isActive;
    }

    // Desenha o inimigo.
    public void render(ShapeRenderer shapeRenderer) {
        if (hitbox.isActive) {
            shapeRenderer.rect(
                hitbox.box.x,
                hitbox.box.y,
                hitbox.box.width,
                hitbox.box.height
            );
        }
    }
}
