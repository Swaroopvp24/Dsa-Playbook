# three-integer-sum

## standard_two_pointer.java
*Style: detailed*

# Technical Reference: Three-Sum Optimization

## Summary
The solution employs the **Two-Pointer technique** on a pre-sorted array to solve the 3SUM problem in $O(n^2)$ time. By sorting the input, we transform the search for a triplet $(a, b, c)$ such that $a+b+c=0$ into a series of $O(n)$ two-pointer sweeps. The algorithm iterates through each element as a "fixed" pivot and uses the two-pointer approach to find pairs in the remaining subarray that sum to the additive inverse of the pivot. This eliminates the need for a nested loop structure of $O(n^3)$ or the overhead of hash-based lookups, which struggle with duplicate handling.

---

## Complexity Analysis

### Time Complexity: $O(n^2)$
*   **Sorting:** The `Arrays.sort()` call utilizes Dual-Pivot Quicksort, contributing $O(n \log n)$.
*   **The Loop Structure:** We iterate through the array once ($n$ iterations). Inside the loop, the two-pointer sweep processes the remaining array at most once ($n$ steps). 
*   **Total:** $O(n \log n + n^2)$, which simplifies to **$O(n^2)$**. 
*   *Constraint Note:* This complexity is optimal for this problem, as any approach involving sorting is bound by this complexity, and even hash-based approaches typically result in $O(n^2)$ due to the necessity of avoiding duplicate triplets.

### Space Complexity: $O(1)$ or $O(n)$
*   **Auxiliary Space:** Ignoring the space required for the output list, the space complexity is **$O(\log n)$ to $O(n)$** depending on the implementation of `Arrays.sort()` (stack space for Quicksort recursion).
*   **In-place logic:** The two-pointer logic itself uses $O(1)$ extra space, as it only maintains integer pointers.

---

## Component Deep Dive

### 1. Pre-sort Mechanism
Sorting is the foundational requirement. It allows us to:
*   Use the monotonicity of the array to shift pointers intelligently. If `sum < 0`, we know we must increase the sum, which is achieved by incrementing the `left` pointer (moving toward larger values).
*   Easily skip duplicate entries using adjacent comparisons (`nums[i] == nums[i-1]`).

### 2. The Two-Pointer Sweep
Inside the `fixedIndex` iteration, `left` starts at `fixedIndex + 1` and `right` at `nums.length - 1`. 
*   **State Space:** The search space for the two pointers shrinks monotonically as `fixedIndex` advances.
*   **Efficiency:** Because we skip duplicates, we prune branches of the search space that are guaranteed to result in redundant triplets, preventing `result` from containing identical lists.

### 3. Duplicate Handling Logic
The code handles two distinct classes of duplicates:
*   **Fixed Pivot Duplicates:** `if (fixedIndex > 0 && nums[fixedIndex] == nums[fixedIndex - 1]) continue;` 
    *   Ensures that we don't start the two-pointer sweep with the same value twice.
*   **Pointer Duplicates:** The `while` loops nested inside the `sum == 0` block ensure that once a valid triplet is found, both `left` and `right` advance past any identical values. This is critical for keeping the output list unique without requiring a `Set` data structure (which would add significant overhead and memory).

---

## Key Insights

### Avoiding `Set` Overhead
Developers often try to use a `HashSet<List<Integer>>` to handle duplicates. **Avoid this.** Inserting into a `HashSet` involves hashing a `List`, which is expensive and unnecessary. Handling duplicates via pointer arithmetic (as shown in the code) is significantly more performant, keeping the memory footprint minimal and avoiding the overhead of object hashing.

### Subtle Edge Cases
*   **Array Length < 3:** The code handles this naturally; the `fixedIndex` loop will execute, but the `left < right` condition will never be met, returning an empty list as expected.
*   **Integer Overflow:** The current code uses `int` for the sum. If the input array contains values near `Integer.MAX_VALUE` or `Integer.MIN_VALUE`, `sum` could overflow. 
    *   *Remediation:* If input constraints allow for large values, cast to `long` before summation: `long sum = (long)nums[fixedIndex] + nums[left] + nums[right];`
*   **Performance Nuance:** The `while` loops for skipping duplicates are only executed when a valid sum is found. This is an efficient design—the duplicate-skipping logic does not impact the best-case or average-case performance for arrays with unique elements.

---
