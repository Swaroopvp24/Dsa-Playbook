# maximum-number-of-balloons

## attempt_1.java
*Style: detailed*

# Engineering Deep-Dive: `maxNumberOfBalloons`

## Summary
The solution employs a **frequency counting pattern** (a variation of the Counting Sort / Hash Bucket approach) to determine the bottleneck resource in string construction. The problem is modeled as a resource allocation constraint problem: given a set of characters as raw materials, we must determine the maximum number of times the target set `{b:1, a:1, l:2, o:2, n:1}` can be satisfied. By mapping character frequencies to an integer array, we reduce the problem to finding the minimum value in a set of constrained ratios.

## Complexity Analysis

### Time Complexity: $O(N + K)$
*   **$O(N)$**: Where $N$ is the length of the input string. We perform a single linear pass over the input to populate the frequency array.
*   **$O(K)$**: Where $K$ is the alphabet size (constant 26). The final calculations and `Math.min` operations occur in constant time relative to the input length.
*   **Total**: $O(N)$, which is optimal as we must inspect each character at least once.

### Space Complexity: $O(1)$
*   The algorithm utilizes a fixed-size integer array of length 26. Regardless of the input string length, the memory footprint remains constant ($26 \times 4$ bytes), fulfilling the $O(1)$ space requirement.

## Component Deep Dive

### 1. Frequency Mapping (`int[] charFrequency`)
Instead of using a `HashMap<Character, Integer>`, which incurs boxing/unboxing overhead and constant-time hashing penalties, we utilize a primitive `int` array.
*   **Indexing:** `ch - 'a'` maps ASCII/Unicode values to a 0-25 range. This provides $O(1)$ random access memory overhead.
*   **Constraint Handling:** Since the input is constrained to lowercase English letters, this approach is both memory-efficient and cache-friendly.

### 2. Constraint Ratios
The core logic relies on the stoichiometry of the word "balloon":
*   **Single-use characters (`b`, `a`, `n`):** The count of these characters translates 1:1 to the number of balloons.
*   **Double-use characters (`l`, `o`):** The count of these must be integer-divided by 2. This is a critical step: `charFrequency['l' - 'a'] / 2` represents the maximum sets of pairs available. If there are 3 'l's, we can only form 1 pair; the integer division correctly yields 1.

### 3. Edge Case Handling
*   **Empty String:** The `charFrequency` array initializes to zeros. All `Math.min` comparisons will result in 0, correctly identifying that 0 balloons can be formed.
*   **Missing Characters:** If any required character (e.g., 'b') is missing, the `charFrequency` index will be 0. `Math.min` propagates this 0, ensuring an accurate result.
*   **Input with Irrelevant Characters:** The code naturally ignores any characters not present in the "balloon" set, as they are populated in the frequency array but never queried in the `Math.min` sequence.

## Key Insights

*   **Integer Division Nuance:** Using `/ 2` is a safe, efficient way to compute the "batches" of pairs required. Note that this is only valid because the ratio is exact (e.g., we don't need *at least* two 'l's, we need exactly two per balloon unit).
*   **Bottleneck Bottling:** The logic assumes a "weakest link" approach. The total count is strictly limited by the resource that runs out first. This is a classic application of **Little's Law** or **Theory of Constraints** in software engineering—throughput is determined by the slowest process/scarcest resource.
*   **Optimization:** While `text.toCharArray()` creates a copy of the underlying character array (allocating $O(N)$ extra space), it is generally cleaner than repeated `charAt()` calls. If extreme memory constraints existed, one could iterate using `text.length()` and `text.charAt(i)` to achieve $O(1)$ auxiliary space completely.
*   **Data Structure Choice:** An `int[26]` array is significantly faster than a `Map` due to **spatial locality** and the elimination of hash collisions/rehashing events.

---
