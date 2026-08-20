# best-time-to-buy-and-sell-stock

## greedy/dp_solution.java
*Style: detailed*

# Technical Reference: Optimal Single-Pass Stock Profit Calculation

## 1. Summary
The provided solution addresses the "Best Time to Buy and Sell Stock" problem, which seeks the maximum delta between two elements in an array where the subtrahend (buy price) must appear before the minuend (sell price). 

The approach utilizes a **greedy strategy** implemented via a single-pass linear scan. By maintaining a running state of the optimal "anchor" (either the minimum price seen so far or the maximum price seen from the right), the algorithm reduces a quadratic $O(n^2)$ exhaustive search to a linear $O(n)$ temporal complexity. Conceptually, this is a space-optimized Dynamic Programming (DP) pattern where $dp[i]$ represents the local maximum profit at index $i$, and we discard historical states as they are no longer relevant to global maximization.

---

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The algorithm iterates through the `prices` array exactly once. Regardless of the input size $n$, the loop performs a constant number of operations (comparison, subtraction, and assignment) per element.
*   **Operations:** $n$ iterations $\times$ $O(1)$ constant work per iteration.

### Space Complexity: $O(1)$
*   **Derivation:** The solution allocates a fixed number of primitive integer variables (`maxProfit`, `rightMax`/`minBuyPrice`) regardless of input array size. No auxiliary data structures (stacks, queues, or DP tables) are utilized. This is the optimal space footprint for this problem.

---

## 3. Component Deep Dive

### State Maintenance Logic
There are two distinct mental models provided in the source:

1.  **Forward Pass (The `minBuyPrice` approach):**
    *   **Logic:** Tracks the global minimum encountered *up to index $i$*. 
    *   **Functionality:** By subtracting the current `minBuyPrice` from the `currentPrice`, we simulate selling at the current moment given the best historical buy opportunity.
2.  **Reverse Pass (The `rightMax` approach):**
    *   **Logic:** Tracks the global maximum encountered *from index $i$ to $n-1$*.
    *   **Functionality:** This is effectively solving for the "largest gain" by treating the current element as the buy point and the subsequent elements as potential sell points.

### Edge-Case Handling
*   **Empty/Null Arrays:** The provided code assumes a non-null, non-empty array. In a production environment, an input validation check `if (prices == null || prices.length < 2) return 0;` is mandatory to avoid `ArrayIndexOutOfBoundsException` or incorrect return values.
*   **Descending Trends:** If the input is strictly decreasing (e.g., `[5, 4, 3, 2]`), `currentProfit` will always be $\le 0$. The `Math.max(maxProfit, 0)` ensures the function returns `0` rather than a negative value, correctly signifying that no profitable trade exists.
*   **Integer Overflow:** While stock prices typically fit within `int` ranges, if inputs were large, `maxProfit` calculations could theoretically overflow. Using `long` for profit tracking is recommended if price volatility exceeds $2^{31}-1$.

---

## 4. Key Insights

*   **Greedy Correctness:** The greedy property holds because the profit function is monotonically increasing relative to the distance between the local minimum and the future maximum. We do not need to backtrack because a lower `minBuyPrice` appearing later is strictly better than any earlier high-price anchor.
*   **DP Equivalence:** The forward-pass code is essentially a space-optimized version of:
    `dp[i] = max(dp[i-1], prices[i] - min_price_so_far)`.
    Since `dp[i]` only depends on `dp[i-1]`, we reduce the state to a single variable `maxProfit`.
*   **Optimization Nuance:** In Java, the `Math.max` method introduces a minor function-call overhead. In extreme high-performance scenarios (e.g., real-time HFT simulations), replacing `Math.max` with an inline ternary operator `(a > b ? a : b)` can marginally reduce bytecode instructions, though the JIT compiler often performs this inlining automatically.
*   **Subtle Bug Warning:** Be cautious of the initialization of `minBuyPrice`. Initializing it to `0` instead of `prices[0]` or `Integer.MAX_VALUE` will result in incorrect profit calculations if the array contains only high prices (e.g., `[100, 101]`), as the algorithm might incorrectly compare against the zero-base rather than the actual price floor.

---
