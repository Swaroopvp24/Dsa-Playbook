public class MyQueue {
    private Stack<Integer> inputStack;
    private Stack<Integer> helperStack;

    public MyQueue() {
        inputStack = new Stack<>();
        helperStack = new Stack<>();
    }

    public void push(int value) {
        inputStack.push(value);
    }

    public int pop() {
        // Move elements to helper stack until the front is reached.
        while (inputStack.size() > 1) {
            helperStack.push(inputStack.pop());
        }

        int frontValue = inputStack.pop();

        // Restore the original order.
        while (!helperStack.isEmpty()) {
            inputStack.push(helperStack.pop());
        }

        return frontValue;
    }

    public int peek() {
        // Move elements to helper stack until the front is reached.
        while (inputStack.size() > 1) {
            helperStack.push(inputStack.pop());
        }

        int frontValue = inputStack.peek();

        // Restore the original order.
        while (!helperStack.isEmpty()) {
            inputStack.push(helperStack.pop());
        }

        return frontValue;
    }

    public boolean empty() {
        return inputStack.isEmpty();
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