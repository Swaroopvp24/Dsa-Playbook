# 3sum

## standard_two_pointer.java
*Style: detailed*

# Engineering Deep-Dive: Three-Sum Optimization

## Summary
The solution employs a **fixed-pointer sweep with a two-pointer narrowing strategy**. By pre-sorting the input array, we transform the $O(n^3)$ brute-force search into an $O(n^2)$ problem. The algorithm iterates through each element, treating it as a "pivot" (or `fixedIndex`), and utilizes a two-pointer approach (`left` and `right`) on the remaining subarray to identify pairs that satisfy the zero-sum condition. This approach leverages the monotonic property of sorted arrays to prune the search space dynamically.

---

## Complexity Analysis

### Time Complexity: $O(n^2)$
*   **Sorting:** `Arrays.sort()` uses a Dual-Pivot Quicksort, contributing $O(n \log n)$.
*   **Iteration:** The outer loop runs $n$ times. The inner `while` loop runs in $O(n)$ time as the two pointers traverse the array toward each other. 
*   **Total:** $O(n \log n + n^2) = \mathbf{O(n^2)}$. The quadratic term dominates.

### Space Complexity: $O(n)$ or $O(\log n)$
*   **Auxiliary Space:** Depending on the implementation of `Arrays.sort()`, sorting takes $O(\log n)$ to $O(n)$ stack space.
*   **Resultant Space:** The result list is not strictly counted as auxiliary, but storing the result takes $O(k)$ where $k$ is the number of unique triplets. In the worst case, $k$ can be $O(n^2)$.
*   **Note:** If we treat the output as part of the space requirement, it is $O(n^2)$; otherwise, it is $O(\log n)$ (excluding input modification).

---

## Component Deep Dive

### 1. Pre-Sorting Strategy
Sorting is the architectural bedrock of this solution. It allows the two-pointer approach to make deterministic decisions:
*   If `sum < 0`: Increment the `left` pointer to increase the sum.
*   If `sum > 0`: Decrement the `right` pointer to decrease the sum.
Without sorting, we would be forced to use a Hash Map to track complements, which increases space complexity to $O(n)$ and complicates duplicate handling.

### 2. Duplicate Suppression Logic
The primary challenge in this problem is avoiding duplicate triplets (e.g., `[-1, 0, 1]` and `[-1, 0, 1]`). 
*   **Fixed Index Filter:** `if (fixedIndex > 0 && nums[fixedIndex] == nums[fixedIndex - 1])` prevents the pivot from starting a search sequence identical to the previous iteration.
*   **Pointer Filters:** Once a valid triplet is found, `left` and `right` are incremented/decremented and then passed through `while` loops to skip over subsequent values identical to the ones just used. This is a critical performance optimization that skips irrelevant redundant checks.

### 3. Edge Case Handling
*   **Array Length < 3:** The code naturally handles this; the outer loop executes, but the inner logic (`left = fixedIndex + 1`) ensures the `left < right` condition is never met, correctly returning an empty list.
*   **All Positive/Negative:** The pointers will converge without finding a zero-sum, resulting in an empty list.
*   **Large Inputs:** The `int` overflow for `sum` is unlikely here given standard constraints, but in production systems with larger range integers, one should verify if `nums[i] + nums[left] + nums[right]` could exceed `Integer.MAX_VALUE`.

---

## Key Insights

*   **Pointer Correctness:** A common implementation error is attempting to increment `left` and decrement `right` *before* the duplicate-skipping `while` loops. The current implementation correctly handles the `left < right` bounds check within the skipping loops to prevent index out-of-bounds errors.
*   **Invariant Maintenance:** The algorithm maintains the invariant that at every step, `nums[fixedIndex] <= nums[left] <= nums[right]` (or at least keeps them ordered relative to their movement). This prevents the "missing" of triplets that might occur if the array were not strictly sorted.
*   **Performance Nuance:** The `while` loops for skipping duplicates are `O(n)` internally, but they do not increase the overall time complexity because they only increment the same pointers that the two-pointer logic is already iterating. It is a "work-efficient" skip.
*   **Refinement Opportunity:** If the input array contains many duplicates, the duplicate-skipping logic significantly reduces the number of full $O(n)$ scans. This is a "best-case" scenario for the $O(n^2)$ algorithm.

---
