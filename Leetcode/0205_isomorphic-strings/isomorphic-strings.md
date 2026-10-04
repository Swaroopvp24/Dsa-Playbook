# isomorphic-strings

## hashmap_solution.java
*Style: detailed*

# Technical Deep-Dive: Isomorphic String Validation

## Summary
The solution implements a **Bi-directional Bijection Mapping** strategy. Two strings $S$ and $T$ are isomorphic if there exists a one-to-one mapping between the characters of $S$ and the characters of $T$ such that every character in $S$ maps to exactly one character in $T$, and vice versa. The algorithm enforces this constraint by maintaining two symmetric HashMaps that track mappings in both directions simultaneously, ensuring that no two distinct characters in $S$ map to the same character in $T$ (and vice versa).

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation**: We iterate through the strings exactly once, where $N$ is the length of the strings.
*   **Map Operations**: `HashMap.containsKey()` and `HashMap.put()` operate in $O(1)$ amortized time.
*   Since we perform a constant number of constant-time operations for each character, the total time complexity scales linearly with the input size.

### Space Complexity: $O(K)$
*   **Derivation**: The space is determined by the size of the character set (the alphabet). 
*   **Constraint**: The algorithm uses two HashMaps. In the worst case, we store every unique character present in the strings. If the input is ASCII/Unicode, this space is bounded by the size of the alphabet ($K$).
*   If we assume a fixed alphabet size (e.g., 256 for extended ASCII), the space complexity is effectively $O(1)$ constant space relative to input length $N$.

---

## Component Deep Dive

### 1. Dual-Map Synchronization
The core logic relies on the requirement that the mapping is a **bijection**. 
*   `sourceToTarget`: Tracks $S \to T$. This prevents a single character in $S$ from mapping to multiple characters in $T$ (e.g., 'a' $\to$ 'b' and 'a' $\to$ 'c').
*   `targetToSource`: Tracks $T \to S$. This prevents multiple characters in $S$ from mapping to a single character in $T$ (e.g., 'a' $\to$ 'x' and 'b' $\to$ 'x'). 

Without the second map, the algorithm would fail to detect "many-to-one" mapping errors.

### 2. Edge Case Handling
*   **Length Mismatch**: While not explicitly checked in the code provided, the problem constraint usually assumes $|S| = |T|$. If $|S| \neq |T|$, the current loop structure would throw an `IndexOutOfBoundsException`. 
*   **Empty Strings**: Returns `true` by default, which is correct (an empty set is trivially isomorphic).
*   **Unicode/Extended ASCII**: By using `HashMap<Character, Character>`, this solution natively supports Unicode, unlike an array-based implementation (e.g., `int[256]`), which would be restricted to standard ASCII.

---

## Key Insights & Optimization Nuances

### 1. The "Frequency Array" Optimization
While `HashMap` is general-purpose, it incurs overhead due to object boxing (`Character` objects) and hashing logic. In a performance-critical system with a known alphabet size (e.g., standard ASCII), replacing the `HashMap` with two primitive integer arrays `int[256]` or `int[128]` would yield significant performance gains:
*   **Memory**: Eliminates object allocation and pointer overhead.
*   **Cache Locality**: Contiguous primitive arrays exhibit better CPU cache performance compared to the linked-node structure of a `HashMap`.

### 2. Failure Path Logic
The algorithm employs a "fail-fast" strategy. By checking `sourceToTarget.containsKey()` *before* attempting the `put()` operation, we prevent dirty state writes. The conditional checks function as guards that prune the search space as soon as an invalid bijection is discovered.

### 3. Subtle Gotcha: State Consistency
A common pitfall in isomorphic implementation is updating only one map. Because the code calls `put` on both maps at every iteration, it maintains a perfect state sync. A more dangerous (and buggy) approach is to check mappings independently rather than checking for violations before commits. This implementation avoids that by enforcing:
1. `S[i]` exists in `sourceToTarget` $\to$ must match `T[i]`.
2. `T[i]` exists in `targetToSource` $\to$ must match `S[i]`.
This structure ensures the injective and surjective requirements of a bijection are satisfied.

---
