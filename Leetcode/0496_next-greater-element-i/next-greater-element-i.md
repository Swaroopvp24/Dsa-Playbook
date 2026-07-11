# next-greater-element-i

## attempt_1.java
*Style: concise*

### Next Greater Element
Computes the next greater element for each value in `nums1` based on its relative order in `nums2`. Uses a monotonic decreasing stack to identify the first element to the right that is larger than the current element in $O(N+M)$ time.

#### Key Components
*   **`Stack<Integer> s`**: Stores elements from `nums2` for which we haven't found a "next greater" value yet.
*   **`Map<Integer, Integer> m`**: A lookup table mapping each element in `nums2` to its immediate next greater element.
*   **`for` loop 1**: Builds the mapping by maintaining a decreasing stack; when a larger element is found, it pops smaller stack elements and records the mapping.
*   **`for` loop 2**: Maps the results back to the subset of elements provided in `nums1`.

#### Logic Notes
*   **Monotonic Stack Property**: The stack remains in decreasing order. When `num > s.peek()`, `num` is the *first* (next) greater element for the popped value.
*   **Space-Time Complexity**: $O(N + M)$ time and $O(M)$ space, where $N$ and $M$ are lengths of the arrays.
*   **Efficiency**: Single pass over `nums2` vs. a nested loop approach ($O(N \cdot M)$).

---
