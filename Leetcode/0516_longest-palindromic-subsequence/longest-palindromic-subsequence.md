# longest-palindromic-subsequence

## attempt_1.java
*Style: detailed*

**Summary**
==========

The provided Java code implements a dynamic programming solution to find the length of the longest palindromic subsequence within a given string `st1`. The approach involves reversing the input string `st1` to create a new string `st2`, and then using a 2D array `dp` (not fully utilized) to store intermediate results. However, instead of utilizing the 2D array for dynamic programming, the code employs two 1D arrays `prev` and `cur` to track the lengths of palindromic subsequences in a space-efficient manner.

The algorithmic technique used is based on the concept of longest common subsequences (LCS) between the original string and its reversed version. When a character in the original string matches a character in the reversed string, it indicates a palindromic subsequence of length 1. By iteratively comparing characters and updating the `prev` and `cur` arrays, the code computes the length of the longest palindromic subsequence.

**Complexity Analysis**
--------------------

### Time Complexity

The time complexity of the provided solution is **O(n^2)**, where `n` represents the length of the input string `st1`. This is because the code contains two nested loops, each iterating up to `n` times.

*   The outer loop iterates `n` times, where `n` is the length of `st1`.
*   The inner loop also iterates up to `n` times, as `st2` is the reversed version of `st1` with the same length.

### Space Complexity

The space complexity of the solution is **O(n)**, primarily due to the utilization of two 1D arrays `prev` and `cur`, each of length `n`. Although a 2D array `dp` is initialized, it is not fully utilized and does not contribute significantly to the overall space complexity.

**Component Deep Dive**
--------------------

### Initialization and Setup

The code initializes several variables and arrays:

*   `n1` and `n2` represent the lengths of `st1` and `st2`, respectively, plus 1. These values are used to iterate over the characters in the strings.
*   `dp` is a 2D array intended for dynamic programming, but it is not fully utilized in the provided implementation.
*   `s1` and `s2` are character arrays representing `st1` and `st2`, respectively.
*   `prev` and `cur` are 1D arrays used to track the lengths of palindromic subsequences.

### Dynamic Programming Loop

The core of the solution lies in the nested loop structure:

*   The outer loop iterates over the characters in `st1` (represented by `i`).
*   The inner loop iterates over the characters in `st2` (represented by `j`).

Inside the inner loop, the code checks if the current characters in `s1` and `s2` are equal. If they are, it means a palindromic subsequence of length 1 is found, and the corresponding value in the `cur` array is updated to `1 + prev[j - 1]`.

If the characters do not match, the code updates the `cur` array with the maximum value between `prev[j]` and `cur[j - 1]`. This ensures that the length of the longest palindromic subsequence is propagated correctly.

After the inner loop completes, the `prev` array is updated with the values from the `cur` array, and a new `cur` array is initialized for the next iteration. This process continues until all characters in `st1` have been processed.

### Result

The final result is stored in the last element of the `prev` array, `prev[n2 - 1]`, which represents the length of the longest palindromic subsequence in the input string `st1`.

**Key Insights**
----------------

*   **Unused 2D Array**: Although a 2D array `dp` is initialized, it is not fully utilized in the provided implementation. The code relies on the 1D arrays `prev` and `cur` for dynamic programming. Removing the unused 2D array can simplify the code and reduce memory usage.
*   **Space Optimization**: The use of two 1D arrays `prev` and `cur` is a space-efficient approach, as it avoids the need for a large 2D array. However, it is essential to ensure that the arrays are properly updated and reused to maintain the correctness of the solution.
*   **Character Comparison**: The code relies on character-by-character comparison between `s1` and `s2`. This comparison is case-sensitive and does not account for whitespace or punctuation. If the input string may contain these characters, additional preprocessing steps may be necessary to handle them correctly.
*   **Input Validation**: The code assumes that the input string `st1` is non-null and non-empty. Adding input validation checks can help prevent `NullPointerExceptions` and ensure the solution handles edge cases correctly.

---
