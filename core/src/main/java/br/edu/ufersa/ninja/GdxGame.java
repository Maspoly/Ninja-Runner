package br.edu.ufersa.ninja;

import com.badlogic.gdx.Game;

import br.edu.ufersa.ninja.screens.GameScreen;

public class GdxGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}

