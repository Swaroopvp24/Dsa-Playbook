# combination-sum-ii

## attempt_1.java
*Style: detailed*

# Technical Reference: Combination Sum II

## Summary
The solution implements a **Backtracking** algorithm to find all unique combinations in a set of candidates that sum to a specific target. Each number in the candidate set may only be used once per combination. 

To handle duplicate elements within the input array while ensuring the output set contains only unique combinations, the algorithm utilizes a **Sort-and-Skip** strategy. By sorting the input array, the algorithm can group identical elements, allowing it to prune redundant recursive branches by checking the relationship between the current element and its predecessor.

---

## Complexity Analysis

### Time Complexity: $O(2^n)$
*   **Sorting:** $O(n \log n)$, where $n$ is the length of `candidates`.
*   **Backtracking:** In the worst case, every element can either be included or excluded. The recursion tree can have a depth of $n$, leading to $O(2^n)$ possible subsets. The pruning mechanism (`i > index && c[i] == c[i-1]`) significantly reduces the search space in practice, but the upper bound remains exponential.
*   **Copying:** Adding a valid `ds` (data structure) to `res` takes $O(k)$, where $k$ is the average length of a combination.

### Space Complexity: $O(n)$
*   **Recursion Stack:** The maximum depth of the recursion tree is $n$, where $n$ is the number of elements in `candidates`.
*   **Auxiliary Storage:** The `ds` list holds at most $n$ elements at any time. The input `candidates` is sorted in place (if using `Arrays.sort`), and the result list `res` is generally not considered part of the auxiliary space complexity for algorithm analysis.

---

## Component Deep Dive

### 1. `Arrays.sort(candidates)`
This is the most critical preprocessing step. Sorting transforms the input into a contiguous block of identical values. This is not only required for the "early exit" optimization (`c[i] > target`) but is the functional foundation for the duplicate-skipping logic.

### 2. The `i > index` Skip Logic
```java
if (i > index && c[i] == c[i - 1]) continue;
```
*   **Mechanism:** When iterating through the loop at a fixed recursive level (`index`), if the current element is the same as the previous one, it indicates that the current branch would explore a subset that was already explored by the previous identical element.
*   **Why `i > index`?** This ensures we only skip duplicates *within the same tree level*. It is valid to have the same number at different recursive levels (e.g., if the input is `[1, 1, 2]`, we *can* use two `1`s if the array contains two `1`s).

### 3. Early Termination (`c[i] > target`)
```java
if (c[i] > target) break;
```
Because the array is sorted, if the current element is greater than the remaining `target`, all subsequent elements in the current loop will also be greater than the `target`. This `break` provides a significant performance boost by pruning branches that are mathematically guaranteed to fail.

---

## Key Insights

### Backtracking Pattern
The implementation uses a classic **"choose, explore, un-choose"** pattern:
1.  **Choose:** `ds.add(c[i])`
2.  **Explore:** `findComb(..., i + 1, ...)` — Note that `i + 1` ensures each element is consumed only once.
3.  **Un-choose:** `ds.remove(ds.size() - 1)` (Backtrack)

### Subtle Edge Case: The "Used Once" Constraint
A common pitfall in Combination Sum problems is forgetting to increment the index passed to the next recursive call. By using `i + 1`, the developer correctly enforces that an element at a specific index cannot be reused within the same combination path, satisfying the problem constraint that each number may only be used once per combination.

### Optimization Nuance
While this is a clean implementation, passing `ds` as a reference is standard. In memory-constrained environments, one might consider using an `int[]` array to track counts of candidates rather than an `ArrayList` to reduce heap allocations for intermediate objects, though that would sacrifice the ease of backtracking provided by the `ArrayList.remove()` method.

---
