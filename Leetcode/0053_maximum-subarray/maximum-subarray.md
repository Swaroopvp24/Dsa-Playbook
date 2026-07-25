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

## Kadane's_algorithm_print_array.java
*Style: detailed*

# Technical Deep-Dive: Kadane’s Algorithm Implementation

## Summary
The provided implementation solves the **Maximum Subarray Problem** using **Kadane’s Algorithm**, a dynamic programming approach that identifies the contiguous subarray within a one-dimensional numeric array that has the largest sum. 

The algorithm operates on the principle that the maximum subarray ending at index `i` is either the element at `i` itself or the sum of the maximum subarray ending at `i-1` plus the element at `i`. By maintaining a running `sum` and resetting it to zero whenever it drops below zero (effectively discarding a prefix that would decrease the potential sum of future subarrays), the solution achieves linear time efficiency.

## Complexity Analysis

*   **Time Complexity: $O(n)$**
    *   The algorithm utilizes a single pass over the input array of size `n`. 
    *   The `if` logic inside the loop performs constant-time arithmetic and comparison operations.
    *   The final `for` loop, used for printing, is bounded by the length of the identified subarray ($k \le n$), resulting in $O(n + k) \approx O(n)$.
*   **Space Complexity: $O(1)$**
    *   The algorithm uses a constant amount of extra space (`max`, `sum`, `st`, `ansst`, `ansend`) regardless of the input array size. No auxiliary data structures (like arrays or hash maps) are allocated.

## Component Deep Dive

### 1. The Greedy Reset Mechanism
```java
if (sum < 0) { sum = 0; }
```
This is the heart of Kadane’s logic. If the running `sum` becomes negative, any subarray starting before the current index will only diminish the sum of any subsequent subarray. Thus, we "reset" the window. 

### 2. Boundary Tracking
The implementation explicitly tracks indices `st`, `ansst`, and `ansend`:
*   `st`: Monitors the potential beginning of a new subarray. It updates to `i` only when `sum` is reset to 0.
*   `ansst`/`ansend`: These capture the definitive boundaries of the maximum sum window found so far, allowing for the subsequent print operation.

### 3. Edge-Case Handling
*   **Negative Arrays:** The initial value `max = -10001` (based on typical problem constraints where `nums[i] >= -10000`) correctly handles arrays containing only negative numbers. 
*   **Single Element Arrays:** The logic gracefully handles single-element arrays; the loop executes once, updates `max`, and returns the element.
*   **Empty Arrays:** While not explicitly guarded against (e.g., `nums.length == 0`), the code would return `-10001` and skip the print loop, which is technically safer than an `ArrayIndexOutOfBoundsException` but suggests a potential need for an initial `if (nums == null || nums.length == 0)` guard.

## Key Insights & Nuances

*   **The "Reset" Limitation:** The logic `if (sum == 0)` to set `st = i` is slightly fragile. If the array contains a sequence that sums to zero exactly (e.g., `[-1, 1]`), the logic might trigger resets in ways that don't capture the mathematically optimal start index if not carefully aligned with the `sum < 0` logic.
*   **Output vs. Return:** The inclusion of `System.out.print` inside a method intended for competitive programming or library integration is a **side-effect anti-pattern**. In production systems, this should be refactored into a separate utility or the boundaries should be returned via a custom object/result array.
*   **Initialization Constraints:** Using `-10001` as a sentinel value is risky. If the input array contains values smaller than -10,000, `max` will be initialized incorrectly. A more robust approach is to initialize `max = nums[0]` and start the loop from `i = 1`.
*   **Performance Optimization:** The `System.out.print` inside the loop is an $O(k)$ operation that performs I/O. In high-frequency trading or performance-critical loops, I/O should be buffered or deferred to prevent system call overhead.

---
