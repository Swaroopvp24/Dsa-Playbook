# find-k-closest-elements

## sorting_(custom_comparator).java
*Style: detailed*

# Technical Reference: K-Closest Elements via Custom Comparator

## Summary
The provided solution employs a **sorting-based heuristic approach** to identify the $k$ closest elements to a target $x$. The algorithm treats distance as the primary sort key and the numerical value as a tie-breaker. By projecting the input array into a `List`, applying a custom `Comparator`, and performing a partial sort, the solution identifies the optimal subset. Finally, the subset is re-sorted to satisfy the problem's requirement for ascending output order.

## Complexity Analysis

### Time Complexity: $O(N \log N)$
*   **Initial Sorting:** Sorting the entire list of size $N$ using `numbers.sort()` invokes Timsort (in Java), which is $O(N \log N)$.
*   **Sub-list creation:** Slicing the first $k$ elements is $O(k)$.
*   **Secondary Sorting:** Sorting the final $k$ elements takes $O(k \log k)$.
*   **Total:** $O(N \log N + k \log k)$, which simplifies to **$O(N \log N)$** as $k \leq N$.

### Space Complexity: $O(N)$
*   **Storage:** The algorithm allocates a new `ArrayList` to store the $N$ integers from the input array.
*   **Auxiliary Space:** Timsort's stack/merge space is $O(N)$ in the worst case.
*   **Resultant List:** The final list consumes $O(k)$ space.
*   **Total:** **$O(N)$**, primarily due to the creation of the `ArrayList` wrapper.

---

## Component Deep Dive

### 1. The Comparator Logic
The core of the selection strategy lies in the custom lambda:
```java
int firstDistance = Math.abs(first - x);
int secondDistance = Math.abs(second - x);
if (firstDistance != secondDistance) return firstDistance - secondDistance;
return first - second;
```
*   **Distance Calculation:** Using `Math.abs` is safe within integer bounds provided the input values don't overflow the difference.
*   **Tie-breaking:** The constraint "smaller number first" when distances are equal is handled by the `first - second` return statement. This ensures deterministic behavior when $|a - x| = |b - x|$.

### 2. Post-Selection Requirement
The problem requires the output to be in **ascending order**. Since the primary sort was based on proximity to $x$, the initial `sort()` operation effectively scrambles the original relative ordering of the elements. Therefore, a secondary `Collections.sort()` is mandatory to satisfy the contract.

---

## Key Insights & Optimization Nuances

### 1. Efficiency Trap: Binary Search vs. Sorting
While this $O(N \log N)$ approach is readable and correct, it is suboptimal for large datasets. Because the input array is typically sorted (or can be treated as a range), the problem can be solved in **$O(\log(N-k) + k)$** time using binary search. 
*   **The Logic:** Instead of sorting, we can find the starting index of the window of size $k$ using binary search on the array, identifying the optimal "left" boundary where `arr[mid] < x - arr[mid + k]`.

### 2. Performance Nuance: Boxing Overhead
*   The use of `List<Integer>` forces **auto-boxing** of `int` primitives into `Integer` objects. For an array of size $N$, this incurs significant memory overhead due to object headers (16-24 bytes per element) and impacts cache locality compared to an `int[]` primitive array.

### 3. Edge-Case Handling
*   **$k = N$:** The algorithm correctly returns the entire array (re-sorted).
*   **$k = 0$:** Returns an empty list.
*   **Negative Numbers:** The logic holds true for negative integers as `Math.abs` handles the range correctly, and the primary comparison is based on distance, not magnitude.

### 4. Subtle Bug Risk
*   **Integer Overflow:** If `first` or `x` are near `Integer.MIN_VALUE` or `Integer.MAX_VALUE`, `Math.abs(first - x)` could potentially overflow. In a production environment, this should be cast to `long` before calculation to ensure stability: `(long)first - x`.

---

