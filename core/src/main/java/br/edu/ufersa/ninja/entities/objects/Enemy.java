
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

    // Aplica dano ao jogador quando ocorre contato.
    public boolean contactPlayer(Player player) {
        if (player == null || !hitbox.isActive) {
            return false;
        }

        if (!hitbox.box.overlaps(player.hitbox.box)) {
            return false;
        }

        return player.takeDamage(1);
    }

    // Reduz a vida do inimigo.
    public boolean takeDamage(int damage) {
        if (damage <= 0 || !isAlive()) {
            return false;
        }

        health = Math.max(0, health - damage);

        // Desativa a hitbox quando o inimigo perde toda a vida.
        if (health == 0) {
            hitbox.isActive = false;
        }

        return true;
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
