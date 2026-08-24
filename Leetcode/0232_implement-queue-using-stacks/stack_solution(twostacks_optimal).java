class MyQueue {
    private Deque<Integer> inputStack;
    private Deque<Integer> outputStack;

    public MyQueue() {
        inputStack = new ArrayDeque<>();
        outputStack = new ArrayDeque<>();
    }

    public void push(int value) {
        inputStack.push(value);
    }

    public int pop() {
        moveElementsIfNeeded();

        return outputStack.pop();
    }

    public int peek() {
        moveElementsIfNeeded();

        return outputStack.peek();
    }

    public boolean empty() {
        return inputStack.isEmpty() && outputStack.isEmpty();
    }

    private void moveElementsIfNeeded() {
        // Move elements only when the output stack is empty.
        if (outputStack.isEmpty()) {
            while (!inputStack.isEmpty()) {
                outputStack.push(inputStack.pop());
            }
        }
    }
}


/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */