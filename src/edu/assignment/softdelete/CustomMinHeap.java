package edu.assignment.softdelete;

import edu.assignment.dataStructure.DeliveryJob;
import edu.assignment.dataStructure.Heap;

public class CustomMinHeap<E extends Comparable<E>> extends Heap<E>{
    private DeliveryJob[] heap;
    private int size;
    private double currentX, currentY;

    public CustomMinHeap(int capacity, double cx, double cy) {
        this.heap = new DeliveryJob[capacity];
        this.size = 0;
        this.currentX = cx;
        this.currentY = cy;
    }

    // Helper to calculate distance from the CURRENT van location
    private double getDist(DeliveryJob job) {
        return Math.sqrt(Math.pow(job.getLat() - currentX, 2) + Math.pow(job.getLon() - currentY, 2));
    }

    public void insert(DeliveryJob job) {
        heap[size] = job;
        bubbleUp(size);
        size++;
    }

    public DeliveryJob extractMin() {
        if (size == 0) return null;
        DeliveryJob min = heap[0];
        heap[0] = heap[size - 1];
        size--;
        bubbleDown(0);
        return min;
    }

    private void bubbleUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (getDist(heap[index]) >= getDist(heap[parent])) break;
            swap(index, parent);
            index = parent;
        }
    }

    private void bubbleDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int smallest = index;

            if (left < size && getDist(heap[left]) < getDist(heap[smallest])) smallest = left;
            if (right < size && getDist(heap[right]) < getDist(heap[smallest])) smallest = right;
            
            if (smallest == index) break;
            swap(index, smallest);
            index = smallest;
        }
    }

    private void swap(int i, int j) {
        DeliveryJob temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public boolean isEmpty() { return size == 0; }
}