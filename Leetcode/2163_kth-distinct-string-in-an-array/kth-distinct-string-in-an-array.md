# kth-distinct-string-in-an-array

## attempt_1.java
*Style: detailed*

# Technical Reference: `kthDistinct` Implementation

## Summary
The solution implements a **two-pass frequency counting algorithm** to identify the $k$-th non-repeating element in a collection while preserving the original insertion order. The algorithm leverages a hash-based lookup table to decouple the frequency evaluation from the sequence traversal. By performing a pass to build a frequency map followed by a pass over the input array, the algorithm ensures $O(1)$ lookup time for cardinality checks, satisfying the requirement to find the $k$-th distinct element in linear time.

## Complexity Analysis

### Time Complexity: $O(N \cdot L)$
*   **$N$**: Number of elements in the input array.
*   **$L$**: Average length of the strings in the array.
*   **Analysis**:
    *   **First Pass**: Iterating through the array once and performing $N$ hash map insertions/updates. Each insertion involves calculating the hash code for a string of length $L$, resulting in $O(N \cdot L)$.
    *   **Second Pass**: Iterating through the array once more. Each lookup in the `HashMap` is $O(L)$ (for equality checks/hashing).
    *   **Total**: $O(N \cdot L + N \cdot L) \approx O(N \cdot L)$.

### Space Complexity: $O(N \cdot L)$
*   **Analysis**: In the worst-case scenario (all strings are distinct), the `HashMap` stores $N$ keys. Each key is a string of length $L$, leading to $O(N \cdot L)$ space consumption to store the dictionary. Auxiliary space for the `distinctCount` counter is $O(1)$.

## Component Deep Dive

### 1. `frequencyMap` (HashMap<String, Integer>)
The choice of `HashMap` is critical. It provides amortized $O(1)$ time complexity for `put` and `get` operations. 
*   **Mechanism**: The map stores the string as the key and its total occurrences as the value. 
*   **Edge Case Handling**: By using `frequencyMap.getOrDefault(str, 0) + 1`, the implementation gracefully handles the initialization of keys that do not yet exist in the map, avoiding explicit `contains` checks.

### 2. Sequential Traversal
The second pass iterates over the **original array** `arr` rather than the `frequencyMap`'s `keySet`. This is intentional and mandatory; a `HashMap` (prior to Java 8, and even with insertion-order `LinkedHashMap`) does not necessarily guarantee the sequence required if we were to delete/re-insert. Iterating over the source `arr` preserves the original index-based order, which is the definition of the "k-th" element in this context.

### 3. Termination Condition
*   **Successful Termination**: Returns the string immediately upon `distinctCount == k`. This optimization prevents unnecessary traversal of the remainder of the array.
*   **Failure Termination**: If the loop finishes without reaching `k`, the function returns `""`. This covers scenarios where the input array contains fewer than `k` distinct elements.

## Key Insights

*   **Order Sensitivity**: While `java.util.HashMap` does not guarantee order, the solution bypasses this limitation by using the `HashMap` solely as a frequency metadata store and utilizing the input `arr` for sequence iteration. This is a common pattern to trade memory for $O(N)$ speed.
*   **Memory Footprint**: If memory constraints were extreme and the string set was small (fixed alphabet size), one could theoretically use a `Trie` with a frequency counter at each leaf. However, for general-purpose strings, the overhead of a `Trie` would likely exceed that of the `HashMap`.
*   **Potential Optimization (Early Exit)**: In languages where the hash map can be inspected for internal consistency, one could potentially combine passes if memory were not an issue, but the two-pass approach is the standard, readable, and highly performant way to solve this in Java.
*   **Hidden Performance Cost**: The cost of `String.hashCode()` and `String.equals()` is proportional to the string length. For extremely long strings, this implementation will perform significantly slower; in such cases, string interning or using a custom hash wrapper could mitigate costs if the set of unique strings is small but their lengths are large.

---
