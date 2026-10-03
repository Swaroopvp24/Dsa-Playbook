# monotonic-array

## attempt_1.java
*Style: detailed*

# Technical Reference: Monotonic Array Validation

## Summary
The solution implements a **single-pass linear scan** to determine the monotonicity of an integer array. An array is defined as monotonic if it is either monotone increasing (non-decreasing) or monotone decreasing (non-increasing). The algorithm maintains two boolean state flags—`isIncreasing` and `isDecreasing`—which are lazily invalidated as the algorithm encounters local inversions. By evaluating both conditions simultaneously, the solution avoids redundant traversals and handles constant or strictly monotonic sequences with equal efficiency.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The algorithm performs exactly one iteration over the input array of length $N$, starting from the second element ($i=1$). 
*   **Constant Factor:** Each iteration executes two constant-time conditional checks and potential boolean assignments. The total number of operations is approximately $2(N-1)$, yielding a strict $O(N)$ complexity.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm uses a fixed amount of auxiliary space. Regardless of the input size $N$, only two boolean primitives are stored on the stack. No heap-allocated structures or recursion stacks are utilized, ensuring constant space complexity.

## Component Deep Dive

### State Management
The logic relies on **Boolean State Invalidation**:
1.  **Initialization:** Both flags are initialized to `true`, representing the optimistic assumption that the array could be either monotonic type.
2.  **Inversion Detection:**
    *   `nums[i] < nums[i - 1]`: This constitutes a "descending step." If this occurs, the array violates the non-decreasing condition (`isIncreasing = false`).
    *   `nums[i] > nums[i - 1]`: This constitutes an "ascending step." If this occurs, the array violates the non-increasing condition (`isDecreasing = false`).

### Edge-Case Handling
*   **Empty Arrays / Single-Element Arrays:** The loop condition `i < nums.length` (where $i$ starts at 1) will fail immediately for `nums.length < 2`. The function will correctly return `true` (as both flags remain `true`), consistent with the mathematical definition of monotonicity for vacuously true or single-point sets.
*   **Arrays with Identical Elements:** If all elements are equal, neither `nums[i] < nums[i - 1]` nor `nums[i] > nums[i - 1]` will ever evaluate to true. Both flags remain `true`, and `true || true` returns `true`, correctly identifying constant arrays as monotonic.
*   **Strict vs. Non-Strict:** The use of the `>` and `<` operators (rather than `>=` or `<=`) allows the algorithm to distinguish between "strictly monotonic" and "non-decreasing/non-increasing."

## Key Insights

### Performance Optimization Nuances
While the algorithm is already optimal at $O(N)$, it features an **early exit opportunity**. If both `isIncreasing` and `isDecreasing` become `false` mid-iteration, the function could technically return `false` immediately to save cycles.
*   *Optimization Suggestion:*
    ```java
    if (!isIncreasing && !isDecreasing) return false;
    ```
    Adding this check inside the loop effectively transforms the average-case runtime for non-monotonic arrays, as the function would exit the moment a "zig-zag" pattern is detected.

### Subtle Logic Observations
*   **Independence of States:** It is impossible for both flags to be set to `false` in a strictly sorted array, but it is possible for both to remain `true` (e.g., `[1, 1, 1]`). 
*   **State Decay:** Note that the logic is monotonically decreasing in its "truthfulness." Once a flag is flipped to `false`, it can never be flipped back to `true`. This creates a deterministic path where the truth value of the array’s status is permanently locked in after the first violating pair is found.

---
