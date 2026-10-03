class Solution {
    public int maxAscendingSum(int[] nums) {
        int currentSum = nums[0];
        int maximumSum = nums[0];

        for (int i = 1; i < nums.length; i++) {
            // Continue the current ascending subarray
            if (nums[i - 1] < nums[i]) {
                currentSum += nums[i];
            } else {
                // Start a new ascending subarray
                currentSum = nums[i];
            }

            // Keep track of the maximum ascending sum found so far
            maximumSum = Math.max(maximumSum, currentSum);
        }

        return maximumSum;
    }
}
