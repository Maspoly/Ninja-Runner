package br.edu.ufersa.ninja.world;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ChunkToTile extends ApplicationAdapter {
    private static TextureRegion[][] tileSet = TextureRegion.split(new Texture("tile/tileset.png"), 16, 16);

    private ChunkToTile() {
        // Construtor privado para evitar instâncias
    }

    public static TextureRegion getTile(int tileIndex) {
        int tilesPerRow = 8; // Número de tiles por linha no tileset
        int row = tileIndex / tilesPerRow;
        int col = tileIndex % tilesPerRow;
        return tileSet[row][col];
    }

    @Override
    public void dispose() {
        for (TextureRegion[] row : tileSet) {
            for (TextureRegion tile : row) {
                if (tile.getTexture() != null) {
                    tile.getTexture().dispose();
                }
            }
        }
    }
}
