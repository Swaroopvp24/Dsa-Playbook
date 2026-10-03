class Solution {
    public boolean isMonotonic(int[] nums) {
        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < nums.length; i++) {
            // If the current element is smaller, the array
            // cannot be non-decreasing.
            if (nums[i] < nums[i - 1]) {
                isIncreasing = false;
            }

            // If the current element is larger, the array
            // cannot be non-increasing.
            if (nums[i] > nums[i - 1]) {
                isDecreasing = false;
            }
        }

        // The array is monotonic if it is either
        // non-decreasing or non-increasing.
        return isIncreasing || isDecreasing;
    }
}
