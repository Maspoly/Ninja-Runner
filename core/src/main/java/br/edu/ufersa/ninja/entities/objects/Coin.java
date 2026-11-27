package br.edu.ufersa.ninja.entities.objects;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;

public class Coin extends Item {

    private float shieldDuration;
    int Coins = 0;
    private float delta = 0.016f; // Valor padrão de delta (aproximadamente 60 FPS)
    private boolean isCollected = false;
    private float stateTime;
    private Texture idleSheet;
    private Player player;
    private Animation<TextureRegion> idleAnimation;
    private boolean isCoinCollectedByPlayer = false;

    public Coin(float x, float y, float width, float height) {
        super(x, y, width, height);

        this.idleSheet = new Texture("player/player_idle-Sheet.png");

        this.isCoinCollectedByPlayer = false;
        TextureRegion[][] frames = TextureRegion.split(
                this.idleSheet,
                this.idleSheet.getWidth() / 4,
                this.idleSheet.getHeight()
        );

        this.idleAnimation = new Animation<>(0.14F, frames[0]);
        this.idleAnimation.setPlayMode(PlayMode.LOOP);
        this.stateTime = 0.0F;
    }

    /// Adicionar a Item
    public void addBag(Item object, Player player){
        Entity dummy = verifyPlayer(player, object);
        if (dummy != null) {
            player.bag.add(object);

            System.out.println(player.bag.peek().getClass().getSimpleName() + " adicionado à bolsa do jogador.");

            isCollected = true;

        }
    }

    public void update(Player player) {
        this.stateTime += delta;

        
        if (!isCollected) {
            Shield object = new Shield();
            addBag(object, player);
        };

        addCoins(player); // Adiciona 1 moeda ao jogador
    }    

    // Desenha a animação do jogador.
    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, (int) (currentPos.x - hitbox.box.width / 2), (int) currentPos.y - 3);
    }

    public void render(ShapeRenderer shapeRenderer) {
            shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    
    }


    public void addCoins(Player player) {
        if (isCoinCollectedByPlayer == false) { 
            if(hitbox.box.overlaps(player.hitbox.box)) {
                player.addCoins();
                isCollected = true;
                System.out.println("Player collected a coin! Total coins: " + player.coinsCollected);
                isCoinCollectedByPlayer = true;
            }
        }
    }
}
