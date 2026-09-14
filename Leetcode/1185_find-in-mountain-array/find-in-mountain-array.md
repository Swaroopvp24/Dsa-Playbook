# find-in-mountain-array

## standard_binary_search.java
*Style: detailed*

# Deep-Dive Reference: Mountain Array Search

## Summary
The solution employs a **three-phase divide-and-conquer strategy** utilizing binary search to find a `target` within a bitonic array (a mountain array). Given the array's unimodal structure (strictly increasing until a peak, then strictly decreasing), the problem is partitioned into:
1. **Peak Identification:** Locating the global maximum.
2. **Ascending Search:** Standard binary search on $[0, \text{peakIndex}]$.
3. **Descending Search:** Inverted binary search on $[\text{peakIndex} + 1, \text{length} - 1]$.

This approach avoids linear scans, leveraging the property that the array can be treated as two independent sorted subarrays.

---

## Complexity Analysis

### Time Complexity: $O(\log N)$
*   **Peak Finding:** A binary search over the range $[0, N-1]$ performs $O(\log N)$ comparisons.
*   **Subarray Searches:** We perform two binary searches, each $O(\log N)$.
*   **Total:** $O(\log N) + O(\log N) + O(\log N) = O(\log N)$. Since the `MountainArray.get()` interface is effectively an $O(1)$ abstraction, the total complexity remains logarithmic relative to the array length $N$.

### Space Complexity: $O(1)$
*   The algorithm operates using a fixed number of pointer variables (`left`, `right`, `middle`, `peakIndex`). No auxiliary data structures or recursion stacks are used, ensuring constant space requirements.

---

## Component Deep Dive

### 1. `findPeakIndex` (Peak Identification)
This function utilizes the property that if `arr[mid] < arr[mid + 1]`, the slope is positive, meaning the peak *must* exist at `mid + 1` or further right. 
*   **Edge Case Handling:** By using `right = middle` (instead of `middle - 1`), the algorithm safely preserves the potential peak index during narrowing. The termination condition `left < right` guarantees convergence to a single index where the invariant `arr[left] >= arr[left + 1]` is eventually violated or met.

### 2. `binarySearch` (Polymorphic Logic)
This is a unified implementation for both sorted halves.
*   **Logic Branching:** The `boolean ascending` flag toggles the comparison operator behavior.
    *   **Ascending:** If `midVal < target`, the search window shifts right (`left = mid + 1`).
    *   **Descending:** If `midVal < target`, the search window shifts left (`right = mid - 1`), because a value smaller than the target in a descending sequence implies the target is "higher up" toward the peak (leftward).
*   **Return Priority:** The main function calls the ascending search first. This explicitly handles the requirement to return the *smallest* index if the `target` appears twice (once on each slope).

---

## Key Insights & Nuances

### 1. Minimizing API Calls
The interface `mountainArray.get()` is the primary bottleneck. In a production system, these are often hidden network or disk I/O calls. 
*   **Optimization:** A standard implementation of `binarySearch` that calls `get()` twice per iteration (once for comparison, once for range adjustment) would be inefficient. Note that this implementation caches `middleValue` once per iteration, keeping constant factors low.

### 2. Termination Safety
The `findPeakIndex` loop logic is inherently safe against `ArrayIndexOutOfBoundsException`. By checking `mid < mid + 1` and limiting the range to `arrayLength - 1`, we ensure `middle + 1` is always valid.

### 3. Potential Pitfalls
*   **Range Splitting:** The split point between the two binary searches must be precise: `[0, peakIndex]` and `[peakIndex + 1, N - 1]`. If the peak is the last element, the second call to `binarySearch` will receive `(N, N-1)` as bounds. The `while (left <= right)` condition in the search correctly handles this by skipping execution immediately.
*   **Integer Overflow:** The calculation `int middle = left + (right - left) / 2` is used instead of `(left + right) / 2`. This is a critical defensive coding practice to prevent overflow when dealing with extremely large index values.

---

## standard_binary_search(cache).java
*Style: detailed*

