class StockSpanner {
    private Deque<int[]> stack;

    public StockSpanner() {
        stack = new ArrayDeque<>();
    }
    // The key idea is that each stack entry stores [price, span], allowing multiple previous days to be skipped in one pop.
    public int next(int price) {
        int span = 1;

        // Merge consecutive previous days with a lower or equal price.
        while (!stack.isEmpty() && price >= stack.peek()[0]) {
            int[] previousDay = stack.pop();
            span += previousDay[1];
        }

        // Store: [price, span]
        stack.push(new int[] {price, span});

        return span;
    }
}

/**
 * Your StockSpanner object will be instantiated and called as such:
 * StockSpanner obj = new StockSpanner();
 * int param_1 = obj.next(price);
 */
