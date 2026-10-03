package br.edu.ufersa.testgame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private float speed = 100f;
    private float x;
    private float y;
    private float stateTime;
    private Texture idleSheet;
    private Animation<TextureRegion> idleAnimation;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        idleSheet = new Texture("player_idle-Sheet.png");
        TextureRegion[][] frames = TextureRegion.split(idleSheet, idleSheet.getWidth() / 4, idleSheet.getHeight());
        idleAnimation = new Animation<>(0.14f, frames[0]);
        idleAnimation.setPlayMode(Animation.PlayMode.LOOP);
        stateTime = 0f;
    }

    public void update(float delta) {
        stateTime += delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= (int) (speed * delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += (int) (speed * delta);
        }
    }

    public void render(SpriteBatch batch) {
        TextureRegion currentFrame = idleAnimation.getKeyFrame(stateTime);
        batch.draw(currentFrame, x, y);
    }

    public void dispose() {
        idleSheet.dispose();
    }
}