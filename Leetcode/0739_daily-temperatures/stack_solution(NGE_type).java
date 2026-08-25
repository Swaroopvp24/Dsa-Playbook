class Solution {

    public int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        Deque<Integer> stack = new ArrayDeque<>();

        // Store indices, starting from the last day.
        stack.push(temperatures.length - 1);

        for (int day = temperatures.length - 2; day >= 0; day--) {

            // Remove days that are not warmer than the current day.
            while (!stack.isEmpty()
                    && temperatures[stack.peek()] <= temperatures[day]) {
                stack.pop();
            }

            // The top of the stack is the next warmer day.
            if (!stack.isEmpty()) {
                result[day] = stack.peek() - day;
            }

            stack.push(day);
        }

        return result;
    }
}
