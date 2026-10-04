# isomorphic-strings

## hashmap_solution.java
*Style: detailed*

# Technical Deep-Dive: Isomorphic String Validation

## Summary
The solution implements a **bi-directional injective mapping strategy** to determine string isomorphism. Two strings are isomorphic if the characters in $s$ can be replaced to get $t$, where every occurrence of a character must be replaced with another character while preserving the order. The algorithmic technique ensures a "one-to-one" mapping (bijection) constraint:
1. No two characters may map to the same character.
2. A character may map to itself.
3. No two characters may map to the same target character (precluded by the reverse map).

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Derivation:** We perform a single linear pass over the input strings of length $N$. Inside the loop, `HashMap` operations (`containsKey`, `get`, `put`) operate in $O(1)$ amortized time.
*   **Note:** While `HashMap` has a worst-case $O(N)$ for high collision rates, the character set size (e.g., ASCII or Unicode) is typically fixed, making these operations effectively $O(1)$.

### Space Complexity: $O(K)$
*   **Derivation:** $K$ represents the size of the character set (alphabet size). We maintain two hash maps storing up to $K$ mappings.
*   **Constraints:** Since the number of unique characters is bounded (e.g., 256 for extended ASCII), the space complexity is technically $O(1)$ constant space relative to input string length, but scales with the alphabet size.

## Component Deep Dive

### Mapping Logic
The core mechanism relies on maintaining two distinct mappings:
*   `sourceToTarget`: Tracks the functional mapping $f(s) = t$. This prevents the violation where one source character maps to multiple target characters.
*   `targetToSource`: Tracks the inverse mapping $f^{-1}(t) = s$. This prevents the violation where multiple source characters map to the same target character (violating the injective property).

### Edge-Case Handling
*   **Length Mismatch:** Though not explicitly handled by an `if` statement at the start, the implicit assumption in the loop `i < s.length()` assumes `s.length() == t.length()`. In a production environment, an input validation check `if (s.length() != t.length()) return false;` should be added to prevent `StringIndexOutOfBoundsException`.
*   **Empty Strings:** The code handles empty strings gracefully; the loop simply never executes, and the method returns `true`, which is mathematically correct (the empty set is isomorphic to itself).
*   **Character Overlap:** The logic handles cases where `sourceChar == targetChar` and cross-mapping correctly without interference.

## Key Insights

### Performance Optimization: Arrays over HashMaps
While `HashMap<Character, Character>` is idiomatic, it incurs significant overhead due to object boxing (`char` to `Character`) and memory allocation for `Map.Entry` objects.
*   **Optimization:** If the character set is limited (e.g., ASCII), replace the maps with `int[256]` arrays.
*   **Array approach:** `int[] mapS = new int[256]` stores the index mapping. By initializing with `-1`, you can avoid `containsKey` checks, significantly reducing pressure on the Garbage Collector and improving cache locality.

### Subtle Logical Nuance
The dual-map approach is necessary because a simple single-map approach fails when one-to-many mappings are introduced in the inverse direction.
*   *Example:* `s = "ab", t = "aa"`.
    *   `sToT`: {'a': 'a', 'b': 'a'}
    *   `tToS`: {'a': 'b'} (when processing 'b'->'a', the check detects 'a' is already mapped to 'a', triggering `return false`).
*   **Warning:** Failure to check both directions will result in false positives for cases where multiple keys map to the same value.

### Thread Safety
The current implementation is **thread-safe for local execution** as the maps are scoped to the method stack. However, if these maps were converted to static class members, the implementation would require `ConcurrentHashMap` or external synchronization. Given the current design, this is highly efficient for concurrent request processing.

---

## constant_space_optimized.java
*Style: detailed*

# Engineering Deep-Dive: Isomorphic Strings Implementation

## Summary
The solution implements a **Bijective Mapping** strategy to determine if two strings are isomorphic. Two strings are isomorphic if the characters in string `s` can be replaced to get string `t`, maintaining a strict one-to-one correspondence where no two characters in `s` map to the same character in `t`, and vice versa. 

The algorithm utilizes a dual-array lookup table (acting as a fixed-size hash map) to enforce this bijection in a single pass. By maintaining mappings in both directions (`s` to `t` and `t` to `s`), we satisfy the requirement of injective mapping from both domains.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Analysis:** We perform a single linear traversal of the strings. Given $N$ is the length of the string, each character access, comparison, and array write operation occurs in $O(1)$ constant time.
*   **Note:** The constant factor is minimal because array lookups are cache-friendly compared to `HashMap<Character, Character>` overhead.

### Space Complexity: $O(K)$
*   **Analysis:** The space complexity is $O(K)$, where $K$ is the character set size (in this case, $K=256$ for Extended ASCII). 
*   **Justification:** Since the array size is fixed at 256 regardless of the input string length $N$, the space complexity is technically $O(1)$ in terms of asymptotic growth relative to $N$. However, it scales with the alphabet size.

---

## Component Deep Dive

### 1. The Dual-Array Mapping Strategy
The use of two separate integer arrays (`sourceToTarget` and `targetToSource`) is a classic technique to enforce a **Bijective Function**. 
*   **Conflict Detection:** 
    *   If `sourceToTarget[sourceChar]` already holds a value, we must verify that the existing value matches the current `targetChar`. Any discrepancy implies a violation of the "one-to-one" rule.
    *   Symmetrical checks in `targetToSource` prevent "many-to-one" mappings (e.g., two different source characters mapping to the same target).

### 2. The Zero-Initialization Nuance
*   **Implicit Handling:** The arrays are initialized to `0`. In Java, this is safe because the character set effectively uses values 1–255. 
*   **The Trap:** If the input alphabet included the null character `\0` (ASCII 0), this logic would fail as `0` would be indistinguishable from "no mapping yet." In an production environment with wider character support (e.g., Unicode), one should use an `int` array initialized to `-1` or a `BitSet` to track visited indices.

### 3. Edge Case Handling
*   **String Length:** The logic implicitly assumes `s.length() == t.length()`. If strings were of unequal lengths, the loop would throw an `ArrayIndexOutOfBoundsException` or `StringIndexOutOfBoundsException`. (In the context of the problem constraints, it is assumed they are equal).
*   **Empty Strings:** If both strings are empty, the loop is skipped, returning `true`. This is mathematically consistent (a null function is a valid bijection).

---

## Key Insights

*   **Performance Optimization:** Using `int[]` instead of `HashMap<Character, Character>` significantly improves performance by avoiding **auto-boxing** (primitive `char` to `Character` object) and **hash collision overhead**. Array access by index is a direct memory address calculation, resulting in significantly higher cache locality.
*   **Subtle Bug Warning:** This implementation assumes the standard ASCII character set (0-255). If the input were to contain full Unicode (e.g., emojis or extended Asian scripts), the `new int[256]` would trigger an `ArrayIndexOutOfBoundsException`. To harden this for production, one would either replace the arrays with `Map<Character, Character>` or use a larger primitive array (`new int[65536]` for Basic Multilingual Plane).
*   **Memory Efficiency:** By allocating the 256-integer array on the stack (implicitly) or heap, we utilize exactly $256 \times 4 \times 2 = 2048$ bytes of memory. This constant memory footprint is optimal for high-throughput systems where GC pressure must be minimized.

---
