package br.edu.ufersa.ninja.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class ScoreScreen extends ScreenAdapter {
    private final Game game;
    private final Stage stage;
    private final Skin skin;
    private final FitViewport viewport;

    public ScoreScreen(Game game) {
        this.viewport = new FitViewport(320, 180);
        this.game = game;
        this.stage = new Stage(viewport);
        this.skin = new Skin(Gdx.files.internal("ui/buttons/button.json"));

        Gdx.input.setInputProcessor(stage);
        createMenu();
    }

    public void createMenu() {
        Table table = new Table();
        table.setFillParent(true);
        Button exitButton = new Button(skin, "exit");
        table.bottom().right().padRight(30).padBottom(60);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                if (exitButton.isPressed()) {
                    game.setScreen(new MenuScreen(game));
                }
            }
        });

        table.add(exitButton).padRight(20);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

}
