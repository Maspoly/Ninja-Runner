package br.edu.ufersa.ninja.entities.MyList;

public class MyList<T> {

    private int currentIndex = 0;
    private Object[] elementos;
    private int tamanho;
    private final int CAPACIDADE = 1;

    public MyList() {
        this.elementos = new Object[CAPACIDADE];
        this.tamanho = 0;
    }

    // Construtor de cópia
    public MyList(MyList<T> other) {
        this.tamanho = other.size();
        this.elementos = new Object[Math.max(CAPACIDADE, this.tamanho)];
        for (int i = 0; i < this.tamanho; i++) {
            this.elementos[i] = other.get(i);
        }
    }

    public void add(T element) {
        int newSize = tamanho + 1;
        Object[] newArray = new Object[newSize];
        for (int i = 0; i < tamanho; i++) {
            newArray[i] = elementos[i];
        }

        newArray[tamanho] = element;
        this.tamanho = newSize;
        this.elementos = newArray;
    }

    public T get(int index) {
        if (index < 0 || index >= tamanho) {
            throw new IndexOutOfBoundsException("Índice inválido: " + index);
        }
        return (T) elementos[index];
    }

    public T next() {
        if (currentIndex >= tamanho) {
            currentIndex = 0;
            return null;
        }
        return get(currentIndex++);
    }

    public void resetCursor() {
        currentIndex = 0;
    }

    public void remove(T element) {
        if (tamanho == 0) {
            return;
        }

        for (int i = 0; i < tamanho; i++) {
            if (elementos[i] != null && elementos[i].equals(element)) {
                for (int j = i; j < tamanho - 1; j++) {
                    elementos[j] = elementos[j + 1];
                }

                elementos[tamanho - 1] = null;
                tamanho--;
                return;
            }
        }
    }

    public int size() {
        return tamanho;
    }

    public T[] toArray() {
        return (T[]) elementos;
    }
}