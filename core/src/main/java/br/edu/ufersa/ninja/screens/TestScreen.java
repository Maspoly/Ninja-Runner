package br.edu.ufersa.ninja.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TestScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private Rectangle rectangle;
    private Rectangle collisor;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;

    public TestScreen() {
        batch = new SpriteBatch();
        rectangle = new Rectangle(0, 0, 16, 16);
        collisor = new Rectangle(100, 100, 16, 16);
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(320, 180);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        rectangleMove(delta);
        System.out.println("x rectangle: " + rectangle.x + " y rectangle: " + rectangle.y);
        if (rectangle.overlaps(collisor)) {
            System.out.println("x collisor: " + collisor.x + " y collisor: " + collisor.y);
            if (rectangle.x < collisor.x + collisor.width / 2) {
                if (rectangle.y < collisor.y + collisor.height / 2) {
                    System.out.println("colidiu pelo canto inferior esquerdo");
                    rectangle.x = collisor.x - rectangle.width;
                    rectangle.y = collisor.y - rectangle.height;
                } else {
                    System.out.println("colidiu pelo canto superior esquerdo");
                    rectangle.x = collisor.x - rectangle.width;
                    rectangle.y = collisor.y + collisor.height;
                }
                System.out.println("colidiu pela esquerda");
                rectangle.x = collisor.x - rectangle.width;
            } else if (rectangle.x > collisor.x + collisor.width / 2) {
                System.out.println("colidiu pela direita");
                rectangle.x = collisor.x + collisor.width;
            }
        }
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
