public class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int n = nums.length;
        int[] prefixSum = new int[n + 1];

        // Build prefix sum array
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        int minLen = n + 1;

        for (int i = 0; i < n; i++) {
            int left = i, right = n;

            // Binary search for the smallest valid ending index
            while (left < right) {
                int mid = (left + right) / 2;
                int currentSum = prefixSum[mid + 1] - prefixSum[i];
                // If current sum is enough, try to find a smaller valid subarray
                // by moving right boundary towards mid
                if (currentSum >= target) {
                    right = mid;
                } else {
                    // Sum is too small, so we need to move further right
                    left = mid + 1;
                }
            }

            // Update minimum length if a valid subarray exists
            if (left != n) {
                // Calculate the length of the current valid subarray
                // left = ending index, i = starting index
                // +1 because both indices are inclusive
                minLen = Math.min(minLen, left - i + 1);
            }
        }

        // Return 0 if no valid subarray was found
        return minLen == (n + 1) ? 0 : minLen;
    }
}