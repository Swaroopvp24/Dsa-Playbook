public class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int n = nums.length;
        int[] prefixSum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }

        int res = n + 1;
        for (int i = 0; i < n; i++) {
            int l = i, r = n;
            while (l < r) {
                int mid = (l + r) / 2;
                int curSum = prefixSum[mid + 1] - prefixSum[i];
                if (curSum >= target) {
                    r = mid;
                } else {
                    l = mid + 1;
                }
            }
            if (l != n) {
                res = Math.min(res, l - i + 1);
            }
        }

        // return res % (n + 1);
        return res == (n + 1) ? 0 : res;
    }
}