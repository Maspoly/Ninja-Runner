package br.edu.ufersa.testgame.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.ufersa.testgame.entities.Player;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Viewport viewport;
    private Player player;

    public GameScreen() {
        batch = new SpriteBatch();
        background = new Texture("viewporttest.png");
        viewport = new StretchViewport(320, 180);
        player = new Player(100, 50);
    }

    @Override
    public void render(float delta) {
        player.update(delta);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0);
        player.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        player.dispose();
    }
}