class MinStack {
    private Deque<Long> stack;
    private long currentMin;

    public MinStack() {
        stack = new ArrayDeque<>();
    }

    public void push(int value) {
        if (stack.isEmpty()) {
            stack.push(0L);
            currentMin = value;
        } else {
            long difference = value - currentMin;
            stack.push(difference);

            // If value is smaller, it becomes the new minimum.
            if (value < currentMin) {
                currentMin = value;
            }
        }
    }

    public void pop() {
        long difference = stack.pop();

        // A negative difference means the popped value was the minimum.
        if (difference < 0) {
            currentMin = currentMin - difference;
        }
    }

    public int top() {
        long difference = stack.peek();

        // Negative value represents the current minimum itself.
        if (difference < 0) {
            return (int) currentMin;
        }

        return (int) (currentMin + difference);
    }

    public int getMin() {
        return (int) currentMin;
    }
}
