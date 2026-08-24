public class MyStack {
    private Queue<Integer> stackQueue;

    public MyStack() {
        stackQueue = new LinkedList<>();
    }

    public void push(int value) {
        stackQueue.offer(value);

        // Rotate the queue so the new element comes to the front.
        for (int i = stackQueue.size() - 1; i > 0; i--) {
            stackQueue.offer(stackQueue.poll());
        }
    }

    public int pop() {
        return stackQueue.poll();
    }

    public int top() {
        return stackQueue.peek();
    }

    public boolean empty() {
        return stackQueue.isEmpty();
    }
}
