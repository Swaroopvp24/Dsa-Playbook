# minimum-size-subarray-sum

## standard_sliding_window.java
*Style: detailed*

# Technical Deep Dive: Sliding Window Minimum Subarray

## Summary
The solution implements an **optimized sliding window** (also known as the "two-pointer" or "caterpillar" method) to solve the Minimum Size Subarray Sum problem. Unlike a brute-force $O(n^2)$ approach that evaluates every possible subarray, this algorithm maintains a dynamic window $[l, r]$ where the sum is at least `target`. By expanding the right bound ($r$) to incorporate elements and contracting the left bound ($l$) only when the constraint is satisfied, it effectively prunes the search space, ensuring each element is visited a constant number of times.

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** Although there is a nested `while` loop, the inner loop pointer `l` only ever moves forward. In the worst-case scenario, `r` traverses the array from $0$ to $n-1$ (n operations), and `l` also traverses from $0$ to $n-1$ (n operations). 
*   **Total Operations:** Since each pointer is incremented at most $n$ times, the total number of operations is $2n$, yielding a linear time complexity $O(n)$.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm utilizes a constant amount of extra space (`l`, `len`, `sum`, `r`). No auxiliary data structures (like HashMaps or prefix sum arrays) are allocated. The memory footprint remains independent of the input size $n$.

---

## Component Deep Dive

### 1. The Dynamic Window State
*   `sum`: Tracks the running total of the current window. Note that `sum` is `int`. If the array contains very large values, an overflow could occur; in production systems with arbitrary input ranges, `long` should be considered for `sum`.
*   `l` (Left Pointer): Marks the inclusive start of the window.
*   `r` (Right Pointer): Acts as the iterator for the current element being added.

### 2. Contraction Logic
The `while (sum >= target)` block is the core of the optimization. Once the window constraint is satisfied, we attempt to shrink the window from the left to find the **minimal** length.
*   `len = Math.min(len, r - l + 1)`: Updates the global minimum. The length of a window $[l, r]$ is calculated as $r - l + 1$.
*   `sum -= nums[l++]`: The contraction step. It removes the element at `l` and post-increments the pointer, effectively reducing the window size and the `sum`.

### 3. Edge Case Handling
*   **No solution found:** If the total sum of the array is less than `target`, the `while` loop never executes, and `len` remains `Integer.MAX_VALUE`. The return statement handles this by returning `0`, correctly identifying that no subarray satisfies the condition.
*   **Empty array/Null:** The current code assumes `nums` is non-null. In a production environment, an input validation check `if (nums == null || nums.length == 0) return 0;` should be prepended.
*   **Single-element window:** The logic correctly handles cases where a single element $\ge$ `target` because `r - l + 1` evaluates to $1$.

---

## Key Insights

### 1. The "Greedy" Nature
This approach is greedy because, for every `r`, it finds the *smallest* valid window ending at `r`. Because we want the global minimum, we keep updating `len` throughout the iteration. This is valid because if a smaller window exists, we will eventually encounter its ending index and update `len` accordingly.

### 2. Performance Nuances
*   **Branch Prediction:** The `while` loop is highly predictable for the CPU if the array contains mostly positive numbers, as the `sum` increases monotonically with `r` and decreases with `l`.
*   **Early Termination:** While not implemented here, if `nums` consists only of positive integers and we find a window of `len == 1`, we could theoretically return `1` immediately, as it is impossible to find a smaller valid subarray.

### 3. Potential Pitfalls: Negative Numbers
*   **Crucial Limitation:** This algorithm relies on the property that **adding an element always increases (or keeps constant) the sum, and removing one always decreases it.** 
*   If the input array contains **negative numbers**, this sliding window approach breaks. In the presence of negative values, the window sum is no longer monotonic, and one would need to use a `Prefix Sum + Monotonic Queue` or a `Balanced BST` approach to maintain $O(n \log n)$ or $O(n)$ complexity.

---

## prefixSum_binarySearch.java
*Style: detailed*

