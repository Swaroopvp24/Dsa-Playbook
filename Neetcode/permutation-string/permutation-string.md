# permutation-string

## standard_sliding_window.java
*Style: concise*

### Study Notes: Permutation in String

#### Overview
This solution determines if `s2` contains a permutation of `s1` by using a **sliding window** of fixed size (`s1.length()`) and maintaining frequency counts of characters in both strings. It achieves $O(n)$ time complexity by updating the match status incrementally rather than re-comparing the full frequency array.

#### Key Logic
*   **`targetFreq` / `windowFreq`**: Two integer arrays of size 26 tracking character counts.
*   **`matchingChars`**: An integer state representing how many of the 26 character slots currently have identical counts between the window and `s1`. If this reaches 26, the current window is a permutation of `s1`.

#### Non-Obvious Logic
*   **Incremental Update**: Instead of checking `Arrays.equals()` in every loop iteration, we update `matchingChars` only when a frequency change pushes a count into or out of parity with `targetFreq`.
    *   **Entering Window**: If `count++` makes it equal to target, `matchingChars++`. If `count` becomes `target + 1`, it was previously a match but is now broken, so `matchingChars--`.
    *   **Leaving Window**: If `count--` makes it equal to target, `matchingChars++`. If `count` becomes `target - 1`, the previous match is destroyed, so `matchingChars--`.
*   **Edge Case**: The final `matchingChars == 26` check is required outside the loop to catch a permutation occurring at the very end of `s2`.

---
