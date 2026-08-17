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