# Engineering Deep-Dive: Minimum Size Subarray Sum (Binary Search Approach)

## 1. Summary
This implementation solves the "Minimum Size Subarray Sum" problem by transforming the input array into a **Prefix Sum array** and performing a **Binary Search** for each possible starting position.

Instead of utilizing the standard sliding window (two-pointer) approach—which would be $O(N)$—this algorithm treats the prefix sum array as a monotonically increasing sequence. For every index $i$, it searches for the smallest index $j \ge i$ such that $PrefixSum[j+1] - PrefixSum[i] \ge target$. By decoupling the window search from a linear scan, this approach ensures that for any fixed start point, the optimal end point is found in logarithmic time.

## 2. Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Preprocessing:** Building the `prefixSum` array takes $O(N)$ time.
*   **Search Phase:** We iterate through $N$ indices. For each index, we perform a binary search over the range $[i, n]$, which takes $O(\log N)$.
*   **Total:** $O(N) + O(N \log N) = \mathbf{O(N \log N)}$.

### Space Complexity: $O(N)$
*   The algorithm allocates a `prefixSum` array of size $N+1$ to store the cumulative sums. This auxiliary space is mandatory for this specific implementation technique, making it less memory-efficient than the $O(1)$ space sliding window approach.

---

## 3. Component Deep Dive

### Prefix Sum Array Construction
The `prefixSum` array is defined such that `prefixSum[k]` stores the sum of `nums[0...k-1]`.
*   **Invariant:** `prefixSum[i+1] - prefixSum[j]` yields the sum of the subarray `nums[j...i]`.
*   **Edge Case:** The array size is $N+1$ to handle the empty prefix (sum 0) at `prefixSum[0]`, allowing range calculations for any subarray starting at index 0 without conditional branching.

### The Binary Search Engine
The core logic resides within the `while (l < r)` loop.
*   **Lower Bound Logic:** The code seeks the *leftmost* index `mid` where the condition `prefixSum[mid + 1] - prefixSum[i] >= target` is true.
*   **Convergence:** By setting `r = mid` when the condition is met, the search space narrows to the left, effectively finding the smallest subarray starting at $i$ that satisfies the target.
*   **Termination:** If `l` reaches `n` without satisfying the condition, it signifies that no subarray starting at index $i$ (or any subsequent index) can reach the target sum (assuming positive integers).

### Sentinel Value Handling
*   The code initializes `res = n + 1`. This serves as a sentinel value representing "Infinity" or "No valid subarray found." 
*   The final ternary operator `res == (n + 1) ? 0 : res` converts this sentinel into the required problem specification output of `0`.

---

## 4. Key Insights

### Why $O(N \log N)$ vs $O(N)$?
While this binary search approach is theoretically slower than the $O(N)$ two-pointer sliding window, it provides a distinct advantage when dealing with **non-contiguous query requirements** or scenarios where the array is static but queries for different "targets" might be performed repeatedly after the initial $O(N)$ preprocessing.

### Subtle Bugs & Nuances
*   **Integer Overflow:** The current implementation uses `int` for prefix sums. In production systems with large arrays or large integer values, the `prefixSum` array should be initialized as `long[]` to prevent overflow during the summation process.
*   **Monotonicity Constraint:** This algorithm **strictly relies on non-negative integers** in the `nums` array. If the array contains negative numbers, the `prefixSum` array is no longer monotonically increasing, rendering binary search invalid.
*   **The `mid` calculation:** The line `int mid = (l + r) / 2;` is generally safe here given the constraints, but in systems where `l + r` could exceed `Integer.MAX_VALUE`, `l + (r - l) / 2` is the idiomatic safety pattern.
*   **Loop Boundary:** `prefixSum[mid + 1]` accessed within the loop is safe because `r` is initialized to `n`, and `mid` is bounded by `n-1` at maximum, ensuring the access never exceeds index `n`.

---

## prefixSum_binarySearch.java
*Style: detailed*

# Engineering Deep-Dive: Minimum Size Subarray Sum (Binary Search Approach)

