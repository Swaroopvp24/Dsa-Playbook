# combinations

## attempt_1.java
*Style: detailed*

# Technical Reference: Recursive Combinatorial Generation

## Summary
The provided implementation solves the $C(n, k)$ problem using **Backtracking** (a depth-first search approach on the state-space tree). The algorithm explores the selection of $k$ integers from the range $[1, n]$ by maintaining a mutable state list (`ds`), adding elements, recursing deeper, and then performing a "backtrack" step by removing the last element. This ensures the exploration of all valid combinations without redundant permutations, maintaining an implicit ordering that guarantees uniqueness.

---

## Complexity Analysis

### Time Complexity: $O(k \cdot \binom{n}{k})$
*   **Derivation:** The number of combinations is exactly $\binom{n}{k}$. For each valid combination, the algorithm performs a list copy operation `new ArrayList<>(ds)` to store the result, which takes $O(k)$ time.
*   **Bound:** Since the search tree nodes are proportional to $\binom{n}{k} \cdot k$, the time complexity is tightly coupled to the size of the output space.

### Space Complexity: $O(k)$
*   **Recursion Stack:** The maximum depth of the recursion tree is $k$. Each frame stores local variables and the state pointer, contributing $O(k)$ to the stack depth.
*   **Auxiliary Space:** The `ds` list holds at most $k$ elements at any time. 
*   **Note:** We exclude the output space ($O(k \cdot \binom{n}{k})$) from the internal algorithmic space complexity, as it is required by the problem contract.

---

## Component Deep Dive

### 1. Backtracking State Management
The algorithm uses a single mutable `ArrayList<Integer> ds` (Data Structure) to track the current path. 
*   **Push/Pop Pattern:** `ds.add(i)` followed by `ds.remove(ds.size() - 1)` ensures that the reference to the list is constant, avoiding expensive object allocation during the tree traversal.
*   **Defensive Copying:** `result.add(new ArrayList<>(ds))` is critical. Because `ds` is reused across the entire search, failing to create a new instance would result in a list of empty references or corrupted data once the backtrack logic clears the elements.

### 2. State-Space Pruning & Branching
The loop `for (int i = index; i <= n; i++)` handles the selection of the next element.
*   **`index` usage:** By passing `i + 1` into the recursive call, the algorithm enforces a strictly increasing sequence, which is the mathematical requirement for combinations (where order $\{1, 2\}$ is identical to $\{2, 1\}$).
*   **Termination:** The base case `ds.size() == k` terminates the branch early, effectively pruning any path that has already satisfied the combination requirements.

---

## Key Insights & Optimization Nuances

### 1. Pruning Opportunity (The "Mathematical Bound")
The current implementation performs unnecessary iterations. If the number of remaining elements in the range $[i, n]$ is less than the number of elements needed to fill `ds` (i.e., `k - ds.size()`), the loop should break. 
*   **Optimization:** Change the loop condition to `for (int i = index; i <= n - (k - ds.size()) + 1; i++)`. 
*   **Impact:** This prevents the algorithm from entering recursive branches that are mathematically guaranteed to fail, significantly improving performance for cases where $k$ is close to $n$.

### 2. Stack Overhead vs. Heap Allocation
While using a single `List` is memory efficient, Java's `ArrayList` resizing can trigger occasional internal array copies. For strict performance requirements, replacing `ArrayList` with a fixed-size integer array `int[] path = new int[k]` would eliminate object-based overhead and garbage collection pressure, though it would require manual management of the `depth` pointer.

### 3. Edge-Case Vulnerabilities
*   **$k > n$:** The code implicitly handles this; the loop `i <= n` will fail immediately, resulting in an empty list.
*   **$k = 0$:** The base case `ds.size() == 0` will trigger immediately, adding an empty list to `result`, which is standard behavior for $C(n, 0) = 1$.
*   **Concurrency:** The class is **not thread-safe** because it relies on instance-level fields (`result`, `n`, `k`) that are mutated during the `combine` method execution. If this class is used as a singleton in a multi-threaded environment, `combine` must be synchronized or the state must be moved to method-local variables.

---
