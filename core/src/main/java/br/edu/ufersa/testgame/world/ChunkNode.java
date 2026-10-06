package br.edu.ufersa.testgame.world;

public class ChunkNode {
    Chunk chunk;
    ChunkNode next;

    public ChunkNode(Chunk chunk) {
        this.chunk = chunk;
        next = null;
    }
}