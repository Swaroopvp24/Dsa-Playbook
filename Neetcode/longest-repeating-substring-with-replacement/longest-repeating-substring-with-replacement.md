# longest-repeating-substring-with-replacement

## standard_two_pointer.java
*Style: detailed*

# Deep-Dive: Longest Repeating Character Replacement

## Summary
The provided solution utilizes a **Sliding Window** technique combined with an exhaustive search over the character set. Instead of attempting to track all character frequencies simultaneously (as in the optimal $O(N)$ solution), this approach iterates through every unique character present in the string, treating each as the potential "target" character that will populate the final repeating sequence.

For each target character, the algorithm maintains a window $[left, right]$ where the number of non-target characters (calculated as `(windowSize - targetCharacterCount)`) does not exceed $k$. If the budget $k$ is exceeded, the window is contracted from the left.

---

## Complexity Analysis

### Time Complexity: $O(A \cdot N)$
*   **$N$**: The length of the string `s`.
*   **$A$**: The size of the alphabet (number of unique characters).
*   **Why**: We perform an outer loop over the unique characters ($A$). Inside, we execute a standard sliding window over the entire string ($N$). Since the sliding window pointer operations (`right` and `left`) are amortized $O(1)$ per iteration, the total complexity is $O(A \cdot N)$. If the alphabet size is constant (e.g., 26 for English uppercase), this effectively functions as $O(N)$.

### Space Complexity: $O(A)$
*   **Why**: The `HashSet` stores up to $A$ unique characters. The space consumed by the sliding window pointers and counters is $O(1)$. Thus, the space is dominated by the storage of unique characters.

---

## Component Deep Dive

### 1. Sliding Window Logic
The core mechanism is the constraint: `(right - left + 1) - targetCharacterCount <= k`.
*   `(right - left + 1)` represents the current window size.
*   `targetCharacterCount` represents the frequency of the current "target" character within the window.
*   The difference is the number of characters that *must* be changed to match the `targetCharacter`.

### 2. Contraction Strategy
When the condition `(windowSize - targetCharacterCount) > k` is triggered, the `while` loop forces the window to shrink from the `left`. This is crucial because it ensures the window is always the largest valid sequence for the current `targetCharacter`. Importantly, the window size only ever stays the same or grows; it never shrinks when moving `right`, which maintains the "longest" property.

### 3. Edge-Case Handling
*   **$k=0$**: The code correctly defaults to finding the longest substring of identical contiguous characters.
*   **$k \ge |s|$**: The code will correctly return the full length of the string, as the condition will never be violated.
*   **Empty String**: The loops will not execute, returning `maxLength = 0`, which is the correct behavior.

---

## Key Insights

### Performance Nuance: Sub-optimal vs. Optimal
While this solution is $O(A \cdot N)$, it is less efficient than the single-pass $O(N)$ approach.
*   **The Single-Pass Optimization:** In a single-pass approach, you maintain a frequency map of *all* characters in the window and track the `maxFrequency` (the frequency of the most common character seen so far). The condition becomes `(windowSize - maxFrequency) <= k`.
*   **Why the provided code is still valid:** The provided solution is conceptually easier to reason about because it transforms the problem into a "Filter" exercise: "If I *must* use this character as the result, what is the best I can do?"

### Potential Bottlenecks
*   **Repeated Scanning:** For strings with very large alphabets (e.g., Unicode), $O(A \cdot N)$ could become significantly slower than the single-pass approach.
*   **String Access:** `s.charAt(right)` is called frequently. In performance-critical Java applications, converting the string to a `char[]` (`s.toCharArray()`) before entering the loops would avoid the overhead of method calls to `charAt()` and potential bounds checking.

### Subtle Logic Observation
The current implementation resets `targetCharacterCount` inside the `targetCharacter` loop but not `left`. This is correct because the `left` pointer must be reset for every new character iteration. If the developer attempted to maintain state across characters, the logic would break, highlighting that the algorithm treats each character "target" as an independent problem instance.

---

## standard_two_pointer_modified.java
*Style: concise*

### Notes: Longest Repeating Character Replacement

**Overview**
Finds the longest substring length possible after replacing at most `k` characters to make all characters in the substring identical. It achieves this by iterating through each possible target character and maintaining a sliding window.

**Key Logic**
*   **Target-Based Sliding Window:** Instead of tracking the global most frequent character (as in the standard $O(N)$ solution), this approach brute-forces each character ('A'-'Z') as the "intended" character for the window.
*   **Window Validity Condition:** A window is valid if `(windowSize - countOfTargetChar) <= k`. This represents the number of characters that would need to be replaced to make the entire window equal to `targetCharacter`.
*   **Complexity:** $O(26 \times N)$, which simplifies to $O(N)$.

