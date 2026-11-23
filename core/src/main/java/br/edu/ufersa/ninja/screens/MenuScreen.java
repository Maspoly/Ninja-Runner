
package br.edu.ufersa.ninja.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import br.edu.ufersa.ninja.entities.Player;

public class MenuScreen extends ScreenAdapter {
    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private final FitViewport viewport;
    private SpriteBatch batch;
    private final Texture ground;
    private final Texture ground2;
    private final Texture background;
    private float groundX = 0.0f;
    private float ground2X = 320.0f;
    private Player player;

    public MenuScreen(Game game) {
        this.viewport = new FitViewport(320, 180);
        this.game = game;
        this.stage = new Stage(viewport);
        this.skin = new Skin(Gdx.files.internal("ui/buttons/button.json"));
        this.ground = new Texture(Gdx.files.internal("background/menu-ground.png"));
        this.ground2 = new Texture(Gdx.files.internal("background/menu-ground.png"));
        this.background = new Texture(Gdx.files.internal("background/viewporttest.png"));
        this.batch = new SpriteBatch();
        player = new Player(-32, 48, 16, 16);
        player.cutscene = true; // Impede que o jogador se mova durante a cutscene

        Gdx.input.setInputProcessor(stage);
        createMenu();
    }

    public void createMenu() {
        Table table = new Table();
        table.setFillParent(true);
        Button startButton = new Button(skin, "start");
        Button scoreButton = new Button(skin, "score");
        Button exitButton = new Button(skin, "exit");
        table.bottom().right().padRight(30).padBottom(60);
        startButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (startButton.isPressed()) {
                    game.setScreen(new GameScreen(game));
                }
            }
        });

        scoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (scoreButton.isPressed()) {
                    game.setScreen(new ScoreScreen(game));
                }
            }
        });

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (exitButton.isPressed()) {
                    Gdx.app.exit();
                }
            }
        });
        table.add(startButton).padRight(20);
        table.row();
        table.add(scoreButton).padRight(20).padTop(10);
        table.row();
        table.add(exitButton).padRight(20).padTop(10);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        groundX -= 100.0 * delta;
        ground2X -= 100.0 * delta;
        if (groundX <= -320.0f) {
            groundX = ground2X + 320.0f;
        }
        if (ground2X <= -320.0f) {
            ground2X = groundX + 320.0f;
        }

        player.update(delta, null);
        if (player.getCurrentPos().x < 48) {
            player.update(player.getCurrentPos().x + delta * 100.0f, 48);
        } else {
            player.update(48, 48);
        }

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(background, 0, 0);
        batch.draw(ground, groundX, 0);
        batch.draw(ground2, ground2X, 0);
        player.render(batch);
        batch.end();

        if (player.getCurrentPos().x >= 48) {
            stage.act(delta);
            stage.draw();
        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        ground.dispose();
    }

}
