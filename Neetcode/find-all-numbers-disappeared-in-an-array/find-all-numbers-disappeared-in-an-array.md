# find-all-numbers-disappeared-in-an-array

## attempt_1.java
*Style: detailed*

# Engineering Deep Dive: In-Place Frequency Marking for Array Discrepancy

## 1. Summary
The algorithm solves the "Disappeared Numbers" problem by leveraging the input array as a **hash map proxy**. Given that the input contains integers in the range $[1, n]$ where $n$ is the array length, each value $v$ can be mapped bijectively to a zero-based index $v-1$. 

The core technique is **In-Place Signal Encoding**: by using the sign of the value at the target index as a boolean flag, we negate values to represent "visited" status. This eliminates the need for $O(n)$ auxiliary space, transforming the problem from a typical frequency-counting task into a destructive, state-marking traversal.

---

## 2. Complexity Analysis

### Time Complexity: $O(n)$
*   **Traversal 1 (Marking):** We iterate through the array once. Each element is visited exactly once, performing constant-time arithmetic (`Math.abs`) and memory writes.
*   **Traversal 2 (Collection):** We perform a single pass over the modified array to verify the sign of each element. 
*   Total operations $\approx 2n$, which simplifies to **$O(n)$**.

### Space Complexity: $O(1)$
*   **Auxiliary Space:** We utilize only a few primitive variables (`index`, `currentNumber`). 
*   **Output Space:** While the return `List<Integer>` requires $O(k)$ space (where $k$ is the number of missing elements), this is standard for output requirements and does not count against the auxiliary space complexity for the algorithm's internal logic. The input array itself is modified in-place, avoiding the $O(n)$ overhead of a `Set` or `Boolean[]`.

---

## 3. Component Deep Dive

### Marking Mechanism (`nums[index] = -Math.abs(nums[index])`)
The use of `Math.abs` is critical here. Because we negate values iteratively, an element at index $i$ might have already been negated by a previous number. If we simply used `nums[index] = -nums[index]`, a subsequent visit to the same index would flip a negative back to positive, effectively "unmarking" it. Using `Math.abs` ensures that no matter how many times an index is visited, the target value is forced into a negative state, acting as an idempotent "visited" flag.

### The Indexing Logic (`Math.abs(currentNumber) - 1`)
Since the range of numbers is $1$ to $n$, the array indices are $0$ to $n-1$. The `- 1` shift is the standard translation to map one-based values to zero-based memory addressing. 

### Edge Case Handling
*   **Empty Array:** The loops correctly handle `nums.length == 0`, returning an empty list.
*   **No Missing Numbers:** The first pass negates every element; the second pass finds no positive values, correctly returning an empty list.
*   **Duplicates:** The logic gracefully handles duplicates. If a number appears twice, the index is simply marked "visited" twice (the second time being a redundant negation of an already negative number), ensuring the presence of that value is still captured.

---

## 4. Key Insights & Nuances

### Destructive State
This algorithm is **destructive**. It modifies the input array `nums`. In a production environment or a multi-threaded system, this is a significant side effect. If the original data must be preserved, you would require $O(n)$ space or a pre-copying step, which would negate the primary performance benefit.

### Integer Overflow Considerations
*   While `Math.abs` is safe for the constraints of this specific problem ($1 \le nums[i] \le n$), one must be wary of `Integer.MIN_VALUE` in general Java programming. Negating `Integer.MIN_VALUE` does not change the value due to two's complement overflow. However, since the input range is constrained to $n$ (the array length), this is not an issue here.

### Cache Locality
Because the algorithm accesses memory in a pseudo-random fashion (determined by the values inside the array), it can cause **cache misses** compared to a strictly linear scan. However, because we iterate only twice and perform the operation in-place, the reduction in memory footprint often yields better performance in memory-constrained environments than allocating a `HashSet<Integer>`, which carries significant object overhead in the JVM.

---
