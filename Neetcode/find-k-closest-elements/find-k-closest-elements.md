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
