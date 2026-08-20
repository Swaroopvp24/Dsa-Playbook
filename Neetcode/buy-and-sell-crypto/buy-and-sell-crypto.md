# buy-and-sell-crypto

## greedy/dp_solution.java
*Style: detailed*

# Deep-Dive Reference: Single-Pass Stock Profit Maximization

This document evaluates the optimal strategies for the "Best Time to Buy and Sell Stock" problem. Both provided solutions leverage greedy algorithmic paradigms to solve the problem in linear time with constant space.

---

## 1. Summary: Algorithmic Approach
The problem is fundamentally an identification of the maximum difference between two elements $A[j]$ and $A[i]$ such that $j > i$.

*   **Approach A (Backward Pass):** Maintains a `rightMax` state. By iterating from the end to the beginning, we effectively determine the highest future selling price for every potential buying day $i$. This turns the problem into a running maximum calculation.
*   **Approach B (Forward Pass - Standard):** Maintains a `minBuyPrice` state. By iterating forward, we identify the lowest trough encountered so far and evaluate the profit delta against current market prices.

Both implementations are manifestations of **Space-Optimized Dynamic Programming**. They reduce the $O(n)$ space requirement of a classic DP table (storing min/max prefixes/suffixes) to $O(1)$ by maintaining only the necessary state variables.

---

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Reasoning:** We traverse the input array `prices` exactly once. For each element, we perform a constant number of operations: one comparison (`Math.max` or `Math.min`) and one arithmetic subtraction. Since $n$ iterations are performed where $n$ is the array length, the complexity is strictly linear.

### Space Complexity: $O(1)$
*   **Reasoning:** Regardless of the input size $n$, the memory footprint is restricted to two primitive integer variables (`maxProfit` and either `rightMax` or `minBuyPrice`). No auxiliary data structures (like arrays, stacks, or memoization tables) are allocated, satisfying the constant auxiliary space requirement.

---

## 3. Component Deep Dive

### Backward Pass Mechanism (`rightMax`)
*   **Logic:** The algorithm assumes we are selling at some index $i$ and looking for the maximum value in the range $[i, n-1]$.
*   **Edge Case Handling:**
    *   **Decreasing Sequence:** If prices are strictly decreasing, `maxProfit` remains `0` because `rightMax - prices[i]` will never be positive. This correctly handles the "no profit possible" case without extra conditionals.
    *   **Single Element Array:** If `prices.length == 1`, the loop executes once, `rightMax` becomes the element, `maxProfit` becomes `0`. Correct.

### Forward Pass Mechanism (`minBuyPrice`)
*   **Logic:** This is the canonical implementation. It assumes we are buying at some index $i$ and looking for the maximum value in the range $[i, n-1]$.
*   **Edge Case Handling:**
    *   **Empty Array:** The current snippet assumes `prices` is non-null/non-empty. If `prices.length == 0`, accessing `prices[0]` will throw an `ArrayIndexOutOfBoundsException`. A defensive check (`if (prices == null || prices.length == 0) return 0;`) is recommended for production-grade robustness.

---

## 4. Key Insights

### Greedy Choice Property
The "Greedy" nature works here because the decision at each step is independent of future price fluctuations beyond the simple "is this the lowest price seen so far?" check. We do not need to backtrack because the lowest price seen before index $i$ is globally optimal for the interval $[0, i]$.

### Performance & Micro-Optimization
*   **Branch Prediction:** The `Math.max`/`Math.min` functions are highly optimized, but in extremely latency-sensitive environments, replacing them with ternary operators (`val > max ? val : max`) can occasionally provide a minor instruction cycle improvement by reducing method call overhead and improving branch predictability.
*   **Instruction Pipeline:** Both loops are trivially parallelizable in theory, but the dependency on `minBuyPrice` or `rightMax` creates a carry-chain dependency. For standard input sizes, this is negligible, but for massive data streams, SIMD (Single Instruction, Multiple Data) optimizations would be required to outperform this single-pass sequential logic.

### Pitfalls to Watch For
1.  **Integer Overflow:** While stock prices are usually represented as integers, if prices are exceptionally large and the profit calculation were to involve sums, `long` might be necessary. Here, `maxProfit` is naturally bounded by the maximum element in `prices`, so `int` is sufficient unless prices exceed $2^{31}-1$.
2.  **Zero-Profit Semantic:** Ensure the requirements clarify whether selling on the same day is prohibited (which it is here, due to the implicit $j > i$ constraint). The code correctly handles this as the subtraction `prices[i] - prices[i]` will result in `0` profit, which is ignored by the `Math.max(maxProfit, ...)` operation.

---

## standard_two_pointer.java
*Style: concise*

### Overview
Calculates the maximum profit achievable from a single buy-sell transaction in an array of stock prices. Uses a sliding window (two-pointer) approach to achieve $O(n)$ time complexity and $O(1)$ space complexity.

### Key Logic
*   **`l` (Left Pointer):** Tracks the index of the lowest price encountered so far (the potential buying point).
*   **`r` (Right Pointer):** Iterates through the array to find potential selling points.
*   **Update Rule:** If `prices[r] > prices[l]`, calculate potential profit. If `prices[r] <= prices[l]`, the current price is a new local minimum, so update `l` to `r` to "reset" the buy point.

### Implementation Notes
*   **Efficiency:** The array is traversed exactly once.
*   **Greedy Reset:** By moving `l = r` whenever a lower price is found, the algorithm effectively discards previous, less-optimal buy points without redundant calculations.

---
