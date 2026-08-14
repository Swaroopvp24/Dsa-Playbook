class Solution {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int next0 = 0, next1 = 0;
        for (int ind = n - 1; ind >= 0; ind--) {
            for (int canBuy = 0; canBuy < 2; canBuy++) {
                int profit = 0;
                if (canBuy == 1) {
                    profit = Math.max(-prices[ind] + next0, 0 + next1);
                } else {
                    profit = Math.max(prices[ind] + next1, 0 + next0);
                }
                if (canBuy == 1) {
                    next1 = profit;
                } else {
                    next0 = profit;
                }
            }
        }
        return next1; // Because you start without a stock, meaning you're in the canBuy = 1 state.
    }
}