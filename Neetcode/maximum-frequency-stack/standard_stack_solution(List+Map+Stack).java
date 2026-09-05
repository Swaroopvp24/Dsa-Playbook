class FreqStack {

    // For each frequency, store the values that currently have that frequency.
    // The stack ensures that among values with the same frequency,
    // the most recently pushed value is popped first.
    List<Deque<Integer>> valuesByFrequency;

    // Stores the current frequency of each value.
    Map<Integer, Integer> frequencyMap;

    // Highest frequency currently present in the stack.
    int maxFrequency;

    public FreqStack() {
        valuesByFrequency = new ArrayList<>();
        frequencyMap = new HashMap<>();
        maxFrequency = 0;
    }

    public void push(int value) {

        // Increase the frequency of this value.
        int newFrequency = frequencyMap.getOrDefault(value, 0) + 1;
        frequencyMap.put(value, newFrequency);

        // Make sure we have a stack available for this frequency.
        // Index 0 is unused, so the frequency itself can be used as the index.
        while (valuesByFrequency.size() <= newFrequency) {
            valuesByFrequency.add(new ArrayDeque<>());
        }

        // Add the value to the stack corresponding to its new frequency.
        valuesByFrequency.get(newFrequency).push(value);

        // Update the highest frequency seen so far.
        maxFrequency = Math.max(maxFrequency, newFrequency);
    }

    public int pop() {

        // Pop the most recently added value among those
        // having the highest frequency.
        int value = valuesByFrequency.get(maxFrequency).pop();

        // Decrease the frequency of the popped value.
        frequencyMap.put(value, frequencyMap.get(value) - 1);

        // If no values remain at the current maximum frequency,
        // move down to the next lower frequency.
        if (valuesByFrequency.get(maxFrequency).isEmpty()) {
            maxFrequency--;
        }

        return value;
    }
}


/**
 * Your FreqStack object will be instantiated and called as such:
 * FreqStack obj = new FreqStack();
 * obj.push(val);
 * int param_2 = obj.pop();
 */