# find-k-closest-elements

## attempt_1_sorting.java
*Style: concise*

### Study Notes: Find K Closest Elements

#### Overview
This solution identifies the `k` elements in a sorted array that are closest to a target value `x`. It achieves this by sorting all array elements based on their absolute difference from `x`, then returning the first `k` elements in their original relative order.

#### Key Logic
*   **Custom Comparator**: 
    *   Sorts primarily by distance: `abs(num - x)`.
    *   Sorts secondarily by value: `a - b` (tie-breaker for equal distances).
*   **Post-processing**: Uses `list.subList(0, k)` to extract the result set, then performs `Collections.sort()` to restore the ascending order required by the problem's output contract.

#### Complexity Note
*   **Time**: $O(N \log N)$ due to the full array sort. 
    *   *Optimization Note:* This can be reduced to $O(\log N + k)$ using binary search to find the closest element and a two-pointer approach to expand the window.
*   **Space**: $O(N)$ to store the list copy.

---

## two_pointer_linearsearch.java
*Style: detailed*

# Technical Reference: `findClosestElements`

## 1. Summary
The implementation utilizes a **Greedy Two-Pointer Expansion** strategy. It identifies the element closest to the target $x$ as the initial seed, then iteratively expands outward toward the boundaries of the array. The algorithm evaluates the absolute difference $|x - arr[i]|$ for adjacent candidates to maintain the "closest elements" property, effectively performing a localized search that expands until the window size $k$ is satisfied. Finally, it sorts the result set to meet the problem's monotonic output requirement.

---

## 2. Complexity Analysis

### Time Complexity: $O(n + k \log k)$
*   **Initial Search ($O(n)$):** The algorithm performs a linear scan through the array to find the initial index `idx` closest to `x`. 
*   **Expansion ($O(k)$):** The `while` loop runs exactly $k-1$ times, performing constant-time comparisons and pointer updates in each iteration.
*   **Sorting ($O(k \log k)$):** The final `Collections.sort(res)` operation is necessary because the expansion process populates the list based on proximity, not array order.
*   *Note:* While the overall complexity is $O(n + k \log k)$, the search for the initial index could be optimized to $O(\log n)$ using binary search, which would reduce the total complexity to $O(\log n + k \log k)$.

### Space Complexity: $O(k)$
*   The space requirement is dominated by the `ArrayList` used to store the $k$ result elements. No additional data structures scaling with the input size $n$ are utilized.

---

## 3. Component Deep Dive

### Initial Seed Selection
```java
for (int i = 1; i < n; i++) {
    if (Math.abs(x - arr[idx]) > Math.abs(x - arr[i])) {
        idx = i;
    }
}
```
This loop effectively identifies the "center of gravity." By choosing the element with the smallest absolute difference, we ensure we begin expansion from the most optimal entry point. 
*   **Edge Case:** If multiple elements have the same absolute difference, this implementation implicitly selects the *leftmost* one due to the strict inequality (`>`).

### Two-Pointer Expansion
The `while` loop acts as a comparator between candidates `l` (left) and `r` (right).
*   **Priority Rule:** `Math.abs(x - arr[l]) <= Math.abs(x - arr[r])`.
    *   This logic prioritizes the left element in the event of a tie. This is crucial for consistency, as the problem statement typically mandates that smaller values are preferred when distances are equal.
*   **Boundary Handling:** The logic explicitly checks `l >= 0` and `r < n` before accessing array indices, ensuring `ArrayIndexOutOfBoundsException` is avoided when the expansion hits the array limits (e.g., if $x$ is very small or very large).

---

## 4. Key Insights & Engineering Nuances

### 1. Suboptimal Search Pattern
The $O(n)$ scan is the primary bottleneck for large inputs. In a performance-critical system, this should be replaced with a **Binary Search** for the insertion point. Because the array is sorted, we can find the index `idx` such that `arr[idx] >= x` in $O(\log n)$ time, providing a significant speedup for large $n$.

### 2. Tie-Breaking Strategy
The line `if (Math.abs(x - arr[l]) <= Math.abs(x - arr[r]))` is the most important piece of logic. If we were to change this to `<` (strict inequality), the algorithm would favor the right side in ties. Changing this behavior might violate standard problem constraints (which often define the selection preference for equal distances).

### 3. Sorting Post-Processing
The use of `Collections.sort(res)` is a pragmatic trade-off. While the algorithm effectively finds the closest elements, it discovers them in a "closest-to-farthest" order relative to $x$. If performance is extremely tight, the list could be populated by tracking the `min` and `max` indices and returning a sub-list or copy, which would eliminate the $O(k \log k)$ sort entirely.

