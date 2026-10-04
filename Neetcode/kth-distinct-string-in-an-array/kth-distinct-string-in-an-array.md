# kth-distinct-string-in-an-array

## attempt_1.java
*Style: detailed*

# Technical Deep Dive: K-th Distinct String in an Array

## Summary
The solution employs a **two-pass frequency counting strategy** using a Hash Map to identify unique elements while preserving their original relative order. By decoupling the identification of distinct elements (via frequency tracking) from the retrieval process (via linear scan), the algorithm maintains an $O(N)$ time complexity. This approach transforms a potentially $O(N^2)$ brute-force comparison problem into a linear-time lookup problem by leveraging hashing for $O(1)$ average-case frequency verification.

---

## Complexity Analysis

### Time Complexity: $O(N \cdot L)$
*   **Pass 1 (Population):** Iterating through the array takes $O(N)$, where $N$ is the number of elements. Each `HashMap.put` and `getOrDefault` operation incurs an $O(L)$ cost, where $L$ is the average length of the strings (due to hashing the string and equality checks).
*   **Pass 2 (Retrieval):** A second linear traversal of the array takes $O(N)$. Map lookups are $O(L)$.
*   **Total:** $O(N \cdot L)$. Since $L$ is typically treated as a constant in standard constraints, this simplifies to **$O(N)$**.

### Space Complexity: $O(N \cdot L)$
*   **Frequency Map:** In the worst-case scenario where all elements in the input array are unique, the `HashMap` will store $N$ keys. Each key occupies $O(L)$ space for the string itself.
*   **Total:** $O(N \cdot L)$.

---

## Component Deep Dive

### 1. Frequency Map Pattern (`HashMap<String, Integer>`)
The choice of `HashMap` is critical. It serves as an auxiliary data structure to achieve constant-time lookup for the second pass. 
*   **Why not `HashSet`?** A `HashSet` only tracks presence/absence. To track "distinctness," we must differentiate between elements that appear exactly once and those that appear multiple times. The `Integer` value in our map explicitly captures this state.

### 2. Linear Order Retention
A common pitfall in frequency problems is iterating over the map entries directly. If we were to iterate over `frequencyMap.keySet()`, we would lose the original insertion order because `HashMap` does not guarantee order. By performing the second pass over the **original array `arr`**, the algorithm correctly respects the sequence in which elements appear, ensuring the $k$-th distinct element is retrieved in its correct temporal position.

### 3. Edge Case Handling
*   **$k$ exceeds distinct count:** If the `distinctCount` never reaches `k` (e.g., input `["a", "b"], k=3`), the function correctly falls through the loop and returns `""`.
*   **Empty/Null Input:** While not explicitly guarded, if `arr` is empty, the loops are skipped, returning an empty string. If `arr` contains `null` elements, `HashMap` supports one `null` key, but logic may fail if the input expects standard string processing; standard constraints typically assume non-null strings.

---

## Key Insights & Performance Nuances

*   **Load Factor & Hashing:** The `HashMap`'s performance can degrade if the hash function for `String` experiences many collisions. In Java, `String.hashCode()` is well-distributed, but for extremely large datasets with maliciously crafted strings (designed to trigger collisions), the $O(1)$ lookup can degrade to $O(N)$, leading to $O(N^2)$ overall performance.
*   **Early Exit:** The algorithm is optimized for early termination. As soon as `distinctCount == k`, the execution returns, potentially skipping the remainder of the array.
*   **Memory Overhead:** For high-throughput systems, the `HashMap` object overhead (Entry nodes, table array) can be significant. If memory constraints are tight and the input array is small, an alternative might be a custom sorting approach; however, that would sacrifice the $O(N)$ time complexity for $O(N \log N)$.
*   **String Pooling:** In Java, if the input strings are interned or shared, the `HashMap` key lookups are slightly more performant as they benefit from identity-based comparisons before full content-equality checks.

---
