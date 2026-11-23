package br.edu.ufersa.ninja.world;

import java.io.FileWriter;
import java.io.IOException;

public class WorldGenerator {
    private static final int INITIAL_GROUND_CHUNKS = 3;

    private int chunkWidth;
    private int chunkHeight;
    private int generatedCount;
    private ChunkNode first;
    private ChunkNode last;

    public WorldGenerator(int chunkWidth, int chunkHeight) {
        this.chunkWidth = chunkWidth;
        this.chunkHeight = chunkHeight;
        this.generatedCount = 0;
        this.first = null;
        this.last = null;
    }

    public void generateChunk() {
        Chunk chunk;

        if (generatedCount < INITIAL_GROUND_CHUNKS) {
            chunk = createGroundChunk();
        } else {
            Chunk baseChunk = (last != null) ? last.getChunk() : createGroundChunk();
            ChunkGenerator generator = new ChunkGenerator(chunkWidth, chunkHeight, baseChunk);
            for (int i = 0; i < chunkWidth; i++) {
                generator.generateColumn();
            }
            chunk = generator.getChunk();
        }

        addChunk(chunk);
        generatedCount++;
        saveDebugFile();
    }

    private Chunk createGroundChunk() {
        Chunk chunk = new Chunk(chunkWidth, chunkHeight);
        for (int y = 0; y < chunkHeight; y++) {
            for (int x = 0; x < chunkWidth; x++) {
                chunk.chunk[y][x] = ChunkElement.AIR.ordinal();
            }
        }

        int floorStartRow = Math.max(0, chunkHeight - 3);
        for (int y = floorStartRow; y < chunkHeight; y++) {
            for (int x = 0; x < chunkWidth; x++) {
                chunk.chunk[y][x] = ChunkElement.FLOOR.ordinal();
            }
        }

        return chunk;
    }

    private void addChunk(Chunk chunk) {
        int nextSection = (last == null) ? 0 : last.getChunk().getSection() + 1;
        chunk.setSection(nextSection); // se o campo for público ou tiver outro setter, ajuste aqui

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

    public ChunkNode getFirst() {
        return first;
    }

    public ChunkNode getLast() {
        return last;
    }
}
