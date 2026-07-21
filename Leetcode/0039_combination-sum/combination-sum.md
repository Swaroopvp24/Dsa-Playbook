# combination-sum

## attempt_1.java
*Style: detailed*

# Technical Deep-Dive: Combination Sum (Backtracking Implementation)

## 1. Summary
The provided solution addresses the **Combination Sum** problem using **Depth-First Search (DFS) with Backtracking**. The algorithm treats the problem as a decision tree where each node represents a state defined by the current candidate index and the remaining `target`. 

At every step, the algorithm branches into two distinct choices:
1. **Include** the current candidate (allowing for unbounded repetition).
2. **Exclude** the current candidate and move to the next index in the array.

This approach ensures an exhaustive search of all possible subsets that sum to `target` without duplicates, leveraging the inherent nature of recursion to manage the state stack.

---

## 2. Complexity Analysis

### Time Complexity: $O(N^{\frac{T}{M} + 1})$
*   **$T$**: The target value.
*   **$M$**: The minimum value among candidates.
*   **$N$**: The number of candidates.
*   **Reasoning**: The recursion depth is constrained by $T/M$ (the maximum number of times the smallest element can fit into the target). At each step, we have at most 2 branches. However, because we reduce the remaining target by at least $M$ in each recursive call, the number of nodes in the decision tree is bounded by the branching factor relative to the target depth. The upper bound reflects the exponential growth associated with generating combinations where repetition is allowed.

### Space Complexity: $O(\frac{T}{M})$
*   **Reasoning**: The space complexity is dictated by the maximum depth of the recursion stack. Since the stack depth correlates to the number of elements in the path (maximum $T/M$), and we store the current combination in an `ArrayList`, the space required is linear with respect to the maximum possible number of elements in a single combination. Note that the output list storage ($O(\text{total solutions})$) is typically excluded from auxiliary space analysis but is non-trivial in practice.

---

## 3. Component Deep Dive

### `comb(int idx, int target, List<Integer> arr, int[] c)`
*   **Decision logic**:
    *   **Base Case (Success)**: `target == 0`. The current `arr` is a valid combination. We create a shallow copy (`new ArrayList<>(arr)`) because the `arr` reference is mutated globally during backtracking.
    *   **Base Case (Failure)**: `target < 0` (overshot) or `idx >= c.length` (exhausted candidates).
*   **State Transition**:
    *   **Inclusion**: `comb(idx, target - c[idx], arr, c)` keeps the same index. This is the crucial step that permits **unbounded reuse** of the current candidate.
    *   **Backtrack**: `arr.remove(arr.size() - 1)` resets the state to backtrack after an inclusion attempt.
    *   **Exclusion**: `comb(idx + 1, target, arr, c)` pivots to the next candidate, effectively closing the door on using the current index further.

### Edge-Case Handling
*   **Empty Candidates**: If `candidates` is empty, the `idx >= c.length` check triggers immediately, returning an empty result set.
*   **Target unreachable**: The logic handles cases where the target cannot be formed by returning without adding to `res`.
*   **Efficiency**: The recursive structure assumes candidates are positive integers. If the array contains zeros, the logic would enter an infinite recursion loop ($T - 0 = T$).

---

## 4. Key Insights

### The "Unbounded" Nuance
The commented-out line `// comb(idx + 1, target - c[idx], arr, c);` is a common pitfall. In a standard "Combination Sum" problem where you can use elements infinitely, you **must** call `comb(idx, ...)` after inclusion to remain at the same index. Pivoting to `idx + 1` prematurely would restrict element reuse to a maximum of one occurrence per candidate, effectively solving a different variation of the problem (e.g., Combination Sum II).

### Performance Optimization: Sorting
While not explicitly implemented in this snippet, **sorting** the `candidates` array at the start (`Arrays.sort(candidates)`) significantly improves performance:
1.  **Early Exit**: Once `target - c[idx] < 0`, you can `break` immediately if the array is sorted, as no subsequent elements will satisfy the target.
2.  **Pruning**: It allows for tighter bounds in the decision tree traversal.

### Memory Allocation
The line `res.add(new ArrayList<>(arr))` is computationally expensive as it performs an $O(k)$ copy operation (where $k$ is the current path length) every time a valid combination is found. In scenarios with a high density of valid combinations, this will become the primary performance bottleneck.

---

## attempt_1_modified.java
*Style: concise*

### Study Notes: Combination Sum

**Overview**
This solution finds all unique combinations of an integer array that sum to a specific target. It uses backtracking to explore a decision tree where each element can be reused an unlimited number of times.

**Key Components**
*   `combinationSum`: Entry point; initializes the result list and triggers the recursive search.
*   `comb`: A backtracking helper that traverses the candidates array. It manages the current path (`arr`) and the remaining `target`.

**Logic & Observations**
*   **Decision Branching:** The recursion explores two distinct choices at each index `idx`:
    1.  Include the current candidate (keeping `idx` the same to allow reuse).
    2.  Skip the current candidate (incrementing `idx` to move to the next).
*   **State Management:** Standard backtracking pattern; the list `arr` is modified before the recursive call and restored (`remove(arr.size() - 1)`) afterward to backtrack effectively.
*   **Efficiency:** Pruning occurs when `target < 0` or when we run out of candidates. If the input array is sorted, an additional check `c[idx] > target` could prune the search space earlier.

---
