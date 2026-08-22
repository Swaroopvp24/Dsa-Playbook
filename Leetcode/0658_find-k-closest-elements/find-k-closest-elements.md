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
