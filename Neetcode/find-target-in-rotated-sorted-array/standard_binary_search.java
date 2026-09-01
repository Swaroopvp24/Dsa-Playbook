class Solution {
    public int search(int[] nums, int target) {
        int pivotIndex = findPivot(nums);

        // Array is not rotated
        if (pivotIndex == 0) {
            return binarySearch(nums, target, 0, nums.length - 1);
        }

        // Target lies in the left sorted portion
        if (target >= nums[0]) {
            return binarySearch(nums, target, 0, pivotIndex - 1);
        }

        // Target lies in the right sorted portion
        return binarySearch(nums, target, pivotIndex, nums.length - 1);
    }

    // Finds the index of the smallest element
    private int findPivot(int[] nums) {
        int left = 0;
        int right = nums.length - 1;

        while (left < right) {
            int middle = left + (right - left) / 2;

            if (nums[middle] > nums[right]) {
                // Pivot is to the right of middle
                left = middle + 1;
            } else {
                // Pivot is at middle or to its left
                right = middle;
            }
        }

        return left;
    }

    // Standard binary search on a sorted portion
    private int binarySearch(int[] nums, int target, int left, int right) {
        while (left <= right) {
            int middle = left + (right - left) / 2;

            if (nums[middle] == target) {
                return middle;
            }

            if (nums[middle] < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return -1;
    }
}
