# move-zeroes

## attempt_1.java
*Style: concise*

### Notes: Move Zeroes (In-place)

**Functionality**
Moves all zeros to the end of an integer array while maintaining the relative order of non-zero elements. It performs this operation in-place with $O(n)$ time complexity and $O(1)$ space complexity.

**Key Components**
*   `j`: The "write" pointer representing the index where the next non-zero element should be placed.
*   `i`: The "scan" pointer iterating through the array.
*   `Swap logic`: Swaps the current non-zero element at `i` with the element at `j`.

**Logic Notes**
*   **Two-Pointer Strategy:** By incrementing `j` only when a non-zero element is found, `j` effectively tracks the boundary between the processed non-zero elements and the zeros.
*   **Efficiency:** The swap operation is cleaner than overwriting and refilling, as it handles the displacement of zeros implicitly without requiring a second pass or extra memory.
*   **Edge Case:** If the array contains no zeros, `i` and `j` will remain equal throughout, resulting in redundant self-swaps (a negligible cost for code simplicity).

---
