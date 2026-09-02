class Solution {
    public boolean search(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;

            // If the middle element is the target, we found it.
            if (nums[middle] == target) {
                return true;
            }

            /*
             * When nums[left] == nums[middle], we cannot determine
             * which half is sorted.
             *
             * Example: [1, 0, 1, 1, 1]
             *          ^     ^
             *        left  middle
             *
             * Both values are 1, so the sorted-half logic is ambiguous.
             * Therefore, safely skip the duplicate at 'left'.
             */
            if (nums[left] == nums[middle]) {
                left++;
            }

            /*
             * CASE 1: Left half is sorted.
             *
             * nums[left] <= nums[middle] means:
             *
             * left ---------------- middle
             *   [ sorted portion ]
             *
             * Now check whether the target lies inside this sorted range.
             */
            else if (nums[left] <= nums[middle]) {

                /*
                 * Target is inside the sorted left half:
                 *
                 * nums[left] <= target < nums[middle]
                 *
                 * So we can discard the right half.
                 */
                if (nums[left] <= target && target < nums[middle]) {
                    right = middle - 1;
                } else {
                    /*
                     * Target cannot be in the left half,
                     * so search the right half.
                     */
                    left = middle + 1;
                }
            }

            /*
             * CASE 2: Left half is NOT sorted.
             *
             * Since the array is a rotated sorted array,
             * if the left half is not sorted, the right half must be sorted.
             *
             * Example:
             * [4, 5, 6, 7, 0, 1, 2]
             *  left     middle      right
             *
             * The right side [0, 1, 2] is sorted.
             */
            else {

                /*
                 * Check whether the target lies inside
                 * the sorted right half:
                 *
                 * nums[middle] < target <= nums[right]
                 *
                 * If yes, discard the left half.
                 */
                if (nums[middle] < target && target <= nums[right]) {
                    left = middle + 1;
                } else {
                    /*
                     * Target cannot be in the sorted right half,
                     * so search the left half.
                     */
                    right = middle - 1;
                }
            }
        }

        // We exhausted the search space without finding the target.
        return false;
    }
}
