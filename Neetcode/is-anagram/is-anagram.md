# is-anagram

## attempt_1.java
*Style: concise*

### Anagram Validator

**Overview**
Determines if two strings are anagrams by comparing character frequency counts. It runs in $O(n)$ time and $O(1)$ space (fixed array of 26).

**Key Components**
*   `freq` array: An integer array of size 26 used as a hash map to track the net difference in character counts between `s` and `t`.

**Logic Notes**
*   **Early Exit:** Immediately returns `false` if string lengths differ, avoiding unnecessary processing.
*   **Single-Pass Counter:** Increments indices for characters in `s` and decrements for `t` within a single loop; if the strings are anagrams, all values in `freq` must return to zero.
*   **Index Mapping:** Uses `charAt(i) - 'a'` to map ASCII characters directly to array indices (0-25).

---
