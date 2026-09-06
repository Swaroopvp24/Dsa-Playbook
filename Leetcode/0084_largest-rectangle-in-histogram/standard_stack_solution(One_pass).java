class Solution {
    public int largestRectangleArea(int[] heights) {
        /*
         * The stack stores pairs: (startIndex, height).
         *
         * A pair (i, h) means:
         * - height h can form a rectangle starting from index i.
         * - h can continue to extend to the right until we encounter
         *   a bar shorter than h.
         *
         * When we encounter a shorter bar, we pop taller bars and
         * calculate the maximum rectangle they can form.
         */
        Deque<int[]> stack = new ArrayDeque<>(); // pair: (index, height)
                                                // Height h can form a
                                                // rectangle starting from index i

        int maxArea = 0;

        for (int i = 0; i < heights.length; i++) {
            int startIndex = i;

            // Current height is smaller than the height at the top of the stack.
            while (!stack.isEmpty() && stack.peek()[1] > heights[i]) {
                // Pop the taller bar and calculate the rectangle it can form.
                int[] current = stack.pop();

                int height = current[1];
                int start = current[0];

                maxArea = Math.max(maxArea, height * (i - start));

                /*
                 * The current shorter height can "take over" the popped bar's
                 * starting position because it can also form a rectangle
                 * across the entire range where the taller bar existed.
                 */
                startIndex = start;
            }

            stack.push(new int[] {startIndex, heights[i]});
        }

        /*
         * Every bar still in the stack can extend all the way to the end
         * of the histogram, so calculate the maximum rectangle for each.
         */
        for (int[] pair : stack) {
            int startIndex = pair[0];
            int height = pair[1];

            maxArea = Math.max(
                maxArea,
                height * (heights.length - startIndex)
            );
        }

        return maxArea;
    }
}