# Deep Dive: Finding a Target in a Mountain Array

## 1. Summary
The problem requires finding the minimum index of a `target` within a "mountain array"—a sequence that strictly increases to a peak element and then strictly decreases. 

**Algorithmic Technique:** The solution employs **Three-Phase Binary Search**. Since the array is bitonic, it cannot be searched using a standard binary search. Instead, we decompose the search space by:
1. Identifying the peak index using a variation of binary search ($O(\log N)$).
2. Performing a standard binary search on the ascending left partition.
3. Performing a modified binary search on the descending right partition.

The `valueCache` acts as a memoization layer to adhere to the strict `MountainArray.get()` call constraints, preventing redundant API requests during the search phases.

---

## 2. Complexity Analysis

### Time Complexity: $O(\log N)$
*   **Peak Identification:** We perform a binary search over $N$ elements, requiring $O(\log N)$ comparisons.
*   **Search Phase:** We perform at most two binary searches on sub-arrays of size $P$ (ascending) and $N-P$ (descending). Each binary search takes $O(\log N)$.
*   **Total:** $O(\log N + \log N + \log N) = O(\log N)$.

### Space Complexity: $O(K)$
*   The `valueCache` stores unique lookups. In the worst case, every `get()` operation is unique.
*   **Total:** $O(\log N)$ calls are made due to the three-stage binary search, thus the cache space is $O(\log N)$.

---

## 3. Component Deep Dive

### `getValue(index, mountainArray)`
*   **Purpose:** Encapsulates the API interaction with a memoization layer.
*   **Critical Detail:** The problem specifies a limited number of `get()` calls. While simple binary search only touches $O(\log N)$ indices, the Peak search needs to compare triplets `(mid-1, mid, mid+1)`. Caching ensures that even if a branch visits a previously checked index, we remain within the problem's strict budget.

### Peak Search Logic
*   **Implementation:** The search window is restricted to `[1, length - 2]` as the definition of a mountain array mandates that the peak cannot be at the boundaries.
*   **Conditions:**
    *   `leftValue < midValue < rightValue`: Slope is positive; peak is to the right.
    *   `leftValue > midValue > rightValue`: Slope is negative; peak is to the left.
    *   Else: `mid` is the global maximum.

### Binary Search (Ascending vs. Descending)
*   **Generalization:** The implementation uses a boolean `ascending` flag. 
    *   **Ascending:** If `midValue < target`, we move right (`left = mid + 1`).
    *   **Descending:** If `midValue < target`, the target must be to the left, because values decrease as index increases (`right = mid - 1`).

---

## 4. Key Insights & Nuances

### The "Smallest Index" Requirement
The problem mandates returning the **smallest** index if the target appears multiple times. This is naturally handled by the search order:
1. Search `[0, peakIndex]` first. If found, this is guaranteed to be the smallest index.
2. Only search `[peakIndex + 1, N-1]` if the target was not found in the ascending half.

### Performance Nuance: The Triple-Get
In the peak identification phase, the code fetches `mid-1`, `mid`, and `mid+1` every iteration. While this is $3 \times \log N$ calls, it is safer than alternatives that might cause `IndexOutOfBounds` exceptions or complex boundary arithmetic. Given that $N$ is typically up to $10^4$ or $10^5$, $3 \log N$ is well within standard limits (usually ~100 calls allowed).

### Subtle Edge Case: The "Sharp" Peak
The binary search for the peak correctly handles arrays that might peak early or late. By enforcing `left = 1` and `right = length - 2`, we avoid unnecessary calls to `get()` on invalid indices (e.g., `get(-1)` or `get(length)`), which would otherwise lead to a runtime exception in the underlying API.

### Potential Optimization
The `valueCache` could be removed if the logic was purely iterative and carefully managed. However, using a `Map` is excellent for debugging and ensures that the API call limit is strictly respected even if the search logic is modified in the future. For memory-constrained environments, one could replace `HashMap` with a fixed-size `int[]` if the max $N$ is known, reducing the overhead of object allocation.

---
