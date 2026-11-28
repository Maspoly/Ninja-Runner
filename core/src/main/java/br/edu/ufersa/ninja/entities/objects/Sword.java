package br.edu.ufersa.ninja.entities.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;
import br.edu.ufersa.ninja.entities.MyList.MyList;

public class Sword extends Entity{
    private boolean isCollected = false;
    private float stateTime;
    private Texture idleSheet;
    private Player player;
    private Animation<TextureRegion> idleAnimation;
    private boolean isCoinCollectedByPlayer = false;

    public Sword(float x, float y, float width, float height) {
        super(x, y, 32, 16);

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

    public void update(MyList<Entity> other, float delta) {

        Player player = null;
        for (int i = 0; i < other.size(); i++) {
            Entity entity = other.get(i);
            if (entity instanceof Player) {
                player = (Player) entity;
            }
        }

            if (player == null) {
            return;
        }

        stateTime += delta;

        float offsetX = 20f;
        float offsetY = 0f;

        this.currentPos.set(player.getCurrentPos().x + offsetX, player.getCurrentPos().y + offsetY);

        if (this.hitbox != null && this.hitbox.box != null) {
            this.hitbox.box.setPosition(
                    this.currentPos.x - this.hitbox.box.width / 2f,
                    this.currentPos.y - 3f
            );
        }
    }

    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.rect(hitbox.box.x, hitbox.box.y, hitbox.box.width, hitbox.box.height);
    
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, (int) (currentPos.x - hitbox.box.width / 2), (int) currentPos.y - 3);
    }
    
}
