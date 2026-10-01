class ListNode {
    int value;
    ListNode next;

    public ListNode(int value) {
        this.value = value;
    }

    public ListNode(int value, ListNode next) {
        this.value = value;
        this.next = next;
    }
}

class MyCircularQueue {
    private int size;
    private final int capacity;
    private ListNode front;
    private ListNode rear;

    public MyCircularQueue(int k) {
        capacity = k;
        size = 0;

        // Initially, the queue is empty.
        front = null;
        rear = null;
    }

    public boolean enQueue(int value) {
        // A new element cannot be added once the queue reaches capacity.
        if (isFull()) {
            return false;
        }

        ListNode newNode = new ListNode(value);

        if (isEmpty()) {
            // First element becomes both the front and rear.
            front = newNode;
            rear = newNode;
        } else {
            // Add the new node after the current rear.
            rear.next = newNode;
            rear = newNode;
        }

        size++;
        return true;
    }

    public boolean deQueue() {
        // Nothing to remove if the queue is empty.
        if (isEmpty()) {
            return false;
        }

        // Move front to the next node.
        front = front.next;
        size--;

        // If the queue becomes empty, reset rear as well.
        if (size == 0) {
            rear = null;
        }

        return true;
    }

    public int Front() {
        return isEmpty() ? -1 : front.value;
    }

    public int Rear() {
        return isEmpty() ? -1 : rear.value;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
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