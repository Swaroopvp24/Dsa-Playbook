//This is a greedy one-pass solution that maintains two states and it can also be viewed as space-optimized DP
class Solution {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int rightMax = 0;
        for (int i = prices.length - 1; i >= 0; i--) {
            rightMax = Math.max(rightMax, prices[i]);
            maxProfit = Math.max(maxProfit, rightMax - prices[i]);
        }
        return maxProfit;
    }
}
/*
ALternatively
        int minBuyPrice = prices[0];
        int maxProfit = 0;

        for (int currentPrice : prices) {

            // Update the minimum price at which we can buy
            minBuyPrice = Math.min(minBuyPrice, currentPrice);

            // Calculate profit if we sell at the current price
            int currentProfit = currentPrice - minBuyPrice;

            // Keep track of the maximum profit found so far
            maxProfit = Math.max(maxProfit, currentProfit);
        }

        return maxProfit;
*/