### 4. Subtle Bugs to Watch
*   **Integer Overflow:** While `Math.abs` is used, if `x` or `arr[i]` were `Integer.MIN_VALUE`, `Math.abs` would return a negative number due to two's complement overflow. In production, consider using `long` for distance calculations if the input range includes boundary integers.
*   **Input Requirements:** This implementation assumes the input `arr` is already sorted. If the input is not sorted, the two-pointer expansion logic will fail to produce the correct set of elements. Always assert sorted input in a pre-condition check.

---

## standard_two_pointer(sliding_window).java
*Style: detailed*

# Deep-Dive Reference: $K$ Closest Elements

## Summary
The provided solution utilizes a **two-pointer shrinking strategy** to isolate the $k$ elements closest to $x$ within a sorted array. Since the array is sorted, the elements closest to $x$ must form a contiguous subarray. The algorithm treats the problem as a "removal" task: start with the entire range $[0, n-1]$ and iteratively discard the element that is furthest from $x$ until exactly $k$ elements remain. By comparing the distance of the boundaries (`arr[l]` and `arr[r]`) to $x$, we greedily reduce the window size.

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** Let $n$ be the length of the array. The `while` loop runs exactly $n - k$ times. In each iteration, the search window size decreases by 1. Since $k$ is a constant relative to $n$ in the worst case (where $k \approx 0$ or $k \approx n$), the loop performs $O(n)$ operations. The subsequent `for` loop to build the result list takes $O(k)$ time. Thus, the total complexity is $O(n - k + k) = O(n)$.
*   *Note:* While $O(\log n + k)$ is possible using binary search to find the starting index, this $O(n)$ two-pointer approach is often faster in practice for smaller $n$ due to lower constant overhead and better cache locality.

### Space Complexity: $O(1)$ (excluding output)
*   **Derivation:** The algorithm uses only a constant amount of extra space (`l`, `r` pointers). The space required for the return `List<Integer>` is $O(k)$, which is typically excluded from auxiliary space complexity analysis.

## Component Deep Dive

### 1. The Shrinking Condition: `r - l >= k`
This condition ensures the loop terminates precisely when the window contains $k$ elements. Specifically, when `r - l + 1 == k`, the loop terminates, leaving the indices `l` and `r` as the bounds of the closest subarray.

### 2. The Decision Logic: `Math.abs(x - arr[l]) <= Math.abs(x - arr[r])`
This is the core greedy heuristic:
*   **Tie-breaking:** The use of `<=` is critical. If the distance from $x$ to both endpoints is equal, the problem statement (standard LeetCode convention) dictates that the smaller element is preferred. Since the array is sorted, `arr[l]` is always smaller than `arr[r]`. Therefore, by removing `r` when distances are equal, we preserve the smaller element (`arr[l]`), satisfying the tie-breaking requirement.

### 3. Edge-Case Handling
*   **$k = n$:** The loop condition `r - l >= k` (where `r - l` would be `n - 1`) will be `(n - 1) >= n`, which is false. The loop never executes, and the entire array is returned correctly.
*   **$x$ outside array range:** If $x$ is smaller than `arr[0]`, the `else` block (`l++`) will execute until `l` reaches `0` and `r` shrinks to `k-1`. If $x$ is larger than `arr[n-1]`, the `if` block (`r--`) will execute until the window sits at the end of the array. The logic handles these boundary conditions naturally without explicit checks.

## Key Insights

### Performance Optimization Nuances
*   **Efficiency vs. Algorithmic Complexity:** While binary search ($O(\log n + k)$) is asymptotically superior, it involves significant branching and index calculation complexity. This $O(n)$ two-pointer approach is a "tight" loop, making it highly efficient for arrays that fit in cache.
*   **ArrayList Allocation:** If $k$ is known to be large, initializing the `ArrayList` with `new ArrayList<>(k)` prevents internal array resizing (copying), which is a common performance bottleneck in Java collection usage.

### Subtle Considerations
*   **Integer Overflow:** The current implementation uses `Math.abs(x - arr[l])`. If `x` and `arr[l]` are near `Integer.MAX_VALUE` or `Integer.MIN_VALUE`, subtraction can overflow. While usually safe in competitive programming constraints, in production systems, one should consider using `long` for the subtraction or `Integer.compare` logic to avoid overflow risks.
*   **The "Gap" Logic:** This solution relies on the property that the window is always contiguous. If the input array were not sorted, this approach would fail, and we would need a Min-Heap or QuickSelect strategy ($O(n)$ average, $O(n \log k)$ heap-based) to maintain the $k$ closest elements.

---
