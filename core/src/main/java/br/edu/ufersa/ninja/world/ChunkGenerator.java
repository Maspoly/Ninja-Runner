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
                chunk.chunk[y][currentColumn] = 0; // AIR
            }
            if (y == 1) {
                createPlatform(y);
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
            System.out.println("SPIKE");
            return randomElement(ChunkElement.AIR);
        } else {
            if (chunk.count.get(ChunkElement.SPIKE) >= 2) {
                return randomElement(ChunkElement.AIR);
            }
            if (getPreviousElement(y, 1) == ChunkElement.SPIKE.ordinal()
                    && getPreviousElement(y, 2) == ChunkElement.SPIKE.ordinal()
                    && getPreviousElement(y, 3) == ChunkElement.SPIKE.ordinal()) {
                return randomElement(ChunkElement.AIR);
            }
            if (getPreviousElement(y, 1) == ChunkElement.SPIKE.ordinal()
                    && getPreviousElement(y, 2) == ChunkElement.SPIKE.ordinal()) {
                return randomElement(ChunkElement.AIR, ChunkElement.SPIKE);
            }
            if (getPreviousElement(y, 1) == ChunkElement.SPIKE.ordinal()) {
                return randomElement(ChunkElement.AIR, ChunkElement.AIR, ChunkElement.SPIKE);
            }
        }
        return randomElement(ChunkElement.AIR, ChunkElement.AIR, ChunkElement.AIR, ChunkElement.SPIKE);
    }

    private ChunkElement randomFloorOrHole(int y) {
        if (chunk.count.get(ChunkElement.HOLE) >= 3) {
            return randomElement(ChunkElement.FLOOR);
        }
        if (getPreviousElement(y, 1) == ChunkElement.HOLE.ordinal()
                && getPreviousElement(y, 2) == ChunkElement.HOLE.ordinal()
                && getPreviousElement(y, 3) == ChunkElement.HOLE.ordinal()) {
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
        if (getPreviousElement(y, 3) == ChunkElement.PLATFORM.ordinal()) {
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
}