## Summary
The provided solution addresses the "Minimum Size Subarray Sum" problem by combining **prefix sum arrays** with **binary search**. By pre-calculating the cumulative sums of the input array, we transform the range-sum query problem into a constant-time operation ($O(1)$) using the property $Sum(i, j) = PrefixSum[j+1] - PrefixSum[i]$. For each starting index $i$, the algorithm performs a binary search over the possible ending indices $j \in [i, n-1]$ to find the smallest range that satisfies the threshold `target`.

## Complexity Analysis

### Time Complexity: $O(n \log n)$
*   **Prefix Sum Construction:** $O(n)$ to iterate through the array once.
*   **Binary Search Execution:** The algorithm iterates through each starting index $i$ ($n$ iterations). Inside the loop, it performs a binary search over the range $[i, n]$, which takes $O(\log n)$ time.
*   **Total:** $O(n + n \log n) \approx O(n \log n)$. 
*   *Note:* While $O(n)$ is achievable via the two-pointer (sliding window) technique, this $O(n \log n)$ approach is more robust when dealing with constraints requiring search queries on static ranges.

### Space Complexity: $O(n)$
*   **Auxiliary Space:** We allocate a `prefixSum` array of size $n+1$ to store the cumulative sums. This provides the necessary $O(1)$ look-up capability to satisfy the binary search condition.

---

## Component Deep Dive

### 1. Prefix Sum Array Construction
*   **Logic:** `prefixSum[i]` stores the sum of `nums[0...i-1]`. `prefixSum[0]` is initialized to 0. 
*   **Mathematical Property:** To calculate the sum of the subarray `nums[i...j]`, we use `prefixSum[j + 1] - prefixSum[i]`. This ensures we avoid redundant iterations inside the binary search logic.

### 2. Binary Search Strategy
*   **Invariant:** The `prefixSum` array is monotonically non-decreasing (assuming `nums[i] > 0`). This is a critical prerequisite for binary search.
*   **Search Space:** For a fixed `i`, we search for the smallest index `mid` such that `prefixSum[mid + 1] - prefixSum[i] >= target`.
*   **Refinement:** If `currentSum >= target`, the current `mid` is a potential candidate, so we store it and search the left half (`right = mid`) to see if a tighter subarray exists. If `currentSum < target`, we are guaranteed that all indices smaller than `mid` will also be insufficient, so we search right (`left = mid + 1`).

### 3. Edge-Case Handling
*   **`minLen` Initialization:** Initialized to `n + 1`. This acts as a sentinel value. If the code never finds a valid subarray, `minLen` remains `n + 1`, and the final ternary check correctly returns `0`.
*   **Non-existent Subarray:** If the total sum of `nums` is less than `target`, `left` will eventually equal `n` in the binary search. The condition `if (left != n)` prevents invalid index access and prevents updating `minLen` with an invalid subarray length.

---

## Key Insights

*   **Constraint Nuance:** This approach assumes all numbers in `nums` are positive. If the array contains negative numbers, the `prefixSum` array would lose its monotonic property, breaking binary search. In the presence of negative numbers, the Two-Pointer approach also fails, and one would typically use a `TreeMap` or a Monotonic Queue to maintain $O(n \log n)$ or $O(n)$ complexity.
*   **Memory Efficiency:** The `prefixSum` array takes $O(n)$ space. While space-efficient relative to $O(n^2)$ matrix-based approaches, it is less memory-efficient than the sliding window approach which runs in $O(1)$ extra space.
*   **Performance Optimization:** The `mid` calculation `(left + right) / 2` is safe from integer overflow because `n` is typically constrained within reasonable bounds for array indices in Java. However, in extreme architectural cases with very large arrays, `left + (right - left) / 2` is preferred as a best practice.
*   **The "Why":** Why use binary search over the sliding window ($O(n)$)? While this solution is technically slower than the $O(n)$ sliding window, it provides an elegant demonstration of **Range Query Optimization**. This pattern is often a building block for solving more complex 2D range sum queries where sliding windows are not applicable.

---
