class MyCircularQueue {
    private int[] queue;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public MyCircularQueue(int k) {
        capacity = k;
        queue = new int[capacity];

        front = 0;
        rear = 0;
        size = 0;
    }

    public boolean enQueue(int value) {
        // Queue is full, so no space is available for a new element.
        if (isFull()) {
            return false;
        }

        // Insert the element at the current rear position.
        queue[rear] = value;

        // Move rear to the next position.
        // Modulo makes it wrap back to 0 when it reaches the end.
        rear = (rear + 1) % capacity;

        size++;
        return true;
    }

    public boolean deQueue() {
        // Nothing to remove if the queue is empty.
        if (isEmpty()) {
            return false;
        }

        // Move front to the next element.
        // Modulo handles wrapping around the array.
        front = (front + 1) % capacity;

        size--;
        return true;
    }

    public int Front() {
        if (isEmpty()) {
            return -1;
        }

        return queue[front];
    }

    public int Rear() {
        if (isEmpty()) {
            return -1;
        }

        // rear always points to the NEXT insertion position.
        // Therefore, the last element is one position behind rear.
        return queue[(rear - 1 + capacity) % capacity];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }
}


/**
 * Your MyCircularQueue object will be instantiated and called as such:
 * MyCircularQueue obj = new MyCircularQueue(k);
 * boolean param_1 = obj.enQueue(value);
 * boolean param_2 = obj.deQueue();
 * int param_3 = obj.Front();
 * int param_4 = obj.Rear();
 * boolean param_5 = obj.isEmpty();
 * boolean param_6 = obj.isFull();
 */