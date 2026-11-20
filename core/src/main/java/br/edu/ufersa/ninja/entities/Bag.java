package br.edu.ufersa.ninja.entities;

import java.util.ArrayList;
import java.util.List;
import br.edu.ufersa.ninja.entities.objects.Item;

public class Bag<T extends Item> {
    private static final int MAX_CAPACITY = 3;

    private static class Node<E> {
        private E value;
        private Node<E> next;
        private Node<E> previous;

        private Node(E value) {
            this.value = value;
        }
    }

    private Node<T> topNode;
    private Node<T> bottomNode;
    private int size;

    public Bag() {
        this.topNode = null;
        this.bottomNode = null;
        this.size = 0;
    }

    public T add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to the bag.");
        }

        // Se a bolsa estiver vazia, cria o primeiro nó e define topNode e bottomNode para ele.
        if (isEmpty()) {
            Node<T> firstNode = new Node<>(item);
            firstNode.next = firstNode;
            firstNode.previous = firstNode;

            topNode = firstNode;
            bottomNode = firstNode;
            size = 1;
            return null;
        }

        // Se a bolsa estiver cheia, substitui o item mais antigo (bottomNode) pelo novo item.
        if (isFull()) {
            T overwrittenItem = bottomNode.value;
            bottomNode.value = item;

            topNode = bottomNode;
            bottomNode = bottomNode.next;
            return overwrittenItem;
        }

        Node<T> newNode = new Node<>(item);
        newNode.previous = topNode;
        newNode.next = bottomNode;

        topNode.next = newNode;
        bottomNode.previous = newNode;
        topNode = newNode;
        size++;

        return null;
    }

    public T remove() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot remove from an empty bag.");
        }

        Node<T> nodeToRemove = topNode;
        T removedItem = nodeToRemove.value;

        // Se houver apenas um item na bolsa, redefine topNode e bottomNode para null.
        if (size == 1) {
            topNode = null;
            bottomNode = null;
        } else {
            topNode = nodeToRemove.previous;
            topNode.next = bottomNode;
            bottomNode.previous = topNode;
        }

        nodeToRemove.value = null;
        nodeToRemove.next = null;
        nodeToRemove.previous = null;
        size--;

        return removedItem;
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Bag is empty.");
        }
        return topNode.value;
    }

    // Retorna uma lista com os itens do mais antigo para o mais recente.
    public List<T> getItems() {
        List<T> items = new ArrayList<>(size);
        Node<T> currentNode = bottomNode;

        for (int step = 0; step < size; step++) {
            items.add(currentNode.value);
            currentNode = currentNode.next;
        }

        return items;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == MAX_CAPACITY;
    }

    public int size() {
        return size;
    }
}