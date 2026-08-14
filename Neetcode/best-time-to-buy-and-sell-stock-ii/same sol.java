class Solution {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n + 1][2];
        dp[n][0] = 0;
        dp[n][1] = 0;
        for (int ind = n - 1; ind >= 0; ind--) {
            for (int canBuy = 0; canBuy < 2; canBuy++) {
                int profit = 0;
                if (canBuy==1) {
                    profit = Math.max(-prices[ind] + dp[ind + 1][0], 0 + dp[ind + 1][1]);
                } else {
                    profit = Math.max(prices[ind] + dp[ind + 1][1], 0 + dp[ind + 1][0]);
                }
            dp[ind][canBuy] = profit;
            }
        }
        System.out.println(dp[0][0]);
        System.out.println(dp[0][1]);
        return dp[0][1];//The answer is 7, because you start without a stock, meaning you're in the canBuy = 1 state.

    }
}