# reverse-vowels-of-a-string

## attempt_1.java
*Style: detailed*

# Technical Deep Dive: Vowel Reversal Algorithm

## Summary
The solution employs a **Two-Pointer Technique** to reverse the vowels within a string in-place. By utilizing two pointers (`st` at the head and `en` at the tail), the algorithm effectively performs a bidirectional scan toward the center of the character array. This approach avoids the overhead of creating auxiliary data structures (like stacks or lists) to store vowel indices, achieving optimal memory efficiency by performing the swaps directly on the mutable character array representation of the input string.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Reasoning:** The algorithm performs a single pass over the string. Even though there are nested `while` loops, each pointer (`st` and `en`) travels across the array exactly once. Each character is visited at most once to check its vowel status. Therefore, the total number of operations is linear with respect to the length of the string, $N$.

### Space Complexity: $O(N)$
*   **Reasoning:** In Java, strings are immutable. The conversion `s.toCharArray()` creates a new character array of size $N$ to facilitate in-place swapping. The final `new String(chac)` constructor creates a second string object of size $N$. Thus, the auxiliary space complexity is $O(N)$.

---

## Component Deep Dive

### 1. `isVowel(char c)`
*   **Mechanism:** Uses `String.indexOf()` against a literal string `"aeiouAEIOU"`.
*   **Performance Note:** While $O(1)$ (as the string length is constant 10), this is slightly slower than a bitmask or a boolean lookup table (like `boolean[256]`). However, for a small set of characters, the JIT compiler optimizes this lookup effectively.

### 2. Two-Pointer Traversal
*   **Inner Pointer Logic:** 
    *   The `while` loops move pointers independently: `st` advances until a vowel is found or the pointer exceeds bounds; `en` regresses until a vowel is found or the pointer goes negative.
    *   **Boundary Safety:** The conditions `st < chac.length` and `en >= 0` are critical. While the outer `st < en` check logically constrains the inner loops, these explicit checks prevent `ArrayIndexOutOfBoundsException` in scenarios involving empty strings or strings containing no vowels.

### 3. Swapping Mechanism
*   The swap is performed using a standard XOR-less temporary variable swap (`char t = ...`).
*   **Pointer Advancement:** The code uses `st++` and `en--` *inside* the assignment expression to update the pointers immediately after the swap, ensuring the inner loops do not process the same vowels twice in the next iteration.

---

## Key Insights

### Performance Nuance: Branch Prediction
The use of `indexOf` creates conditional branching. If the input string has a high density of vowels, the branching is balanced. If the string is primarily consonants, the pointer incrementing loops will dominate the CPU cycles. In extreme scenarios (e.g., millions of characters), replacing `isVowel` with a pre-computed lookup table `boolean[]` is a standard optimization to eliminate method call overhead and string scanning.

### Subtle Edge Cases
*   **Empty Strings:** If `s` is `""`, `chac.length` is 0. The outer `while (st < en)` condition will be false immediately, returning an empty string. The code handles this gracefully.
*   **No Vowels:** If the string contains no vowels, `st` will increment to `chac.length` and `en` will decrement to `-1`. The `st < en` check inside the loop will prevent any operations, returning the original string.
*   **Single Character Strings:** The loop condition `st < en` correctly identifies that no swaps are possible for length 1, returning the input as expected.

### Potential Improvements
*   **Memory Efficiency:** If the input were a `char[]` rather than a `String`, we could achieve true $O(1)$ space complexity. Given Java's `String` immutability, $O(N)$ is the theoretical floor.
*   **Lookup Table:** Replacing `isVowel` with:
    ```java
    private static final boolean[] VOWELS = new boolean[256];
    static {
        for (char c : "aeiouAEIOU".toCharArray()) VOWELS[c] = true;
    }
    ```
    This would offer faster constant-time lookups than `String.indexOf` by avoiding scanning the reference string repeatedly.

---
