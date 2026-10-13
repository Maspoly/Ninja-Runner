package br.edu.ufersa.ninja.world;

import java.io.FileWriter;
import java.io.IOException;

public class WorldGenerator {
    private int chunkWidth;
    private int chunkHeight;
    private ChunkNode first;
    private ChunkNode last;

    public WorldGenerator(int chunkWidth, int chunkHeight) {
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
        this.first = null;
        this.last = null;
    }

    public void generateChunk() {
        if (first == null) {
            addChunk(createFirstChunk());
            saveDebugFile();
            return;
        }
        ChunkGenerator generator = new ChunkGenerator(chunkWidth, chunkHeight, last.getChunk());
        for (int i = 0; i < chunkWidth; i++) {
            generator.generateColumn();
        }
        addChunk(generator.getChunk());
        saveDebugFile();
    }

    private void saveDebugFile() {
        try (FileWriter writer = new FileWriter("world_debug.txt")) {
            for (int y = 0; y < chunkHeight; y++) {
                ChunkNode current = first;
                while (current != null) {
                    for (int x = 0; x < chunkWidth; x++) {
                        writer.write(current.getChunk().chunk[y][x] + " ");
                    }
                    current = current.getNext();
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Chunk createFirstChunk() {
        Chunk chunk = new Chunk(chunkWidth, chunkHeight);
        for (int y = 0; y < chunkHeight; y++) {
            for (int x = 0; x < chunkWidth; x++) {
                chunk.chunk[y][x] = ChunkElement.AIR.ordinal();
            }
        }
        for (int x = 0; x < chunkWidth; x++) {
            chunk.chunk[chunkHeight - 1][x] = ChunkElement.FLOOR.ordinal();
            chunk.chunk[chunkHeight - 2][x] = ChunkElement.FLOOR.ordinal();
            chunk.chunk[chunkHeight - 3][x] = ChunkElement.FLOOR.ordinal();
        }
        return chunk;
    }

    public ChunkNode getFirst() {
        return first;
    }

    public ChunkNode getLast() {
        return last;
    }

    private void addChunk(Chunk chunk) {
        ChunkNode newNode = new ChunkNode(chunk);
        if (first == null) {
            first = newNode;
            last = newNode;
            return;
        }
        last.setNext(newNode);
        newNode.setPrevious(last);
        last = newNode;
    }
}
