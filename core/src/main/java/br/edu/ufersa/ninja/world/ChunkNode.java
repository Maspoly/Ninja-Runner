package br.edu.ufersa.ninja.world;

public class ChunkNode {
    private Chunk chunk;
    private ChunkNode next;
    private ChunkNode previous;

    public ChunkNode(Chunk chunk) {
        this.chunk = chunk;
        next = null;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public ChunkNode getNext() {
        return next;
    }

    public ChunkNode getPrevious() {
        return previous;
    }

    public void setPrevious(ChunkNode previous) {
        this.previous = previous;
    }

    public void setNext(ChunkNode next) {
        this.next = next;
    }

    public Chunk getChunkFlipped() {
        Chunk flippedChunk = new Chunk(chunk.width, chunk.height);
        int tempSection = chunk.getSection();
        flippedChunk.setSection(tempSection);
        for (int y = 0; y < chunk.height; y++) {
            for (int x = 0; x < chunk.width; x++) {
                flippedChunk.chunk[y][x] = chunk.chunk[y][x];
            }
        }
        for (int y = 0; y < flippedChunk.height / 2; y++) {
            for (int x = 0; x < flippedChunk.width; x++) {
                int temp = flippedChunk.chunk[y][x];
                flippedChunk.chunk[y][x] = flippedChunk.chunk[flippedChunk.height - 1 - y][x];
                flippedChunk.chunk[flippedChunk.height - 1 - y][x] = temp;
            }
        }
        return flippedChunk;
    }
}
