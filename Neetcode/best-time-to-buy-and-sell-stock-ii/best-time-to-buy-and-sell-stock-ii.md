# best-time-to-buy-and-sell-stock-ii

## standard_dp_solution.java
*Style: detailed*

# Technical Deep-Dive: Dynamic Programming Approach to Best Time to Buy and Sell Stock II

## Summary
The solution implements a classic **bottom-up Dynamic Programming (DP)** approach to solve the infinite-transaction stock trading problem. By defining the state as `dp[index][canBuy]`, we decompose the problem into a series of optimal sub-structures. The algorithm transitions through the array from the final day backward, deciding at each day whether to buy, sell, or hold, capturing the optimal profit potential for every state.

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The solution employs a nested loop structure. The outer loop iterates through the prices array exactly once ($n$ iterations), and the inner loop runs for a constant number of states (2: `canBuy` = 0 or 1). 
*   **Result:** $O(n \times 2)$, which simplifies to **$O(n)$** in asymptotic notation.

### Space Complexity: $O(n)$
*   **Derivation:** A 2D array `dp[n+1][2]` is allocated to store the state of the market at each day.
*   **Optimization Note:** While the current implementation uses $O(n)$ space, the state at `ind` only depends on the state at `ind + 1`. This could be space-optimized to $O(1)$ by using two variables (`prevBuy`, `prevSell`) to represent the next state, effectively eliminating the need for the full DP table.

---

## Component Deep Dive

### State Definition
*   `dp[ind][1]`: Represents the maximum profit achievable starting from day `ind`, given that the agent is allowed to **buy** a stock.
*   `dp[ind][0]`: Represents the maximum profit achievable starting from day `ind`, given that the agent is currently holding a stock and must **sell** (or skip).

### The State Transition Logic
1.  **When `canBuy == 1` (Buying or Waiting):**
    *   **Buy:** `-prices[ind] + dp[ind+1][0]` (Cost incurred, move to "must sell" state).
    *   **Skip:** `0 + dp[ind+1][1]` (No cost, remain in "can buy" state).
    *   *Decision:* Maximize between buying and skipping.
2.  **When `canBuy == 0` (Selling or Waiting):**
    *   **Sell:** `prices[ind] + dp[ind+1][1]` (Revenue gained, move to "can buy" state).
    *   **Skip:** `0 + dp[ind+1][0]` (No revenue, remain in "must sell" state).
    *   *Decision:* Maximize between selling and skipping.

### Base Case
*   `dp[n][0] = 0` and `dp[n][1] = 0`: At the end of the array (day `n`), no more profits can be generated, effectively terminating the recursion/iteration.

---

## Key Insights

### 1. Greedy vs. DP Equivalence
While this implementation uses DP, the problem "Best Time to Buy and Sell Stock II" (infinite transactions) is mathematically equivalent to a **Greedy strategy**. Summing up all positive differences between consecutive days (`prices[i] - prices[i-1]`) yields the same result. The DP approach provided is more robust as it can be easily extended to variations with transaction fees or cool-down periods where greedy strategies fail.

### 2. Edge Case Handling
*   **Empty/Single Element Array:** If `prices.length` is 0 or 1, the loops correctly handle the bounds. The `n=0` case results in `dp[0][1]` returning 0, which is the correct profit for zero or one-day trades.
*   **Monotonically Decreasing Prices:** The `Math.max` logic ensures that if buying leads to a loss, the `0 + dp[ind+1][1]` branch is chosen, effectively resulting in a total profit of 0.

### 3. Potential Pitfalls: Array Indexing
*   The code uses `dp[n+1][2]`. A common bug in similar implementations is an `ArrayIndexOutOfBoundsException` when accessing `dp[ind + 1]`. By initializing the table to `n+1`, the code safely accesses the base case at index `n` without needing explicit conditional checks inside the loop, resulting in cleaner, branch-prediction-friendly code.

### 4. Performance Nuance
The inner loop `canBuy < 2` is small enough that the JVM likely unrolls these operations, making it extremely efficient. However, if this were to be ported to a system with constrained memory, the `dp[n+1][2]` allocation is the primary bottleneck. Reducing this to two local variables is the recommended path for production-level optimization.

---

## same sol.java
*Style: concise*

### Stock Profit Calculation (Dynamic Programming)

**Summary**
This code calculates the maximum profit from an array of stock prices (allowing multiple transactions) using bottom-up dynamic programming. It models the problem as a state machine where you decide whether to buy, sell, or hold at each index.

