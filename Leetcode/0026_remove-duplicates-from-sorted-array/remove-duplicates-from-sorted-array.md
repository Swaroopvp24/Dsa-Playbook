# remove-duplicates-from-sorted-array

## standard_two_pointer.java
*Style: detailed*

# Technical Deep-Dive: In-Place Array Deduplication

## 1. Summary
The provided implementation employs the **Two-Pointer (Read/Write) Technique** to solve the problem of removing duplicates from a sorted array in-place. 

The algorithm leverages the invariant that the input array is **sorted**. Because duplicates are guaranteed to be contiguous, the algorithm only needs to compare the current element with its immediate predecessor to detect a new unique value. By maintaining a `writeIndex` that lags behind or tracks the position of the last confirmed unique element, we transform the array in $O(1)$ auxiliary space without requiring a secondary buffer.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Reasoning:** The algorithm performs a single linear pass over the input array using `readIndex`. Each element is visited exactly once. Within the loop, the comparison `nums[readIndex] != nums[readIndex - 1]` and the subsequent assignment are constant-time $O(1)$ operations.
*   **Best/Worst Case:** Both are $O(N)$, as the loop must iterate from $1$ to $N-1$ regardless of the density of duplicates.

### Space Complexity: $O(1)$
*   **Reasoning:** The algorithm operates strictly in-place. It utilizes a fixed number of integer variables (`writeIndex`, `readIndex`) regardless of the input array size $N$. No additional data structures (like HashSets or auxiliary arrays) are instantiated.

## 3. Component Deep Dive

### The Write-Pointer Strategy
*   **Initialization:** `writeIndex` is initialized to `1` rather than `0`. This accounts for the fact that the first element (`nums[0]`) is always unique by definition and does not need to be moved.
*   **The Comparison:** `nums[readIndex] != nums[readIndex - 1]` acts as a local filter. Since the array is sorted, any value that differs from its predecessor is inherently unique relative to all previous elements stored at or before `writeIndex - 1`.
*   **In-Place Mutation:** When a unique element is found, `nums[writeIndex++] = nums[readIndex]` shifts the unique element forward to the "boundary" of the unique sub-array. The post-increment operator ensures the pointer always points to the next available slot for a future unique value.

### Edge-Case Handling
*   **Empty Array:** If `nums.length == 0`, the loop condition `1 < 0` fails immediately. The function returns `writeIndex` (1), which is technically incorrect for an empty array.
    *   *Note:* In standard LeetCode environments, this constraint is usually $1 \le nums.length$. If input can be length 0, a guard clause `if (nums.length == 0) return 0;` is required.
*   **Single Element Array:** If `nums.length == 1`, the loop does not execute, returning `writeIndex = 1`. This is correct.
*   **No Duplicates:** The `if` condition remains true for every iteration; `writeIndex` will increment alongside `readIndex`, effectively copying the array onto itself.

## 4. Key Insights

### Performance Nuance: Memory Locality
Because this algorithm performs sequential reads and writes, it is highly **cache-friendly**. The CPU pre-fetcher can effectively predict the next memory address, leading to high spatial locality and minimal cache misses compared to algorithms that involve frequent jumps or non-linear memory access.

### The "Write-Behind" Logic
A subtle detail often overlooked is that the data at `writeIndex` (and beyond) is effectively "garbage" once the algorithm terminates. The caller of this function must strictly use the returned `writeIndex` to define the bounds of the valid sub-array. Any elements residing at indices $\ge$ `return value` should be considered stale.

### Potential Pitfall: Stability and Sorting
This algorithm is **not stable** in the sense that it relies entirely on the input being pre-sorted. If the array were unsorted, this approach would fail to identify all duplicates. The reliance on `nums[readIndex - 1]` creates a stateful dependency on the previous iteration's comparison, which is the "engine" of the deduplication logic.

---
