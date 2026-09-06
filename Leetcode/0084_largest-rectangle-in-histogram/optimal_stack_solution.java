class Solution {
    public int largestRectangleArea(int[] heights) {
        /*
         * This is the same monotonic-stack idea as the previous solution,
         * but instead of storing (startIndex, height) pairs, we only store
         * indices.
         *
         * The stack itself helps us find the left boundary:
         * - Current index i is the right boundary.
         * - After popping an index, the new stack top is the nearest smaller
         *   bar on the left.
         *
         * So for a popped bar:
         *
         *     width = rightBoundary - leftBoundary - 1
         *
         * We use an extra iteration (i == n) with a virtual height of 0
         * to force all remaining bars in the stack to be processed.
         */

        int n = heights.length;
        int maxArea = 0;

        // Monotonic increasing stack: stores indices of increasing heights.
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i <= n; i++) {

            /*
             * Current height is smaller than the height at the top of the
             * stack, so the taller bar can no longer extend to the right.
             *
             * i acts as the right boundary for the popped bar.
             */
            while (!stack.isEmpty() &&
                   (i == n || heights[stack.peek()] >= heights[i])) {

                int height = heights[stack.pop()];

                /*
                 * After popping:
                 * - i is the first smaller bar on the right.
                 * - stack.peek() is the first smaller bar on the left.
                 *
                 * If the stack is empty, the rectangle extends all the
                 * way from index 0 to i - 1.
                 */
                int width = stack.isEmpty()
                    ? i                         // extends from index 0 to i - 1
                    : i - stack.peek() - 1;    // extends from stack.peek() + 1 to i - 1

                int currentArea = height * width;
                maxArea = Math.max(maxArea, currentArea);
            }

            // Don't push the virtual index n into the stack.
            if (i < n) {
                stack.push(i);
            }
        }

        return maxArea;
    }
}
