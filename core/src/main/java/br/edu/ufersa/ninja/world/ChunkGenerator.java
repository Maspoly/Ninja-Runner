
package br.edu.ufersa.ninja.world;

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

        for (int y = height - 1; y >= 0; y--) {

            if (y == 0) {
                chunk.chunk[y][currentColumn] = ChunkElement.AIR.ordinal();
            }

            if (y == 1) {
                chunk.chunk[y][currentColumn] = createPlatform(y).ordinal();
            }

            if (y == height - 1) {
                if (chunk.chunk[y][currentColumn] == ChunkElement.PLATFORM.ordinal()) {
                    chunk.chunk[y][currentColumn] = randomElement(ChunkElement.FLOOR, ChunkElement.HOLE).ordinal();
                } else {
                    chunk.chunk[y][currentColumn] = randomFloorOrHole(y).ordinal();
                }
            }

            if (y == 3) {
                chunk.chunk[y][currentColumn] = createSpike(y).ordinal();

                if (chunk.chunk[y][currentColumn] == ChunkElement.AIR.ordinal()) {
                    chunk.chunk[y][currentColumn] = createEnemy(y).ordinal();
                }
            }

            if (y == 0) {
                if (chunk.chunk[1][currentColumn] == ChunkElement.PLATFORM.ordinal()) {
                    chunk.chunk[y][currentColumn] = createEnemy(y).ordinal();
                }
            }
        }

        chunk.chunk[height
                - 2][currentColumn] = chunk.chunk[height - 3][currentColumn] = chunk.chunk[height - 1][currentColumn];

        currentColumn++;
    }

    private ChunkElement randomElement(ChunkElement... elements) {
        int randomIndex = (int) (Math.random() * elements.length);
        chunk.incrementCount(elements[randomIndex]);
        return elements[randomIndex];
    }

    private ChunkElement createSpike(int y) {

        if (chunk.chunk[height - 1][currentColumn] == ChunkElement.HOLE.ordinal()) {
            return randomElement(ChunkElement.AIR);
        }

        // Máximo de 3 espinhos por chunk.
        if (chunk.count.get(ChunkElement.SPIKE) >= 2) {
            return randomElement(ChunkElement.AIR);
        }

        // Evita espinhos consecutivos, mas não proíbe totalmente.
        if (getPreviousElement(y, 1) == ChunkElement.SPIKE.ordinal()) {
            return randomElement(
                    ChunkElement.AIR,
                    ChunkElement.AIR,
                    ChunkElement.AIR,
                    ChunkElement.SPIKE);
        }

        // Aumenta a chance de espinhos próximos.
        if (getPreviousElement(y, 2) == ChunkElement.SPIKE.ordinal()) {
            return randomElement(
                    ChunkElement.AIR,
                    ChunkElement.AIR,
                    ChunkElement.SPIKE,
                    ChunkElement.SPIKE);
        }

        // Probabilidade normal: aproximadamente 30%.
        return randomElement(
                ChunkElement.AIR,
                ChunkElement.AIR,
                ChunkElement.AIR,
                ChunkElement.SPIKE,
                ChunkElement.SPIKE);
    }

    private ChunkElement createEnemy(int y) {

        int floor = chunk.chunk[height - 1][currentColumn];

        if (floor == ChunkElement.HOLE.ordinal()
                || floor == ChunkElement.AIR.ordinal()) {
            return randomElement(ChunkElement.AIR);
        }

        // Máximo de 2 inimigos por chunk.
        if (chunk.count.get(ChunkElement.ENEMY) >= 2) {
            return randomElement(ChunkElement.AIR);
        }

        // Mantém distância entre inimigos.
        if (getPreviousElement(y, 1) == ChunkElement.ENEMY.ordinal()
                || getPreviousElement(y, 2) == ChunkElement.ENEMY.ordinal()
                || getPreviousElement(y, 3) == ChunkElement.ENEMY.ordinal()) {
            return randomElement(ChunkElement.AIR);
        }

        // Evita inimigos imediatamente após espinhos.
        if (getPreviousElement(3, 1) == ChunkElement.SPIKE.ordinal()) {
            return randomElement(ChunkElement.AIR);
        }

        // Probabilidade de 25%.
        return randomElement(
                ChunkElement.AIR,
                ChunkElement.AIR,
                ChunkElement.AIR,
                ChunkElement.ENEMY);
    }

    private ChunkElement randomFloorOrHole(int y) {

        if (getPreviousElement(y, 1) == ChunkElement.HOLE.ordinal()
                && getPreviousElement(y, 2) == ChunkElement.FLOOR.ordinal()) {
            return randomElement(ChunkElement.HOLE);
        }

        if (chunk.count.get(ChunkElement.HOLE) >= 2) {
            return randomElement(ChunkElement.FLOOR);
        }

        return randomElement(
                ChunkElement.FLOOR,
                ChunkElement.FLOOR,
                ChunkElement.FLOOR,
                ChunkElement.FLOOR,
                ChunkElement.HOLE);
    }

    private ChunkElement createPlatform(int y) {

        if (getPreviousElement(y, 1) == ChunkElement.PLATFORM.ordinal()) {

            if (getPreviousElement(y, 2) == ChunkElement.AIR.ordinal()) {
                return randomElement(ChunkElement.PLATFORM);
            }

            return randomElement(
                    ChunkElement.AIR,
                    ChunkElement.PLATFORM,
                    ChunkElement.PLATFORM);
        }

        return randomElement(
                ChunkElement.AIR,
                ChunkElement.AIR,
                ChunkElement.AIR,
                ChunkElement.PLATFORM,
                ChunkElement.PLATFORM);
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
}
