package br.edu.ufersa.ninja.world;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Mapper {
    private ChunkNode current;
    private WorldGenerator worldGenerator;
    private float worldOffsetX;
    private Map<Integer, Integer> spikesCordinates = new HashMap<>();

    public Mapper(WorldGenerator worldGenerator) {
        this.worldGenerator = worldGenerator;
        worldGenerator.generateChunk();
        this.current = worldGenerator.getFirst();
        this.worldOffsetX = 0;
    }

    public void moveToNextChunk() {
        if (current.getNext() != null) {
            current = current.getNext();
        } else {
            worldGenerator.generateChunk();
            current = current.getNext();
        }
    }

    public void drawChunk(SpriteBatch batch) {
        ChunkNode node = worldGenerator.getFirst();
        while (node != null) {
            Chunk chunk = node.getChunkFlipped();
            for (int y = 0; y < chunk.height; y++) {
                for (int x = 0; x < chunk.width; x++) {
                    if (chunk.chunk[y][x] == ChunkElement.FLOOR.ordinal()) {
                        drawGround(batch, node, chunk, x, y);
                    } else if ((chunk.chunk[y][x] == ChunkElement.AIR.ordinal())) {
                        batch.draw(ChunkToTile.getTile(63),
                                x * 16 + chunk.getSection() * chunk.width * 16 - worldOffsetX, y * 16);
                    } else if ((chunk.chunk[y][x] == ChunkElement.PLATFORM.ordinal())) {
                        drawPlatform(batch, node, chunk, x, y);
                    } else if ((chunk.chunk[y][x] == ChunkElement.SPIKE.ordinal())) {
                        drawSpike(batch, node, chunk, x, y);
                    }
                }
            }
            node = node.getNext();
        }
    }

    private int randomElement(int... elements) {
        int randomIndex = (int) (Math.random() * elements.length);
        return elements[randomIndex];
    }

    public void drawPlatform(SpriteBatch batch, ChunkNode node, Chunk chunk, int x, int y) {
        if (chunk.chunk[y][x] != ChunkElement.PLATFORM.ordinal()) {
            return;
        }
        if (getNextElement(node, x, y) == ChunkElement.AIR.ordinal()) {
            if (getPreviousElement(node, x, y) == ChunkElement.AIR.ordinal()) {
                universalDrawChunk(batch, chunk, x, y, 6);
            } else {
                universalDrawChunk(batch, chunk, x, y, 7);
            }
        } else if (getPreviousElement(node, x, y) == ChunkElement.AIR.ordinal()) {
            universalDrawChunk(batch, chunk, x, y, 5);
        } else {
            universalDrawChunk(batch, chunk, x, y, 6);
        }
    }

    public void drawSpike(SpriteBatch batch, ChunkNode node, Chunk chunk, int x, int y) {
        if (chunk.chunk[y][x] != ChunkElement.SPIKE.ordinal()) {
            return;
        }
        if (spikesCordinates.containsKey(x + chunk.getSection() * chunk.width)) {
            universalDrawChunk(batch, chunk, x, y, spikesCordinates.get(x + chunk.getSection() * chunk.width));
        } else if (getNextElement(node, x, y) == ChunkElement.AIR.ordinal()) {
            if (getPreviousElement(node, x, y) == ChunkElement.AIR.ordinal()) {
                int index = randomElement(16, 17, 18, 19, 20);
                universalDrawChunk(batch, chunk, x, y, index);
                spikesCordinates.put(x + chunk.getSection() * chunk.width, index);
            }
        }
    }

    public void drawGround(SpriteBatch batch, ChunkNode node, Chunk chunk, int x, int y) {
        if (chunk.chunk[y][x] != ChunkElement.FLOOR.ordinal()) {
            return;
        }
        if (chunk.chunk[y + 1][x] == ChunkElement.AIR.ordinal()) {
            if (getNextElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                if (getPreviousElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                    universalDrawChunk(batch, chunk, x, y, 3);
                } else {
                    universalDrawChunk(batch, chunk, x, y, 2);
                }
            } else if (getPreviousElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                if (getNextElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                    universalDrawChunk(batch, chunk, x, y, 3);
                } else {
                    universalDrawChunk(batch, chunk, x, y, 0);
                }
            } else {
                universalDrawChunk(batch, chunk, x, y, 1);
            }
            return;
        } else if (chunk.chunk[y + 1][x] == ChunkElement.SPIKE.ordinal()) {
            if (getNextElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                if (getPreviousElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                    universalDrawChunk(batch, chunk, x, y, 27);
                } else {
                    universalDrawChunk(batch, chunk, x, y, 26);
                }
            } else if (getPreviousElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                if (getNextElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                    universalDrawChunk(batch, chunk, x, y, 27);
                } else {
                    universalDrawChunk(batch, chunk, x, y, 24);
                }
            } else {
                universalDrawChunk(batch, chunk, x, y, 25);
            }
            return;
        } else {
            if (getNextElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                if (getPreviousElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                    universalDrawChunk(batch, chunk, x, y, 11);
                } else {
                    universalDrawChunk(batch, chunk, x, y, 10);
                }
            } else if (getPreviousElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                if (getNextElement(node, x, y) == ChunkElement.HOLE.ordinal()) {
                    universalDrawChunk(batch, chunk, x, y, 11);
                } else {
                    universalDrawChunk(batch, chunk, x, y, 8);
                }
            } else {
                universalDrawChunk(batch, chunk, x, y, 9);
            }
        }
    }

    public int getNextElement(ChunkNode node, int x, int y) {
        if (node == null) {
            return 0;
        }
        if (x >= node.getChunk().width - 1) {
            return node.getNext() != null ? getNextElement(node.getNext(), x - node.getChunk().width, y) : 0;
        }
        Chunk chunk = node.getChunkFlipped();
        return chunk.chunk[y][x + 1];
    }

    public int getPreviousElement(ChunkNode node, int x, int y) {
        if (node == null) {
            return 0;
        }
        if (x <= 0) {
            return node.getPrevious() != null ? getPreviousElement(node.getPrevious(), x + node.getChunk().width, y)
                    : 0;
        }
        Chunk chunk = node.getChunkFlipped();
        return chunk.chunk[y][x - 1];
    }

    public void incrementWorldOffsetX(float x) {
        this.worldOffsetX += x;
    }

    public void universalDrawChunk(SpriteBatch batch, Chunk chunk, int x, int y, int tile) {
        float offsetX = (x * 16 + chunk.getSection() * chunk.width * 16) - worldOffsetX;
        batch.draw(ChunkToTile.getTile(tile), offsetX, y * 16);
    }
}
