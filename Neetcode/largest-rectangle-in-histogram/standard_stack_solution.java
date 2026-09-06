class Solution {
    public int largestRectangleArea(int[] heights) {
        int n = heights.length;

        /*
         * For every bar:
         * - leftBoundary[i]  = index of the first smaller bar on the left
         * - rightBoundary[i] = index of the first smaller bar on the right
         *
         * Once we know these boundaries, the maximum width for heights[i] is:
         *
         *     rightBoundary[i] - leftBoundary[i] - 1
         *
         * We use a monotonic increasing stack to find both boundaries in O(n).
         */
        int[] leftBoundary = new int[n];
        int[] rightBoundary = new int[n];

        // If no smaller bar exists, use the array boundaries.
        Arrays.fill(leftBoundary, -1);
        Arrays.fill(rightBoundary, n);

        Deque<Integer> stack = new ArrayDeque<>();

        // Find the nearest smaller bar on the left for every bar.
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }

            if (!stack.isEmpty()) {
                leftBoundary[i] = stack.peek();
            }

            stack.push(i);
        }

        stack.clear();

        // Find the nearest smaller bar on the right for every bar.
        for (int i = n - 1; i >= 0; i--) {
            while (!stack.isEmpty() && heights[stack.peek()] >= heights[i]) {
                stack.pop();
            }

            if (!stack.isEmpty()) {
                rightBoundary[i] = stack.peek();
            }

            stack.push(i);
        }

        // Calculate the largest rectangle using each bar as the minimum height.
        int maxArea = 0;

        for (int i = 0; i < n; i++) {
            int width = rightBoundary[i] - leftBoundary[i] - 1;
            int currentArea = heights[i] * width;

            maxArea = Math.max(maxArea, currentArea);
        }

        return maxArea;
    }
}
