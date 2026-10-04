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

## constant_space_optimized.java
*Style: detailed*

# Engineering Deep-Dive: Isomorphic Strings Implementation

## Summary
The solution determines if two strings $S$ and $T$ are isomorphic by enforcing a **bijective (one-to-one) mapping** between characters. To satisfy the isomorphic condition, every character in $S$ must map to exactly one character in $T$, and vice versa (injective property).

The implementation utilizes a **Fixed-Array Direct Addressing Table (DAT)** approach instead of hash-based structures. By leveraging the constrained character set (ASCII 256), the algorithm avoids the overhead of object hashing and collision resolution, achieving optimal constant-time lookups.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Analysis:** We perform a single linear pass over the input strings of length $N$. Inside the loop, all operations (array indexing, comparison, and assignment) are $O(1)$.
*   **Constraint Note:** Given that the input is limited to the ASCII character set, the space usage remains constant regardless of $N$, but the execution time scales linearly with string length.

### Space Complexity: $O(1)$ (Technically $O(\Sigma)$)
*   **Analysis:** The space complexity is defined by the size of the character set ($\Sigma = 256$). Since the array sizes are constant (256 integers each), the memory footprint does not grow with the size of the input string $N$.
*   **Constants:** The implementation uses $2 \times 256 \times 4$ bytes $\approx 2$ KB of stack/heap memory, which is negligible even in high-throughput environments.

---

## Component Deep Dive

### 1. The Dual-Mapping Technique
The core of the logic is the maintenance of two arrays:
*   `sourceToTarget[256]`: Tracks $S[i] \to T[i]$.
*   `targetToSource[256]`: Tracks $T[i] \to S[i]$.

A single mapping is insufficient because it only validates $S \to T$. Without the second mapping, two distinct characters in $S$ could map to the same character in $T$ (e.g., "ab" -> "aa"). The dual mapping ensures a true bijection.

### 2. Zero-Value Ambiguity
*   **Observation:** The code uses `0` as the "uninitialized" sentinel value.
*   **Risk:** If the inputs included the null character (`'\0'`), the current logic would treat a mapping to `'\0'` as an uninitialized state.
*   **Refinement:** In production scenarios involving extended character sets, it is safer to initialize arrays to `-1` or use a `boolean[]` array to track whether an index has been visited, decoupling the initialization state from the character value itself.

---

## Key Insights

### Performance Optimization Nuances
*   **Cache Locality:** Using primitive `int[]` arrays is significantly faster than using `HashMap<Character, Character>`. The arrays fit into L1 cache, and the direct indexing bypasses the `Integer` autoboxing and `hashCode()` calculation overheads associated with collection objects.
*   **Branch Prediction:** The checks (`!= 0`) are highly predictable in strings with repeat patterns, allowing the CPU to pipeline the validation logic efficiently.

### Subtle Bugs & Edge Cases
*   **String Length Mismatch:** The current implementation assumes `s.length() == t.length()`. If inputs are not pre-validated, the code will throw an `ArrayIndexOutOfBoundsException` or `StringIndexOutOfBoundsException` when `t.charAt(i)` is called on a shorter string.
*   **Character Set Expansion:** If the problem requirements were upgraded to support Unicode (e.g., UTF-16), a `256` size array would trigger an overflow. In such a scenario, migrating to a `HashMap<Integer, Integer>` or a two-tiered sparse array would be necessary.
*   **Sentinel Value Collision:** The decision to use `0` as a sentinel works perfectly for standard ASCII (where `0` is `null`), but it is a "lucky" implementation detail. Relying on sentinel values within data range is a common source of logic errors; a separate `boolean[] visited` array is the robust professional alternative.

---
