package br.edu.ufersa.ninja.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;
import br.edu.ufersa.ninja.entities.objects.Floor;
import br.edu.ufersa.ninja.world.Mapper;
import br.edu.ufersa.ninja.world.WorldGenerator;

public class GameScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Texture background;
    private Viewport viewport;
    private Player player;
    private WorldGenerator mapGenerator;
    private Mapper mapper;
    private float worldSpeed;
    private List<Entity> colliders;
    private ShapeRenderer shapeRenderer;
    private Game game;

    public GameScreen(Game game) {
        batch = new SpriteBatch();
        background = new Texture("background/viewporttest.png");
        viewport = new FitViewport(320, 180);
        player = new Player(16, 80, 10, 10);
        mapGenerator = new WorldGenerator(8, 7);
        mapper = new Mapper(mapGenerator);
        worldSpeed = 100.0f;
        colliders = new ArrayList<>();
        colliders.add(player);
        shapeRenderer = new ShapeRenderer();
        mapper.moveToNextChunk();
        mapper.moveToNextChunk();
        mapper.moveToNextChunk();
        this.game = game;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.ENTER)) {
            mapper.moveToNextChunk();
        }
        player.update(delta, colliders);
        if (player.getCurrentPos().y < -1000) {
            game.setScreen(new MenuScreen(this.game));

        }
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Entity entity : colliders) {
            if (entity instanceof Floor) {
                Floor floor = (Floor) entity;
                floor.render(shapeRenderer);
            }
        }
        shapeRenderer.end();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0);
        mapper.incrementWorldOffsetX(delta * worldSpeed, colliders);
        mapper.drawChunk(batch, colliders);
        mapper.removeOffscreenFloors(colliders);
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
        game.dispose();
    }
}
