package br.edu.ufersa.testgame.world;

import java.util.EnumMap;

public class Chunk {
    EnumMap<ChunkElement, Integer> count;
    int[][] chunk;
    int width;
    int height;
    private int section;

    public Chunk(int width, int height) {
        this.width = width;
        this.height = height;
        this.chunk = new int[height][width];
        this.count = new EnumMap<>(ChunkElement.class);
        for (ChunkElement element : ChunkElement.values()) {
            count.put(element, 0);
        }
        this.section = 0;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public void incrementSection(Chunk chunk) {
        section = chunk.getSection() + 1;
    }

    public void incrementCount(ChunkElement element) {
        count.put(element, count.get(element) + 1);
    }
}
