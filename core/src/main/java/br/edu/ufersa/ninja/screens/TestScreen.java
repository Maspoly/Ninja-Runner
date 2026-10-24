package br.edu.ufersa.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Rectangle rectangle;
    private Rectangle collisor;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private Vector2 previousPos;

    public TestScreen() {
        batch = new SpriteBatch();
        rectangle = new Rectangle(0, 0, 16, 16);
        collisor = new Rectangle(100, 100, 16, 16);
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(320, 180);
        previousPos = new Vector2(rectangle.x, rectangle.y);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        rectangleMove(delta);
        System.out.println("x rectangle: " + rectangle.x + " y rectangle: " + rectangle.y);
        if (rectangle.overlaps(collisor)) {
            Vector2 overlap = new Vector2();
            overlap = rectangle.getPosition(new Vector2()).sub(previousPos);
            if (overlap.x > 0 && previousPos.x + rectangle.width <= collisor.x) {
                rectangle.x = collisor.x - rectangle.width;
            } else if (overlap.x < 0 && previousPos.x >= collisor.x + collisor.width) {
                rectangle.x = collisor.x + collisor.width;
            }
            if (overlap.y > 0 && previousPos.y + rectangle.height <= collisor.y) {
                rectangle.y = collisor.y - rectangle.height;
            } else if (overlap.y < 0 && previousPos.y >= collisor.y + collisor.height) {
                rectangle.y = collisor.y + collisor.height;
            }
        }
        previousPos.set(rectangle.x, rectangle.y);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect((int) rectangle.x, (int) rectangle.y, rectangle.width, rectangle.height);
        shapeRenderer.rect(collisor.x, collisor.y, collisor.width, collisor.height);
        shapeRenderer.end();
    }

    public void rectangleMove(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rectangle.x += 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rectangle.x -= 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            rectangle.y += 200 * delta;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            rectangle.y -= 200 * delta;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}
