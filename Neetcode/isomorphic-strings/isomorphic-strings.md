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
