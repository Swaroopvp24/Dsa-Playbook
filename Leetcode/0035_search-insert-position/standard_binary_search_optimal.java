class Solution {
    public int searchInsert(int[] nums, int target) {
        /*
         * After binary search ends, left points to the first position
         * where target can be inserted while keeping the array sorted.
         *
         * - If target exists, we return its index immediately.
         * - If target does not exist, left moves past all elements
         *   smaller than target and stops at the correct insertion index.
         */
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;

            if (nums[middle] == target) {
                return middle;
            }

            if (nums[middle] > target) {
                right = middle - 1;
            } else {
                left = middle + 1;
            }
        }

        return left;
    }
}