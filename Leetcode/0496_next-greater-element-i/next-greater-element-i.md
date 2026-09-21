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

## standard_stack_solution.java
*Style: detailed*

# Deep-Dive Technical Reference: Next Greater Element (Monotonic Stack)

## Summary
The solution employs a **Monotonic Decreasing Stack** combined with a **Hash Map** to solve the "Next Greater Element" problem in $O(N + M)$ time. By traversing `nums2` from right to left, the stack maintains a candidate pool of "greater" elements encountered so far. The monotonic property ensures that the stack top always represents the nearest element to the right that is strictly greater than the current element, allowing for efficient lookups in linear time.

---

## Complexity Analysis

### Time Complexity: $O(N + M)$
*   **$M$ (Length of `nums2`)**: The algorithm performs a single pass over `nums2`. Although there is a `while` loop inside the `for` loop, each element of `nums2` is pushed onto the `decreasingStack` exactly once and popped at most once. This amortized analysis confirms linear time complexity for the stack operations. The `HashMap` operations (put/get) are $O(1)$ on average.
*   **$N$ (Length of `nums1`)**: The final loop iterates through `nums1` once, performing a $O(1)$ hash map lookup for each element.
*   **Total**: $O(N + M)$.

### Space Complexity: $O(M)$
*   **Hash Map**: Stores $M$ key-value pairs representing the next greater element for every unique integer in `nums2`.
*   **Stack**: In the worst-case scenario (a strictly decreasing array `nums2`), the stack will contain all $M$ elements.
*   **Total**: $O(M)$.

---

## Component Deep Dive

### 1. The Monotonic Decreasing Stack
The stack stores elements in decreasing order from bottom to top. 
*   **Invariant**: For any element $x$ pushed onto the stack, all elements smaller than or equal to $x$ currently in the stack are discarded (popped). 
*   **Purpose**: By discarding elements $\leq currentElement$, we ensure that when we peek at the stack, the result is the *nearest* element to the right that is *greater* than the current element. If the stack is empty, it mathematically proves no such element exists to the right.

### 2. Backward Traversal Strategy
Traversing from `nums2.length - 2` down to `0` allows us to define the "Next Greater" relationship relative to the state of the stack after processing the right-hand side. This avoids the $O(M^2)$ brute-force approach, as we only need to look at elements we have already processed.

### 3. Hash Map Decoupling
The `nextGreaterMap` acts as a memoization layer. By precomputing results for the superset (`nums2`), we decouple the logic from `nums1`. This allows us to handle the `nums1` query phase in constant time per element, regardless of how many times the same element might appear in `nums1`.

---

## Key Insights

### Performance Optimization: `ArrayDeque` vs `Stack`
*   The implementation uses `ArrayDeque` as a `Deque` interface rather than the legacy `java.util.Stack` class. The `Stack` class is synchronized and suffers from performance overhead due to its inheritance from `Vector`. `ArrayDeque` is more cache-friendly and faster for stack-based operations in single-threaded contexts.

### Handling Edge Cases
*   **Single Element Arrays**: The code handles this by pre-populating the stack with `nums2[nums2.length - 1]` and then starting the loop at `nums2.length - 2`. If `nums2.length` is 1, the loop never executes, and the map correctly returns -1.
*   **Monotonicity Failure**: If the array is sorted in ascending order (e.g., `[1, 2, 3]`), the `while` loop pops elements until the stack is empty for every iteration. While this appears inefficient, the amortized cost remains $O(M)$.
*   **Duplicate Values**: While the problem constraints usually imply unique elements in `nums2`, the logic holds even with duplicates, as the `while` condition `decreasingStack.peek() <= currentElement` ensures we only keep strictly greater elements.

### Subtle Bugs to Watch For
*   **Null Pointer/Empty Key**: The `HashMap` assumes all elements in `nums1` exist in `nums2`. If the input contract is violated (i.e., `nums1` contains elements not present in `nums2`), `nextGreaterMap.get(nums1[index])` would return `null`, potentially causing an `Unboxing` error or `NullPointerException` if converted to `int` implicitly. In production code, one should add a check or use `getOrDefault`.

---
