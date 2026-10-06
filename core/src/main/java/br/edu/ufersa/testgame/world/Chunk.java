package br.edu.ufersa.testgame.world;

import java.util.EnumMap;

public class Chunk {
    EnumMap<ChunkElement, Integer> count;
    int[][] chunk;
    int width;
    int height;

    public Chunk(int width, int height) {
        this.width = width;
        this.height = height;
        this.chunk = new int[height][width];
        this.count = new EnumMap<>(ChunkElement.class);
        for (ChunkElement element : ChunkElement.values()) {
            count.put(element, 0);
        }
    }

    public void incrementCount(ChunkElement element) {
        count.put(element, count.get(element) + 1);
    }
}
