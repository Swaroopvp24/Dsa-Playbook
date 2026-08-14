# best-time-to-buy-and-sell-stock-ii

## attempt_1_standarddp.java
*Style: detailed*

# Technical Deep-Dive: Dynamic Programming Approach to Best Time to Buy and Sell Stock II

## Summary
The provided solution addresses the "Best Time to Buy and Sell Stock II" problem, where an investor may engage in multiple transactions. The core algorithmic technique is **Bottom-Up Dynamic Programming (Tabulation)**. 

The algorithm models the decision state at each day $i$ using a two-dimensional state space: `(day, canBuy)`. It systematically computes the maximum possible profit by evaluating the optimal choice at every step: either execute a transaction (buy/sell) or skip (hold/wait), effectively decomposing the problem into overlapping subproblems that satisfy the principle of optimality.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Reasoning:** The algorithm uses a nested loop structure. The outer loop iterates through the price array of size $N$ (from $n-1$ to $0$), and the inner loop runs a constant $k=2$ times (representing the `canBuy` states). Since the number of states is $2N$ and each state transition takes $O(1)$ time, the total complexity is linear with respect to the input size.

### Space Complexity: $O(N)$
*   **Reasoning:** The solution allocates a 2D array `dp[n + 1][2]`. While this is currently $O(N)$, it is important to note that the state transition only depends on `dp[ind + 1]`. Therefore, this can be optimized to $O(1)$ space by using two variables to track the previous day's states (`nextBuy`, `nextSell`).

---

## Component Deep Dive

### 1. State Definition
*   `dp[ind][canBuy]`: The maximum profit achievable starting from index `ind`, given the boolean state `canBuy` (where `1` means we are looking to buy, and `0` means we are holding stock and looking to sell).

### 2. State Transition Logic
*   **If `canBuy == 1` (Buying state):**
    *   **Option 1 (Buy):** `-prices[ind] + dp[ind + 1][0]` (Cost subtracted, move to state 0).
    *   **Option 2 (Skip):** `0 + dp[ind + 1][1]` (No cost, remain in state 1).
*   **If `canBuy == 0` (Selling state):**
    *   **Option 1 (Sell):** `prices[ind] + dp[ind + 1][1]` (Revenue added, move to state 1).
    *   **Option 2 (Skip):** `0 + dp[ind + 1][0]` (No revenue, remain in state 0).

### 3. Edge-Case Handling
*   **Empty Array:** If `prices.length == 0`, `n` is 0. The array is initialized as `dp[1][2]`. The loop condition `ind >= 0` fails immediately. The function returns `dp[0][1]`, which is initialized to `0`. This is correct.
*   **Single-Element Array:** The loop runs once. It evaluates the possibility of buying or not buying. If prices are given as `[5]`, the algorithm correctly determines that buying and selling are not possible, resulting in `0`.

---

## Key Insights

### 1. The "Greedy" Equivalence
While this code uses DP, it is mathematically equivalent to a **Greedy Algorithm**. Because we can make infinite transactions, the maximum profit is simply the sum of all positive price increases between consecutive days: $\sum_{i=1}^{n-1} \max(0, prices[i] - prices[i-1])$. The DP approach provides a formal framework that can be extended to harder variations (like adding transaction fees or cooldowns) where the greedy approach fails.

### 2. Space Optimization Opportunity
The current `dp[n + 1][2]` structure is memory-heavy for large inputs. In a production environment, you should refactor this to use two variables, `prevBuy` and `prevSell`:
```java
int nextBuy = 0, nextSell = 0;
for (int i = n - 1; i >= 0; i--) {
    int curBuy = Math.max(-prices[i] + nextSell, nextBuy);
    int curSell = Math.max(prices[i] + nextBuy, nextSell);
    nextBuy = curBuy;
    nextSell = curSell;
}
```
This reduces the auxiliary space from $O(N)$ to $O(1)$ without altering the time complexity.

### 3. Subtle Logic Trap
Note the initialization `dp[n][0] = 0` and `dp[n][1] = 0`. This correctly represents the **Base Case**: if there are no more days to trade, the future profit from that point forward is zero. If this were a variation like "Total transactions capped at K," initializing these to a very small negative number (representing impossibility) would be critical to prevent incorrect logic.

---
