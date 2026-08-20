# contains-duplicate-ii

## standard_sliding_window.java
*Style: detailed*

# Technical Deep-Dive: Sliding Window Duplicate Detection

## Summary
The provided solution addresses the "Contains Duplicate II" problem by employing a **Fixed-Size Sliding Window** strategy constrained by index distance `k`. Instead of a brute-force $O(n^2)$ search or a global hash map, the algorithm maintains a `HashSet` that acts as a sliding buffer. The set contains only elements within the indices `[right - k, right]`. If at any point the incoming element `nums[right]` already exists in the set, a duplicate within the permissible range is confirmed.

---

## Complexity Analysis

### Time Complexity: $O(n)$
*   **Derivation:** The algorithm uses a single pass over the array (`right` pointer moves from $0$ to $n-1$). 
*   **Operations:** For each element, we perform `HashSet` operations (`add`, `remove`, `contains`). These are $O(1)$ on average.
*   **Window Maintenance:** Although there is a `while` loop, the `left` pointer only ever moves from $0$ to $n$ across the entire lifecycle of the function. Therefore, the inner removal operations are amortized $O(1)$ per iteration.

### Space Complexity: $O(\min(n, k))$
*   **Derivation:** The `HashSet` stores at most $k$ elements at any given time (plus the current element being checked). 
*   **Constraints:** If $k \ge n$, the set will grow to at most size $n$. If $k < n$, the set size is strictly bounded by $k+1$.

---

## Component Deep Dive

### 1. The Sliding Window Logic
The `while (right - left > k)` block is the engine of the constraint enforcement. By checking the difference between pointers, we ensure that the window size never exceeds the distance requirement.
*   **Boundary Condition:** The window size is effectively `k`. When `right - left == k`, there are exactly `k` elements between `left` and `right`, satisfying the problem constraint that the absolute difference between indices `i` and `j` must be $\le k$.

### 2. HashSet Lifecycle
*   **Insertion:** `window.add(nums[right])` happens after the duplicate check. This prevents false positives when the window size is 1 (unless the same element appears twice).
*   **Removal:** `window.remove(nums[left])` utilizes the hash set’s average $O(1)$ removal performance. This is critical because searching for an element to remove in a `List` would have degraded performance to $O(k)$ per step, leading to $O(n \cdot k)$ overall.

### 3. Edge-Case Handling
*   **`k = 0`:** The loop `right - left > 0` will trigger immediately for every index. Since `nums[right]` will never be found in the set (it was removed in the previous `while` iteration), the function correctly returns `false` (as indices must be distinct).
*   **`nums.length < 2`:** The loop logic handles this gracefully; the `contains` check will never return `true`, correctly returning `false`.
*   **Large `k`:** The logic remains correct even if `k` exceeds the array length, as the `while` loop condition will simply never be met, allowing the set to populate normally until a duplicate is found.

---

## Key Insights

### Performance Optimization
*   **HashSet vs. HashMap:** While some variations of this problem use `HashMap<Integer, Integer>` to store the *last seen index* of a value, the `HashSet` approach is more memory-efficient here because we do not need to store the index—only the existence of the value within the window is required.
*   **Load Factor:** If the dataset is known to be extremely large and performance-critical, initializing the `HashSet` with an initial capacity of `k + 1` can reduce the overhead of re-hashing as the set grows.

### Subtle Considerations
*   **Hash Collisions:** In Java, `HashSet` performance is dependent on the `hashCode()` implementation of the `Integer` wrapper. For standard integer arrays, this is perfectly performant. If using custom objects, ensure `hashCode` and `equals` are implemented to prevent $O(n)$ worst-case lookups.
*   **Early Return:** The return statement is placed *before* the current `right` element is added to the set. This is a critical pattern—it ensures that we are looking for a duplicate *previously* stored in the window, preventing a collision with the element we are currently processing.

---
