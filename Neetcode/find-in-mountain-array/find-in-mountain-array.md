# find-in-mountain-array

## standard_binary_search.java
*Style: detailed*

# Deep-Dive Reference: Mountain Array Search

## Summary
The problem requires finding a target in a "Mountain Array"—a unimodal array that strictly increases to a peak and strictly decreases thereafter. Due to the interface constraints (restricted API access), a standard linear search is $O(N)$, which is inefficient. The solution employs a **three-stage Binary Search strategy**:
1. **Peak Identification**: Find the pivot point (the peak) in $O(\log N)$ time.
2. **Ascending Search**: Perform binary search on the increasing slope $[0, \text{peak}]$.
3. **Descending Search**: If not found, perform binary search on the decreasing slope $[\text{peak} + 1, N-1]$.

## Complexity Analysis

### Time Complexity: $O(\log N)$
*   **Peak Search**: The peak search reduces the search space by half in each iteration, resulting in $O(\log N)$ calls to `mountainArray.get()`.
*   **Binary Search (Ascending/Descending)**: Each binary search is standard, yielding $O(\log N)$ per search.
*   **Total**: Since we perform at most three logarithmic searches, the complexity remains $O(\log N)$. Given the constraint on `mountainArray.get()` calls (typically limited to 100 in competitive environments), this satisfies the performance requirements.

### Space Complexity: $O(1)$
*   The algorithm uses a constant amount of auxiliary space for pointer variables (`left`, `right`, `middle`). No recursion or additional data structures (like arrays or maps) are utilized.

## Component Deep Dive

### 1. `findPeakIndex`
This function identifies the local maximum where $arr[i] < arr[i+1]$ transitions to $arr[i] > arr[i+1]$.
*   **Mechanism**: We use `middle` and `middle + 1` to compare adjacent values.
*   **Invariant**: The peak is always in the range `[left, right]`. If `get(middle) < get(middle + 1)`, the peak must be at `middle + 1` or further right. If the condition is false, the current `middle` could be the peak or the peak exists at `middle` or further left.
*   **Edge Case**: The loop terminates when `left == right`, which is mathematically guaranteed to be the index of the peak element.

### 2. `binarySearch`
A polymorphic binary search implementation handling both monotonic directions.
*   **Directional Logic**: 
    *   **Ascending**: `middleValue < target` $\implies$ move right.
    *   **Descending**: `middleValue < target` $\implies$ move left (because values are decreasing, a smaller value means the target must have appeared earlier in the index range).
*   **Short-circuiting**: The problem asks for the *smallest* index. By searching the ascending portion before the descending portion, we inherently satisfy the requirement to return the smallest index if the target exists in both segments (though this is theoretically impossible in a strictly unimodal mountain array, as values are unique).

## Key Insights

### The "Hidden" Constraint
The problem likely limits `mountainArray.get()` calls (e.g., 100 calls). Because $2^{10} = 1024$ and $2^{30} \approx 10^9$, even for an array of size $10^9$, three binary searches will use $\approx 3 \times \log_2(10^9) \approx 90$ calls, fitting safely within a typical limit of 100.

### Off-by-One Hazards
*   **`findPeakIndex`**: We use `right = middle` instead of `right = middle - 1`. This is critical. If `middle` is the peak, setting `right = middle - 1` would erroneously exclude the peak index from the search range.
*   **Mid-point calculation**: The expression `left + (right - left) / 2` is used consistently to prevent integer overflow, a standard practice for robust systems programming in Java.

### Logic Nuances
*   **Descending Search**: When searching the decreasing portion, the condition `if (middleValue < target)` implies the target is at a *lower* index. This is the inverse of the ascending search.
*   **Strict Monotonicity**: The problem implies a strictly increasing then strictly decreasing structure. If the array contained plateaus (e.g., `[1, 2, 2, 1]`), binary search would fail as the property of monotonicity would be violated, potentially leading to an incorrect peak identification.

---
