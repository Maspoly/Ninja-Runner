package br.edu.ufersa.ninja.screens;

//import java.util.ArrayList;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import br.edu.ufersa.ninja.entities.DummyCol;
import br.edu.ufersa.ninja.entities.Entity;
import br.edu.ufersa.ninja.entities.Player;
import br.edu.ufersa.ninja.entities.MyList.MyList;
import br.edu.ufersa.ninja.entities.objects.Coin;
import br.edu.ufersa.ninja.entities.objects.Jutsu;

public class PilhaScreen extends ScreenAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private DummyCol dummyCol;
    private MyList<Entity> colliders;
    private Player player;
    //private Shield shield;
    private Coin coin;
    private Jutsu jutsu;

    public PilhaScreen() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        viewport = new FitViewport(320, 180);

        //shield = new Shield(150, 100, 16, 16, 5.0f); // Cria um escudo com duração de 5 segundos

        jutsu = new Jutsu(200, 100, 16, 16, 1, null); // Cria um jutsu com direção para a direita e sem dono

        player = new Player(100, 150, 16, 16);

        dummyCol = new DummyCol(80, 80, 200, 16);    

        colliders = new MyList<>(); // Lista de entidades da cena 
        colliders.add(dummyCol);
        colliders.add(player);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        player.update(delta, colliders);
        jutsu.update(delta, colliders, player); // Atualiza o jutsu e verifica colisões com o jogador
        //shield.update(player); // Atualiza o escudo e verifica se o jogador o coletou
        viewport.apply();
        
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // player.render(shapeRenderer); 
        dummyCol.render(shapeRenderer); 
        //shield.render(shapeRenderer);
        shapeRenderer.end();
        
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        player.render(batch);
        //shield.render(batch);
        jutsu.render(batch); // Renderiza o jutsu
        batch.end();
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
