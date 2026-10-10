# maximum-difference-between-even-and-odd-frequency-i

## attempt_1.java
*Style: detailed*

# Engineering Deep-Dive: Frequency-Based Parity Differential Analysis

## Summary
The solution implements a linear-time frequency analysis algorithm to calculate the difference between the most frequent odd-parity character count and the least frequent even-parity character count within a string consisting of lowercase English letters. 

The approach utilizes **Frequency Counting (Bucket Array)** as its primary data structure to transform the input string into a fixed-size integer array, reducing the problem space from $O(N)$ string operations to $O(1)$ (constant 26-size) lookups. By decoupling the raw frequency extraction from the parity-based optimization, the algorithm achieves efficient single-pass categorization.

---

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Preprocessing:** We iterate through the string $S$ of length $N$ exactly once to populate the frequency array. This yields $O(N)$.
*   **Parity Analysis:** We iterate over the fixed-size alphabet array ($|\Sigma| = 26$). Since 26 is a constant, this operation is $O(1)$.
*   **Total:** $O(N) + O(1) = O(N)$.

### Space Complexity: $O(1)$
*   **Data Structure:** We allocate an integer array of size 26 regardless of the input string length $N$. 
*   **Variables:** Constant extra space is used for `minEvenFrequency`, `maxOddFrequency`, and loop indices.
*   **Total:** $O(1)$, as the space requirements do not scale with $N$.

---

## Component Deep Dive

### 1. Frequency Distribution (`int[] frequency`)
Using `frequency[c - 'a']` provides an $O(1)$ mapping from character to index. This technique is optimal for problems involving lowercase English alphabets, as it avoids hashing overhead and memory allocation associated with `HashMap<Character, Integer>`.

### 2. The Logic Filter
The solution treats the parity of the counts as the primary classification constraint:
*   **Even Filtering:** `count % 2 == 0` ensures we only track even occurrences. `Integer.MAX_VALUE` initialization acts as a sentinel to handle the case where no even frequencies are present (though the problem implied existence).
*   **Odd Tracking:** `count % 2 != 0` tracks all odd-occurring characters. Since we seek the *maximum*, initialization to `0` is safe and efficient.

### 3. Boundary/Edge Case Handling
*   **Zero Frequencies:** The condition `count > 0` inside the even check is critical. If we did not check for `count > 0`, the algorithm would interpret the absence of a character (count 0) as an even frequency of 0, erroneously setting `minEvenFrequency` to 0. 
*   **Missing Parity Sets:** If the input string contains only even or only odd characters, `maxOddFrequency` or `minEvenFrequency` will remain at their sentinel values. Depending on the calling context, one might need to add assertions or guard clauses to handle inputs that don't satisfy the problem's implied assumption of containing at least one of each.

---

## Key Insights

*   **Fixed Alphabet Constraint:** The performance advantage here is entirely derived from the fixed alphabet size ($\Sigma = 26$). If the input were extended to Unicode, this approach would require a `HashMap` or a significantly larger array, moving the space complexity to $O(\Sigma)$.
*   **Implicit Assumptions:** The code assumes that at least one even frequency and one odd frequency will exist in the input. If the input is guaranteed to contain both, the current implementation is optimal. If not, the return value `maxOddFrequency - Integer.MAX_VALUE` will result in a significant underflow/negative integer, which serves as a sentinel for invalid input configurations.
*   **Single-Pass Optimality:** While we use two loops (one for counting, one for analysis), the overall operation is cache-friendly. The frequency array fits comfortably within L1 cache, ensuring the second loop is extremely fast despite the $O(1)$ nature.
*   **Potential Optimization:** One could technically perform the parity comparison inside the first loop (during character counting). However, that would require re-evaluating the `min/max` logic repeatedly during every character ingestion, which is unnecessary compared to the clear separation provided by the two-pass approach.

---
