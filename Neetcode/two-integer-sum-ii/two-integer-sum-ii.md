# two-integer-sum-ii

## standard_two_pointer.java
*Style: detailed*

# Engineering Deep Dive: Two-Sum (Sorted Array Variant)

## Summary
The provided implementation solves the **Two-Sum II** problem (finding two indices in a sorted array that sum to a specific target). It utilizes the **Two-Pointer Technique** (specifically, the *Meet-in-the-Middle* approach). By leveraging the property that the input array is already sorted, the algorithm avoids the $O(N^2)$ brute-force search and the $O(N)$ auxiliary space overhead of a Hash Map, achieving optimal linear time complexity with constant space.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** The pointers `st` (start) and `en` (end) move monotonically toward each other. In the worst-case scenario (where the target sum is at the center or does not exist), the two pointers will traverse the entire length of the array exactly once. Each iteration performs a constant-time $O(1)$ arithmetic comparison and pointer increment/decrement.
*   **Constraints:** Since $N$ operations are performed, the complexity is $O(N)$.

### Space Complexity: $O(1)$
*   **Derivation:** The algorithm operates in-place. It only initializes two integer primitives (`st` and `en`) and a `sum` variable, regardless of the input array size. No auxiliary data structures (like HashMaps or Heaps) are allocated.
*   **Constraint:** This is the most memory-efficient approach possible for this problem.

---

## Component Deep Dive

### 1. Pointer Logic
*   **`st` (Left Pointer):** Initialized at index `0`. It expands the lower bound of the window.
*   **`en` (Right Pointer):** Initialized at `nums.length - 1`. It contracts the upper bound of the window.
*   **Mechanism:**
    *   If `sum < target`: The current sum is too low. To increase the sum, we must move the left pointer to the right (`st++`), leveraging the sorted property to ensure we encounter a larger value.
    *   If `sum > target`: The current sum is too high. To decrease the sum, we move the right pointer to the left (`en--`), accessing a smaller value.

### 2. Edge Case Handling
*   **`nums.length < 2`:** The `while (st < en)` condition handles arrays with length 0 or 1 gracefully. The loop will not execute, and the method returns the default `{-1, -1}`.
*   **No Solution Exists:** If no pair sums to the target, the pointers will eventually meet (or cross), terminating the loop and returning `{-1, -1}`.
*   **Integer Overflow:** The current implementation `int sum = nums[st] + nums[en]` is susceptible to integer overflow if `nums[st]` and `nums[en]` are near `Integer.MAX_VALUE`. In a production environment, this should be cast to `long` or compared using subtraction (`nums[st] > target - nums[en]`) to ensure safety.

---

## Key Insights

### 1. The "Sorted" Prerequisite
This approach is strictly dependent on the array being sorted. If the array were unsorted, a pre-sort step would be required, elevating the time complexity to $O(N \log N)$ (due to Timsort/Quicksort) or requiring a Hash Map for $O(N)$ time at the cost of $O(N)$ space.

### 2. Output Requirement Nuance
The solution returns `st + 1` and `en + 1`. This is a classic "1-indexed" requirement often found in technical interview platforms (like LeetCode). When porting this to a system API, ensure the calling layer expects 1-based indices to prevent "off-by-one" errors in downstream array access.

### 3. Loop Termination
The loop `st < en` is optimal. If the target were possible using the same index twice (e.g., $target = 10, nums = [5]$), the loop condition would need to be `st <= en`. However, standard Two-Sum definitions typically prohibit reusing the same element, making `st < en` the correct constraint.

### 4. Performance Optimization Note
While branch prediction in modern CPUs handles the `if/else` logic well, in extremely high-throughput systems, replacing `if` statements with conditional arithmetic (where possible) is rarely necessary here, as the simplicity of the logic allows the JIT compiler to optimize the loop efficiently.

---