**Key Components**
*   `dp[n+1][2]`: A memoization table where the first dimension is the current day (`ind`) and the second is the state `canBuy` (1 = can buy, 0 = must sell).
*   **State Transitions:**
    *   If `canBuy == 1`: Choose between buying (`-prices[ind] + dp[ind+1][0]`) or skipping (`0 + dp[ind+1][1]`).
    *   If `canBuy == 0`: Choose between selling (`prices[ind] + dp[ind+1][1]`) or skipping (`0 + dp[ind+1][0]`).

**Logic Notes**
*   **Base Case:** `dp[n][...]` is initialized to 0, representing zero profit once no more days remain.
*   **State Definition:** `canBuy = 1` means you currently do not hold stock and are eligible to purchase; `canBuy = 0` means you hold stock and are looking to sell.
*   **Return Value:** The code returns `dp[0][1]` because the initial state at day 0 is having no stock, thus being in the "can buy" state. 
*   **Optimization Tip:** This approach can be space-optimized to $O(1)$ by only keeping track of the `previous` day's values, as each state only depends on `ind + 1`.

---

## dp_space_optimized.java
*Style: detailed*

# Technical Deep-Dive: Optimized Dynamic Programming for Stock Trading

## Summary
The provided implementation solves the "Best Time to Buy and Sell Stock II" problem using an **iterative bottom-up Dynamic Programming (DP) approach with space optimization**. 

The algorithm treats the trading process as a state machine where at each day $i$, the agent is in one of two states: `canBuy = 1` (ready to purchase) or `canBuy = 0` (holding a stock). The transition between these states is defined by the decision to buy, sell, or hold, with the objective of maximizing cumulative profit. By observing that the state at day $i$ only depends on the results from day $i+1$, the implementation reduces space complexity from $O(N)$ to $O(1)$.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The solution performs a single pass over the `prices` array of length $N$. Inside the loop, it performs a constant number of operations (comparisons and arithmetic) regardless of the size of $N$.
*   **Bottleneck:** The linear scan is mandatory as each day's price is independent and must be evaluated.

### Space Complexity: $O(1)$
*   **Derivation:** Standard DP for this problem typically utilizes an $N \times 2$ table. This implementation optimizes this to four scalar variables (`next0`, `next1`, and temporary state updates). No matter the input size, the heap/stack footprint remains constant.

---

## Component Deep Dive

### State Transition Logic
The core logic relies on the recurrence relations:
1.  **If `canBuy == 1` (Buying state):**
    *   *Buy:* $-prices[ind] + \text{next\_state\_0}$
    *   *Skip:* $0 + \text{next\_state\_1}$
2.  **If `canBuy == 0` (Selling state):**
    *   *Sell:* $prices[ind] + \text{next\_state\_1}$
    *   *Skip:* $0 + \text{next\_state\_0}$

### The Space Optimization Trap
The code uses `next0` and `next1` to represent the DP table state for the *next* day. 
*   **Subtle implementation detail:** Note that the code updates `next1` and `next0` *inside* the inner loop. 
*   **Risk:** In some DP optimizations, updating a variable while still needing its previous value for subsequent calculations in the same iteration can lead to logical errors. Here, the inner loop order (`canBuy = 0` then `1`) and the logic ensure that when `canBuy` is 1, it uses `next0` (which is technically the *current* iteration's `next0` update if the order were swapped, but here it is handled carefully). 

### Edge-Case Handling
*   **Empty array ($N=0$):** The loop condition `ind >= 0` fails immediately, returns `next1` (0), which is correct.
*   **Single price:** Returns 0 profit correctly, as no transaction can be completed.
*   **Monotonically decreasing prices:** The `Math.max` logic correctly chooses "skip" at every step, yielding 0 profit.

---

## Key Insights

### 1. State Definition Logic
The return value `next1` is effectively the profit state on day 0, given you have the capacity to buy (`canBuy = 1`). This is the mathematical equivalent of solving the state machine forward.

### 2. The "Greedy" Equivalence
While this is implemented as DP, it is worth noting that for this specific problem (unlimited transactions), the problem can also be solved via a **Greedy strategy** (summing all positive differences: `prices[i] - prices[i-1]`). The DP approach is technically more robust, as it can be easily extended to variations with transaction fees or "cooldown" periods, whereas the Greedy approach is highly specific to the "no constraint" version of the problem.

### 3. Potential Bug Warning (Variable Shadowing/Order)
The variables `next0` and `next1` are updated *within* the loop that calculates both states.
*   If the code were: `if (canBuy == 1) { next1 = profit; }`
*   And later used `next0` in the *same* index pass, you are effectively using the "future" state (`i`) for the calculation of the current state. While mathematically valid for this specific recurrence due to the independence of the two states within a single day, this pattern is dangerous. **Best practice:** Always use temporary variables (e.g., `curr0`, `curr1`) to buffer state updates before committing them to the "next" variables to prevent unintentional cross-pollination of states.

---
