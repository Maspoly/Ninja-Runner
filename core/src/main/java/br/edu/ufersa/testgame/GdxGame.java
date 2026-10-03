package br.edu.ufersa.testgame;

import com.badlogic.gdx.Game;

import br.edu.ufersa.testgame.screens.GameScreen;

public class GdxGame extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}