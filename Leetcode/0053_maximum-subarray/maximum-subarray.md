# maximum-subarray

## Kadane's_algorithm.java
*Style: detailed*

# Technical Deep-Dive: Kadane’s Algorithm Implementation

## Summary
The provided implementation uses **Kadane’s Algorithm**, a classic dynamic programming approach to the Maximum Subarray Problem. The core intuition is that a subarray ending at index `i` is either the element `nums[i]` itself or the sum of the subarray ending at `i-1` plus `nums[i]`. By resetting the running `sum` to zero whenever it drops below zero, we effectively discard prefix sums that would only decrease the value of any subsequent subarray.

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The algorithm performs a single linear pass over the input array `nums`. Each element is visited exactly once, and the operations inside the loop (addition, comparison, assignment) are constant-time $O(1)$ operations.
*   **Efficiency:** This is the theoretical lower bound for the problem, as one must inspect every element at least once to determine the maximum subarray sum.

### Space Complexity: $O(1)$
*   **Derivation:** The solution utilizes only two primitive integer variables (`max` and `sum`) regardless of the input size $n$. 
*   **Efficiency:** This is optimal, as it avoids storing intermediate state or recursion stacks.

---

## Component Deep Dive

### 1. Initialization Strategy (`int max = -10001`)
*   **The Constraint Bias:** The initialization of `max` to `-10001` relies on the implicit constraints of the problem (typically $|nums[i]| \leq 10^4$). 
*   **Risk:** While sufficient for LeetCode constraints, this is fragile in production code. A more robust approach would be to initialize `max = nums[0]` to correctly handle arrays consisting entirely of negative numbers.

### 2. The Reset Mechanism (`if (sum < 0) sum = 0`)
*   **Algorithmic Purpose:** This acts as a "hard cut" for the sliding window. If the current running sum is negative, it indicates that the current subarray is a "net loss" for any potential future sequence. Restarting the sum at zero effectively begins a new candidate subarray starting at the next index.
*   **Edge-Case - All Negatives:** In the provided code, if the array is `[-5, -2, -10]`, the code correctly returns `-2` because the `max` update occurs *before* the `sum` reset. If the logic were flipped, the code might incorrectly return `0`.

---

## Key Insights & Nuances

### 1. The "Maximum vs. Reset" Order
A subtle but critical detail is the order of operations:
1. `sum += nums[i]`
2. `if (sum > max) max = sum`
3. `if (sum < 0) sum = 0`

By updating `max` *before* resetting `sum` to 0, the algorithm preserves the ability to identify the least negative value when all elements in the array are negative. If `sum` were reset before checking `max`, the code would return `0` for an all-negative array (assuming `max` was initialized to 0), which is mathematically incorrect as the empty set is not typically considered a valid subarray.

### 2. Integer Overflow
*   **Vulnerability:** The current implementation uses `int`. If the array contains many large positive values, the `sum` variable could potentially overflow the 32-bit signed integer limit (`2^31 - 1`). 
*   **Optimization:** In high-scale or high-precision scenarios, the code should be refactored to use `long` for the `sum` accumulator to prevent overflow, or perform overflow checks if the constraints allow for extreme values.

### 3. Comparison with Divide and Conquer
While this implementation is $O(n)$, a Divide and Conquer approach would yield $O(n \log n)$. Kadane’s is strictly superior in both time and space for this specific problem, making it the preferred solution for system-level implementations where latency is critical.

---

## attempt_2.java
*Style: concise*

### Notes: Maximum Subarray (Kadane’s Variation)

**Overview**
This implementation calculates the maximum subarray sum using a modified Kadane’s algorithm. It handles arrays containing both positive and negative integers by tracking a running sum and updating the global maximum accordingly.

**Key Components**
*   `maxSubArray`: Tracks the current running sum of the subarray. Resets to 0 if the sum drops below zero.
*   `ans`: Stores the global maximum sum encountered.

**Logic & Observations**
*   **Initialization**: `ans` is initialized to `nums[0]` rather than `0` to correctly handle arrays consisting entirely of negative numbers.
*   **Negative Number Handling**: Inside the reset block (`maxSubArray < 0`), `ans` is updated with `Math.max(ans, nums[i])`. This is critical: if all numbers are negative, the algorithm ignores the "reset to 0" logic for the current element and correctly identifies the largest single negative value.
*   **Complexity**: $O(n)$ time complexity; $O(1)$ space complexity.

---
