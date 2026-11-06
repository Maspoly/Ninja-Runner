package br.edu.ufersa.ninja.screens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.ufersa.ninja.entities.Dummy;
import br.edu.ufersa.ninja.entities.DummyCol;
import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;

public class TestScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Dummy dummy;
    private DummyCol dummyCol;
    private List<Entity> colliders;
    private Player player;

    public TestScreen() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(320, 180);
        dummy = new Dummy(0, 0, 16, 16);

        player = new Player(100, 150, 16, 16);

        
        dummyCol = new DummyCol(100, 100, 100, 16);

        colliders = new ArrayList<>(); // Lista de entidades da cena 
        colliders.add(dummyCol);
        colliders.add(dummy);
        colliders.add(player);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        player.update(delta, colliders);
        dummy.update(delta, colliders);
        // dummyCol.update(delta, colliders);

        viewport.apply();

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        player.render(batch);
        batch.end();

        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        dummy.render(shapeRenderer);  
        dummyCol.render(shapeRenderer);
        shapeRenderer.end();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        player.dispose();
    }
}
