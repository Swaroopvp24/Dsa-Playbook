public class MyStack {
    private Queue<Integer> primaryQueue;
    private Queue<Integer> helperQueue;

    public MyStack() {
        primaryQueue = new LinkedList<>();
        helperQueue = new LinkedList<>();
    }

    public void push(int value) {
        helperQueue.offer(value);

        // Move existing elements behind the new element.
        while (!primaryQueue.isEmpty()) {
            helperQueue.offer(primaryQueue.poll());
        }

        // Swap the queues.
        Queue<Integer> temp = primaryQueue;
        primaryQueue = helperQueue;
        helperQueue = temp;
    }

    public int pop() {
        return primaryQueue.poll();
    }

    public int top() {
        return primaryQueue.peek();
    }

    public boolean empty() {
        return primaryQueue.isEmpty();
    }
}
