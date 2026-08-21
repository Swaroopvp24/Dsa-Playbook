class Solution {

    public int minSubArrayLen(int target, int[] nums) {

        int left = 0, minLen = Integer.MAX_VALUE, sum = 0;

        for (int right = 0; right < nums.length; right++) {

            // Add current element to the window
            sum += nums[right];

            // Shrink window while sum is enough
            while (sum >= target) {

                // Update minimum window length
                minLen = Math.min(minLen, right - left + 1);

                // Remove left element and move left
                sum -= nums[left++];
            }
        }

        // Return 0 if no valid subarray was found
        return minLen == Integer.MAX_VALUE ? 0 : minLen;
    }
}