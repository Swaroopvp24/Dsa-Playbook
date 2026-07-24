import java.util.Arrays;

class Solution {

    int[] dp;

    int solve(int i, int[] nums) {
        if (i < 0)
            return 0;

        if (i == 0)
            return nums[0];

        if (dp[i] != -1)
            return dp[i];

        int take = nums[i] + solve(i - 2, nums);
        int notTake = solve(i - 1, nums);

        return dp[i] = Math.max(take, notTake);
    }

    public int rob(int[] nums) {
        dp = new int[nums.length];
        Arrays.fill(dp, -1);

        return solve(nums.length - 1, nums);
    }
}