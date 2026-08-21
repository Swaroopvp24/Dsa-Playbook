class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        int l = 0, len = Integer.MAX_VALUE, sum = 0;
        for (int r = 0; r < nums.length; r++) {
            sum += nums[r];
            while (sum >= target) {
                len = Math.min(len, r - l + 1);
                sum -= nums[l++];
            }
        }
        return len == Integer.MAX_VALUE ? 0 : len;
    }
}