class Solution {
    public int findMin(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int middle = left + (right - left) / 2;

            if (nums[middle] > nums[right]) {
                // Minimum(Pivot) is to the right of middle
                left = middle + 1;
            } else {
                // Minimum(Pivot) is at middle or to its left
                right = middle;
            }
        }

        return nums[left];
    }
}
