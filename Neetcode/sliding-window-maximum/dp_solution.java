class Solution {
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;

        // lMax[i] = maximum value from the start of the current k-sized block
        //           up to index i.
        int[] lMax = new int[n];

        // rMax[i] = maximum value from index i to the end of the current k-sized block.
        int[] rMax = new int[n];

        int[] result = new int[n - k + 1];

        /*
         * Build lMax from left to right.
         *
         * Every k elements form an independent block.
         * At the beginning of a new block, we must reset the maximum.
         *
         * Example for k = 3:
         *
         * [1, 3, 2] [5, 4, 6] ...
         *  lMax -> [1, 3, 3] [5, 5, 6] ...
         */
        lMax[0] = nums[0];

        for (int i = 1; i < n; i++) {
            // Start of a new k-sized block, so reset the maximum.
            if (i % k == 0) {
                lMax[i] = nums[i];
            } else {
                lMax[i] = Math.max(nums[i], lMax[i - 1]);
            }
        }

        /*
         * Build rMax from right to left.
         *
         * rMax stores the maximum from the current index
         * to the end of its k-sized block.
         *
         * We reset at the last index of each block.
         */
        rMax[n - 1] = nums[n - 1];

        for (int i = n - 2; i >= 0; i--) {
            // End of a k-sized block, so reset the maximum.
            if (i % k == k - 1) {
                rMax[i] = nums[i];
            } else {
                rMax[i] = Math.max(nums[i], rMax[i + 1]);
            }
        }

        /*
         * For every sliding window [i ... i + k - 1],
         * the window can cross a block boundary.
         *
         * Therefore, split the window into two parts:
         *
         *   left part  -> rMax[i]
         *   right part -> lMax[i + k - 1]
         *
         * The maximum of the entire window is simply the maximum
         * of these two values.
         */
        for (int i = 0; i <= n - k; i++) {
            result[i] = Math.max(rMax[i], lMax[i + k - 1]);
        }

        // System.out.println(Arrays.toString(nums));
        // System.out.println(Arrays.toString(lMax));
        // System.out.println(Arrays.toString(rMax));

        return result;
    }
}