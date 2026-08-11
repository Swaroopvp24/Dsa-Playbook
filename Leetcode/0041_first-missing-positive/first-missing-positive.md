# first-missing-positive

## attempt_1.java
*Style: detailed*

# Technical Reference: Cyclic Sort for First Missing Positive

## 1. Summary
The provided implementation solves the "First Missing Positive" problem using the **Cyclic Sort** algorithm. This is an in-place rearrangement technique that exploits the fact that for an array of size $N$, the first missing positive must lie within the range $[1, N+1]$. 

The algorithm treats the array indices as a hash map, attempting to place each integer $x$ (where $1 \le x \le N$) at the index $x-1$. After rearranging, any index $i$ that does not contain the value $i+1$ represents the smallest missing positive integer.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
While there is a `while` loop nested within a `for` loop, the algorithm performs at most $O(N)$ swaps. 
*   **Proof:** Each swap operation places at least one element into its "correct" target position ($nums[i] == i+1$). Once an element is in its correct position, it is never moved again. Since there are $N$ positions, there can be at most $N-1$ successful swaps. The initial scans and comparisons add linear overhead, maintaining the $O(N)$ boundary.

### Space Complexity: $O(1)$
The algorithm operates entirely in-place. It utilizes a constant amount of auxiliary space (only a temporary variable for the swap), satisfying the requirement for $O(1)$ extra space beyond the input array.

---

## 3. Component Deep Dive

### The Placement Logic (Cyclic Sort)
The core logic resides in:
```java
while(nums[i] > 0 && nums[i] <= nums.length && nums[i] != nums[nums[i] - 1])
```
*   **Bound Filtering:** `nums[i] > 0 && nums[i] <= nums.length` ignores negative numbers, zeroes, and values larger than the array length, as these cannot be the "first missing positive" within the range $[1, N]$.
*   **Duplicate/Cycle Detection:** The condition `nums[i] != nums[nums[i] - 1]` is critical. It prevents infinite loops when the array contains duplicates. If the target slot already contains the correct value, the algorithm skips the swap.

### The Verification Pass
After the reordering, the algorithm performs a linear scan:
```java
if(nums[i] != (i+1)) return i+1;
```
If the first element that violates the `nums[i] == i+1` constraint is found, that index plus one is the answer. If the loop completes without returning, it implies all numbers from $1$ to $N$ are present, necessitating the return of $N+1$.

---

## 4. Key Insights & Nuances

### Subtle Trap: The Swap Order
The implementation uses `swap(nums, i, nums[i] - 1)`. Because `nums[i]` is used to index the array *and* is modified during the swap, the evaluation order is vital. In this specific code, the expression `nums[i] - 1` is evaluated to identify the swap target, then the swap occurs, then the `while` condition is re-evaluated. If `nums[i]` were updated *before* the target index was calculated, it would lead to an `ArrayIndexOutOfBoundsException` or incorrect swaps.

### Performance Edge Case: Duplicates
The `nums[i] != nums[nums[i] - 1]` condition is the primary guard against infinite loops in scenarios involving duplicates (e.g., `[1, 1]`). Without this check, the algorithm would endlessly swap the value `1` with itself at `nums[0]`.

### Memory Write-Back
This approach is destructive—it modifies the input array. In a production environment or a multi-threaded context, ensure that this side effect is intended. If the original data must be preserved, an $O(N)$ space copy or a different approach (like a BitSet if memory permits) would be required.

### Logic Flow Optimization
The `if` check inside the first loop (`nums[i] <= 0 || nums[i] > nums.length`) acts as a fast-path filter. While technically redundant due to the `while` loop conditions, it serves as a micro-optimization to avoid entering the `while` block for elements that are definitively out of scope.

---
