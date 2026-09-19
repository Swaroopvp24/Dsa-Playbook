class Solution {

    // Returns the minimum number of subarrays needed
    // when each subarray can have a maximum sum of maxAllowedSum.
    public int countSubarrays(int[] nums, int maxAllowedSum) {
        int subarrayCount = 1;
        int currentSum = 0;

        for (int num : nums) {
            // Add the current element if it does not exceed the limit.
            if (currentSum + num <= maxAllowedSum) {
                currentSum += num;
            } else {
                // Otherwise, start a new subarray.
                currentSum = num;
                subarrayCount++;
            }
        }

        return subarrayCount;
    }

    public int splitArray(int[] nums, int k) {
        if (k > nums.length) {
            return -1;
        }

        int largestElement = nums[0];
        int totalSum = 0;

        // The answer must be at least the largest element
        // and at most the sum of all elements.
        for (int num : nums) {
            largestElement = Math.max(largestElement, num);
            totalSum += num;
        }

        int left = largestElement;
        int right = totalSum;

        // Binary search for the minimum possible maximum subarray sum.
        while (left <= right) {
            int mid = left + (right - left) / 2;

            int requiredSubarrays = countSubarrays(nums, mid);

            if (requiredSubarrays > k) {
                // The limit is too small, so we need a larger value.
                left = mid + 1;
            } else {
                // This limit works. Try to find a smaller one.
                right = mid - 1;
            }
        }

        return left;
    }
}