## standard_two_pointer(linear_Search).java
*Style: detailed*

# Technical Deep Dive: K-Closest Elements

## Summary
The provided solution employs a **"Find-and-Expand" two-pointer strategy**. It identifies the index of the element closest to the target $x$ using a linear search, then performs a bidirectional expansion to collect the $k$ closest neighbors. The algorithm prioritizes elements by their absolute distance to $x$, with a tie-breaking rule that favors smaller values (the left neighbor). Finally, it reconciles the collection order by sorting the resulting subset.

## Complexity Analysis

### Time Complexity: $O(N + k \log k)$
*   **Initial Scan:** $O(N)$ to locate the starting `closestIndex`.
*   **Expansion:** $O(k)$ to traverse and select $k$ elements via the `while` loop.
*   **Sorting:** $O(k \log k)$ to sort the resulting `closestElements` list.
*   **Total:** $O(N + k \log k)$. While $O(N)$ is acceptable, the $O(k \log k)$ post-processing sort makes this less efficient than an optimal $O(\log N + k)$ binary search approach.

### Space Complexity: $O(k)$
*   The space requirement is dominated by the storage of the resulting `closestElements` list, which requires $O(k)$ space. The auxiliary space used for pointers and scalar variables is $O(1)$.

## Component Deep Dive

### 1. Initial Pivot Selection
The algorithm begins with a linear scan to find the "anchor" point.
*   **Logic:** `Math.abs(x - arr[closestIndex]) > Math.abs(x - arr[i])`
*   **Edge Case:** If the array contains duplicate values or multiple values with equal distance to $x$, the current logic finds the *first* occurrence of the minimum distance index.

### 2. Bidirectional Expansion
The `while` loop utilizes two pointers (`leftIndex`, `rightIndex`) to act as a sliding window expansion.
*   **Boundary Handling:** The logic explicitly checks if `leftIndex` or `rightIndex` have crossed array bounds. This ensures no `ArrayIndexOutOfBoundsException` occurs.
*   **Tie-Breaking:** The condition `Math.abs(x - arr[leftIndex]) <= Math.abs(x - arr[rightIndex])` is critical. By using `<=`, it ensures that if distances are equal, the `leftIndex` (the smaller value) is favored. This satisfies the problem requirement that in case of ties, smaller values are preferred.

### 3. Post-Processing
Since elements are added to the list as they are discovered (which might jump back and forth across the pivot), the resulting list is not necessarily sorted. The `Collections.sort(closestElements)` call enforces the required ascending output order.

## Key Insights & Optimization Nuances

*   **Algorithmic Efficiency:** The current solution is $O(N)$. However, because the input array is sorted (as implied by the nature of "closest elements" problems), a **Binary Search** could locate the pivot in $O(\log N)$. Furthermore, instead of expanding, one could use a **Sliding Window** over the sorted array to find the $k$-length subarray in $O(\log(N-k) + k)$ time, completely avoiding the need for a final $O(k \log k)$ sort.
*   **Tie-Breaking Nuance:** The logic `Math.abs(x - arr[leftIndex]) <= Math.abs(x - arr[rightIndex])` is the "golden rule" of this implementation. If this were strictly `<` (exclusive of equality), the algorithm would fail the requirement to favor smaller values during ties.
*   **Potential Bug/Redundancy:** 
    *   The initial scan is $O(N)$. If the array is already sorted, the linear search for the closest element is an unnecessary bottleneck compared to `Arrays.binarySearch` or a manual binary search implementation. 
    *   If $k = N$, the algorithm performs $O(N)$ work to build the list and then $O(N \log N)$ to sort it, whereas returning the original array would be $O(1)$ (or $O(N)$ for a copy).
*   **Memory Efficiency:** For very large $k$, returning a view or using a deque to append/prepend could potentially avoid the need for the final `Collections.sort` if the logic was adjusted to ensure order preservation during insertion.

---
