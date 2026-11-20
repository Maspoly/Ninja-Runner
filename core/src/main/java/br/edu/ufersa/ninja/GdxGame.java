package br.edu.ufersa.ninja;

import com.badlogic.gdx.Game;

import br.edu.ufersa.ninja.screens.PilhaScreen;

public class GdxGame extends Game {
    @Override
    public void create() {
        setScreen(new PilhaScreen());
    }
}
