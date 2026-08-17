# 4sum

## standard_solution_using threeloops(two_for_and_one_twopointerwhile_loops).java
*Style: detailed*

# Engineering Deep-Dive: 4Sum Implementation

## Summary
The solution employs a **fixed-pointer iterative reduction** strategy to solve the $k$-sum problem for $k=4$. By sorting the input array initially, the algorithm transforms a combinatorial search problem into a guided linear traversal. 

The approach uses two nested loops to anchor the first two elements of the quadruplet, reducing the problem to a "2-Sum" variant on the remaining suffix of the array. The inner 2-Sum is solved using the **Two-Pointer technique**, which leverages the sorted property to achieve monotonicity in the search space. To maintain uniqueness, the algorithm employs a conditional skipping strategy (deduplication) that ensures identical quadruplets are not added to the result set.

## Complexity Analysis

### Time Complexity: $O(n^3)$
*   **Sorting:** $O(n \log n)$, which is dominated by subsequent operations.
*   **Outer Loops:** The two nested loops run for approximately $O(n^2)$ iterations.
*   **Inner Two-Pointer:** For every combination of the first two indices, the `left` and `right` pointers traverse the remainder of the array at most $O(n)$ times.
*   **Aggregate:** $O(n \log n) + O(n^2 \cdot n) = O(n^3)$.

### Space Complexity: $O(1)$ (excluding output storage)
*   **Auxiliary Space:** $O(1)$ if we exclude the space required for the output list. The sorting algorithm (typically Dual-Pivot Quicksort in Java) may consume $O(\log n)$ stack space, but the iterative logic uses constant extra space.
*   **Result Space:** $O(m)$ where $m$ is the number of valid quadruplets found.

## Component Deep Dive

### 1. Integer Overflow Mitigation
The implementation performs a cast to `long` during the summation: `(long) nums[firstInd] + ...`. 
*   **Why:** In Java, `int` ranges from $-2^{31}$ to $2^{31}-1$. The sum of four integers can exceed these bounds (up to $\pm 8 \times 10^9$ range). Without the `long` cast, a silent overflow could cause the `sum == target` check to return false positives or incorrect comparisons, failing the logic.

### 2. Deduplication Strategy
The code handles duplicates at two levels:
*   **Outer Anchors:** `if (firstInd > 0 && nums[firstInd] == nums[firstInd - 1]) continue;` ensures we do not treat the same value as the start of a new quadruplet in the same position.
*   **Inner Pointers:** After finding a match, the `while (left < right && nums[left] == nums[left - 1])` loop aggressively skips identical elements. This is critical: if you fail to skip the inner duplicates, the algorithm would produce duplicate quadruplets if the input contains many repeated values (e.g., `[2, 2, 2, 2, 2]`).

### 3. Loop Termination/Boundaries
*   The `left < right` condition ensures the pointers never overlap, guaranteeing each set consists of four distinct indices.
*   Sorting effectively partitions the array into segments where the value increases monotonically, allowing the two-pointer approach to shrink the search window based on the `sum > target` comparison.

## Key Insights

### Performance Optimization Nuances
*   **Pruning:** While not implemented in this specific snippet, one could add early exits inside the loops. For example: `if (nums[firstInd] + nums[firstInd+1] + nums[firstInd+2] + nums[firstInd+3] > target) break;`. This is particularly useful for sparse solutions or cases where $n$ is very large, as it terminates the iteration when the smallest possible sum exceeds the target.
*   **Cache Locality:** Since the input is sorted, the two-pointer scan exhibits excellent cache locality, as it accesses sequential memory addresses.

### Subtle Bugs to Watch For
*   **Off-by-one errors:** Ensuring the `left` pointer starts at `secondInd + 1` is critical to prevent reuse of the same index for multiple positions in the quadruplet.
*   **Pointer Over-incrementing:** Notice the `left++` and `right--` *after* a match, followed by the `while` loops. The secondary `while` loops check `left - 1` and `right + 1`. If the primary increment were omitted, the `while` check could potentially access out-of-bounds or misread the current index. 
*   **Input Modification:** This algorithm modifies the input array via `Arrays.sort()`. In production systems, if the original array order must be preserved, a defensive copy (`nums.clone()`) is mandatory, though this increases space complexity to $O(n)$.

