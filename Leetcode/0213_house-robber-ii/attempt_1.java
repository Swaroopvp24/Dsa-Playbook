class Solution {

    public int solve(int[] nums, int st, int en) {
        if (st == en)
            return nums[st];

        int prev2 = nums[st];
        int prev1 = Math.max(nums[st], nums[st + 1]);

        for (int i = st + 2; i <= en; i++) {

            int cur = Math.max(prev2 + nums[i], prev1);

            prev2 = prev1;
            prev1 = cur;
        }

        return prev1;
    }

    public int rob(int[] nums) {

        if (nums.length == 0)
            return 0;

        if (nums.length == 1)
            return nums[0];

        return Math.max(
                solve(nums, 0, nums.length - 2),
                solve(nums, 1, nums.length - 1));
    }
}