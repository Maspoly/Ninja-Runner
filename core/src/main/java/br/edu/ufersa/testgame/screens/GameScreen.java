package br.edu.ufersa.testgame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.ufersa.testgame.entities.Player;
import br.edu.ufersa.testgame.world.Mapper;
import br.edu.ufersa.testgame.world.WorldGenerator;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Viewport viewport;
    private Player player;
    private WorldGenerator mapGenerator;
    private Mapper mapper;
    private float worldSpeed;

    public GameScreen() {
        batch = new SpriteBatch();
        background = new Texture("background/viewporttest.png");
        viewport = new FitViewport(320, 180);
        player = new Player(100, 80);
        mapGenerator = new WorldGenerator(8, 7);
        mapper = new Mapper(mapGenerator);
        worldSpeed = 100.0f;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            mapper.moveToNextChunk();
        }
        player.update(delta);
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0);
        mapper.incrementWorldOffsetX(delta * worldSpeed);
        mapper.drawChunk(batch);
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