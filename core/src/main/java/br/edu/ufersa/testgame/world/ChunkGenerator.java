package br.edu.ufersa.testgame.world;

import java.io.FileWriter;
import java.io.IOException;

public class ChunkGenerator {
    private int width;
    private int height;
    private int currentColumn;
    private Chunk chunk;
    private Chunk previousChunk;

    public ChunkGenerator(int width, int height, Chunk previousChunk) {
        this.width = width;
        this.height = height;
        this.previousChunk = previousChunk;
        currentColumn = 0;
        chunk = new Chunk(width, height);
        chunk.incrementSection(previousChunk);
    }

    public void generateColumn() {
        if (currentColumn >= width) {
            return;
        }
        for (int y = 0; y < height; y++) {
            if (y == 0) {
                chunk.chunk[y][currentColumn] = 0; // AIR
            }
            // verifica se a linha atual é a segunda linha da coluna, que é onde as
            // plataformas podem ser geradas
            if (y == 1) {
                createPlatform(y);
            }
            // verifica se a linha atual é a última linha da coluna, que é onde o piso deve
            // ser gerado
            if (y == height - 1) {
                // se houver plataforma, a regra de tamanho de buraco não se aplica, então gera
                // o piso normalmente
                if (chunk.chunk[y][currentColumn] == ChunkElement.PLATFORM.ordinal()) {
                    chunk.chunk[y][currentColumn] = randomElement(ChunkElement.FLOOR, ChunkElement.HOLE).ordinal();
                } else {
                    // gera o piso, e escolhe se é chão ou buraco, com base na coluna anterior
                    chunk.chunk[y][currentColumn] = randomFloorOrHole(y).ordinal();
                }
            }
        }
        // as três últimas linhas da coluna devem ser iguais
        chunk.chunk[height
                - 2][currentColumn] = chunk.chunk[height - 3][currentColumn] = chunk.chunk[height - 1][currentColumn];
        // final da geração da coluna, incrementa o currentColumn e salva o arquivo de
        // debug
        currentColumn++;
        // saveDebugFile();
    }

    // Gera um elemento aleatório da lista de elementos fornecida, e incrementa o
    // contador do elemento gerado
    private ChunkElement randomElement(ChunkElement... elements) {
        int randomIndex = (int) (Math.random() * elements.length);
        chunk.incrementCount(elements[randomIndex]);
        return elements[randomIndex];
    }

    // Garante que não haja mais de 2 buracos consecutivos, e que não haja mais de 2
    // buracos na chunk
    private ChunkElement randomFloorOrHole(int y) {
        if (chunk.count.get(ChunkElement.HOLE) >= 2) {
            return randomElement(ChunkElement.FLOOR);
        }
        if (getPreviousElement(y, 1) == ChunkElement.HOLE.ordinal()
                && getPreviousElement(y, 2) == ChunkElement.HOLE.ordinal()) {
            return randomElement(ChunkElement.FLOOR);
        }
        return randomElement(ChunkElement.FLOOR, ChunkElement.FLOOR, ChunkElement.HOLE);
    }

    private void createPlatform(int y) {
        if (getPreviousElement(y, 1) == ChunkElement.PLATFORM.ordinal()) {
            if (getPreviousElement(y, 2) == ChunkElement.PLATFORM.ordinal()
                    && getPreviousElement(y, 3) == ChunkElement.PLATFORM.ordinal()) {
                chunk.chunk[y][currentColumn] = ChunkElement.AIR.ordinal();
                return;
            }
            chunk.chunk[y][currentColumn] = ChunkElement.PLATFORM.ordinal();
            return;
        }
        if (getPreviousElement(y, 2) == ChunkElement.PLATFORM.ordinal()) {
            chunk.chunk[y][currentColumn] = ChunkElement.AIR.ordinal();
            return;
        }
        if (chunk.count.get(ChunkElement.PLATFORM) < 2) {
            chunk.chunk[y][currentColumn] = randomElement(ChunkElement.AIR, ChunkElement.AIR, ChunkElement.PLATFORM)
                    .ordinal();
        } else {
            chunk.chunk[y][currentColumn] = ChunkElement.AIR.ordinal();
        }
    }

    private int getPreviousElement(int y, int distance) {
        int x = currentColumn - distance;
        if (x >= 0) {
            return chunk.chunk[y][x];
        }
        if (previousChunk != null) {
            return previousChunk.chunk[y][previousChunk.width + x];
        }
        return ChunkElement.AIR.ordinal();
    }

    public Chunk getChunk() {
        return chunk;
    }

    private void saveDebugFile() {
        try (FileWriter writer = new FileWriter("map_debug.txt")) {
            for (int y = 0; y < chunk.height; y++) {
                for (int x = 0; x < currentColumn; x++) {
                    writer.write(chunk.chunk[y][x] + " ");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}