**Implementation Details**
*   `present[]`: Optimization to skip characters not present in the input string, avoiding unnecessary passes.
*   **Shrink Logic:** The `while` loop decrements `targetCharacterCount` correctly when moving the `left` pointer to maintain the validity constraint.
*   **Result:** `maxLength` is updated at every step, ensuring the largest valid window size across all target character iterations is captured.

---

## optimal_two_pointer.java
*Style: detailed*

# Engineering Deep-Dive: Longest Repeating Character Replacement

## Summary
The solution implements a **Sliding Window** technique to solve the Longest Repeating Character Replacement problem. The core observation is that for any window of length $L$ with a character frequency $F$ of the most frequent character, the number of replacements required to make the entire window uniform is $L - F$.

The algorithm maintains a dynamic window $[left, right]$ that satisfies the constraint $(right - left + 1) - mostFrequentCount \le k$. By expanding the $right$ boundary and only shrinking the $left$ boundary when the constraint is violated, we effectively search for the maximum valid window size in linear time.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Expansion:** The `right` pointer traverses the string exactly once.
*   **Contraction:** Although there is a `while` loop, the `left` pointer also only traverses the string at most once. Each character is added to the frequency map once and removed at most once.
*   **Map Operations:** With a fixed alphabet size (e.g., 26 for uppercase English letters), `Map` operations (or an integer array used as a frequency map) are $O(1)$. Thus, the amortized complexity remains linear.

### Space Complexity: $O(1)$
*   The `HashMap` stores character frequencies. Since the keys are bounded by the input character set size (e.g., 26 uppercase English letters), the space usage does not scale with $N$. It is effectively constant space.

---

## Component Deep Dive

### 1. The Frequency Heuristic (`mostFrequentCount`)
A critical nuance is that `mostFrequentCount` is **not strictly updated when the `left` pointer increments**. 
*   **Why this works:** When `(right - left + 1) - mostFrequentCount > k` is true, it means we have found a window that is "too invalid." By moving `left`, we decrease the window size. We do not need to recalculate the *true* `mostFrequentCount` for the new, smaller window because that smaller window cannot possibly result in a `maxLength` greater than the one we have already recorded. We only care about finding a window size that *exceeds* our previous `maxLength`.

### 2. Window Validity Constraint
The expression `(right - left + 1) - mostFrequentCount` calculates the number of characters that *must* be changed to make all characters in the current window equal to the most frequent character currently in the window. If this value exceeds $k$, the window is invalid, and we must shrink it from the left.

### 3. Edge Case Handling
*   **$k = 0$:** The logic collapses to finding the longest substring of identical consecutive characters, as `(length - mostFrequentCount)` must be 0.
*   **$k \ge s.length()$:** The entire string can be converted to the most frequent character, and the function will correctly return `s.length()`.
*   **Single character strings:** The loops execute correctly, returning 1.

---

## Key Insights & Optimization Nuances

### Optimization: Array over HashMap
While the provided code uses a `HashMap<Character, Integer>`, this involves unnecessary overhead:
*   **Boxing/Unboxing:** Constant conversion between `char` and `Character` objects.
*   **Hashing Overhead:** The constant time complexity of a HashMap hides a non-trivial constant factor.
*   **Refinement:** Replacing `HashMap<Character, Integer>` with `int[26]` (assuming ASCII/uppercase alphabet) eliminates object allocation and hashing, significantly improving performance in tight latency-sensitive loops.

### Potential Logic Trap: The "Stale" Max
It is a common point of confusion that `mostFrequentCount` is not decremented when `left` moves. 
*   Consider a window that results in a valid `maxLength`. If we then shrink the window, the *new* max frequency might be lower than the old one. 
*   However, because we are looking for the *maximum* length, we don't care about a shrinking window's frequency. We only care if we can find a *larger* window later. If a window with a smaller `mostFrequentCount` appears, it cannot produce a `maxLength` larger than the current global `maxLength`, so the accuracy of `mostFrequentCount` for smaller windows is irrelevant to the final output.

### Subtle Bug / Thread Safety
*   The current implementation is **not thread-safe**. It relies on shared state in the `characterFrequency` map. If this were moved to a service class, it should be made stateless or the map should be instantiated within the method scope (as it is now) to ensure thread-local execution.

---