---

## standard_solution_using threeloops(two_for_and_one_twopointerwhile_loops).java
*Style: detailed*

# Technical Reference: K-Sum (Quadruplet) Optimization

## Summary
The provided solution addresses the 4-Sum problem using a **nested two-pointer strategy** atop a sorted array. By sorting the input ($O(N \log N)$), we reduce the search space for the final two elements to a linear scan, effectively lowering the complexity from the brute-force $O(N^4)$ to $O(N^3)$. The algorithm systematically fixes two indices and performs a two-pointer narrowing search on the remaining subarray to find complements that satisfy the target equation.

## Complexity Analysis

### Time Complexity: $O(N^3)$
*   **Sorting:** $O(N \log N)$ preprocessing step.
*   **Nested Loops:** The first two loops iterate $N$ and $N-1$ times respectively.
*   **Two-Pointer Scan:** Inside the second loop, the `left` and `right` pointers traverse the remaining subarray in $O(N)$ time.
*   **Total:** $O(N \log N + N^2 \times N) = O(N^3)$.

### Space Complexity: $O(1)$ to $O(N)$ (excluding output)
*   The algorithm operates in-place on the sorted array. 
*   **Auxiliary Space:** $O(1)$ if the sorting algorithm (e.g., Dual-Pivot Quicksort used by `Arrays.sort`) is considered in-place. Note that the stack space for sorting can reach $O(\log N)$ or $O(N)$ depending on the implementation. The returned list of lists is excluded from this complexity analysis.

---

## Component Deep Dive

### 1. Duplicate Suppression Logic
The algorithm employs three distinct checks to ensure set uniqueness in the result:
*   **Outer Loops:** `if (i > 0 && nums[i] == nums[i-1]) continue;` prevents selecting the same value for the same loop position across subsequent iterations.
*   **Inner Pointer Contraction:** After finding a match, the `while` loops skip adjacent identical values for `left` and `right`. This is crucial because, in a sorted array, identical values are grouped; incrementing/decrementing pointers simply passes over redundant combinations that would result in duplicate quadruplets.

### 2. Overflow Mitigation
The solution explicitly casts `nums[i]` to `long` before summation:
```java
long sum = (long) nums[firstInd] + nums[secondInd] + nums[left] + nums[right];
```
This is a critical production-grade consideration. Even if `nums[i]` are within the range of `int`, the sum of four large integers can easily exceed $2^{31}-1$. Without the cast, the JVM would perform 32-bit signed integer addition, leading to silent overflow and logical failure.

### 3. Pointer Dynamics
The `left` and `right` pointers effectively reduce the 4-sum problem to a 2-sum problem with a fixed sum (`target - nums[firstInd] - nums[secondInd]`). The convergence logic `sum > target` (decrement `right`) vs `sum < target` (increment `left`) is only valid because the array is sorted.

---

## Key Insights

### Performance Nuance: Search Space Pruning
While the provided code uses the standard approach, it can be further optimized by introducing **"early-exit" and "skip" heuristics** to prune branches that cannot possibly satisfy the target:
*   **Early Exit:** If `nums[i] + 3 * nums[i+1] > target`, we can break the outer loop as all subsequent quadruplets will exceed the target.
*   **Early Skip:** If `nums[i] + 3 * nums[n-1] < target`, we can `continue` the outer loop as the largest possible quadruplet starting with `nums[i]` is still too small.

### The "Sort Trap"
Sorting is a non-negotiable prerequisite. Without it, the greedy pointer movement (increment/decrement) lacks the property of monotonicity required to satisfy the logic. A common subtle bug in interview environments is attempting to use a `HashSet` to store intermediate sums for 4-Sum—while that would reduce complexity to $O(N^2)$, it significantly increases memory overhead and complicates the logic for ensuring quadruplet uniqueness compared to the sorted pointer approach.

### Memory Safety
The code relies on `Arrays.sort`. For extremely large arrays, be wary of the stack depth of `Arrays.sort` (if implemented via Quicksort), although standard JDK implementations optimize this heavily. If dealing with real-time constrained systems, a primitive heap-sort would guarantee $O(1)$ space complexity for sorting.

---
