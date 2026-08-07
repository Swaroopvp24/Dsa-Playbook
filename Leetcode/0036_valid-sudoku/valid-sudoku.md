# valid-sudoku

## attempt_1.java
*Style: detailed*

# Technical Deep-Dive: Sudoku Validator

## Summary
The provided solution validates a $9 \times 9$ Sudoku board by enforcing three fundamental rules of the game: row uniqueness, column uniqueness, and $3 \times 3$ sub-grid uniqueness. The approach employs an **iterative scanning strategy**, performing three distinct passes over the input matrix. Each pass utilizes a `HashSet` to provide $O(1)$ average-time lookup for duplicate detection. This is an application of the "Constraint Satisfaction" checking paradigm where we greedily validate state validity.

## Complexity Analysis

### Time Complexity: $O(N^2)$
*   **Derivation:** Where $N=9$ (the dimension of the board). The algorithm iterates over every cell in the grid exactly three times (once for rows, once for columns, once for sub-grids).
*   **Work per cell:** Each operation within the inner loops (set insertion/lookups) is $O(1)$ on average.
*   **Total:** $3 \times (N \times N) = O(N^2)$. Since $N$ is a constant (9), this is technically $O(1)$, but in the context of an $N \times N$ board, the scaling factor is quadratic.

### Space Complexity: $O(N)$
*   **Derivation:** In each pass, we allocate a `HashSet` to track characters seen.
*   **Capacity:** Each set stores a maximum of 9 distinct characters ('1'-'9').
*   **Total:** $O(N)$. Even if implemented as a bitmask, the space requirement remains proportional to the number of possible values in a row/column/sub-grid.

---

## Component Deep Dive

### 1. Row/Column Validation
The first two passes leverage nested loops to traverse the matrix. By keeping the outer loop fixed to `i` and iterating `j`, the code effectively flattens the 2D plane into independent 1D vectors. The use of a new `HashSet` per iteration ensures memory is cleared and state is isolated between rows/columns.

### 2. Sub-grid Partitioning
The sub-grid logic is the most complex component. It maps a linear index `sq` (0 to 8) to a coordinate space:
*   **Block Indexing:** `(sq / 3) * 3` determines the starting row index, and `(sq % 3) * 3` determines the starting column index.
*   **Traversal:** The `i` and `j` loops (0-2) act as offsets within the identified $3 \times 3$ box. 
*   **Edge Case Handling:** The `b[r][c] == '.'` check correctly ignores empty cells, as they do not violate uniqueness constraints.

---

## Key Insights

### Performance Optimization: Bitmasking vs. Hashing
While `HashSet<Character>` is semantically clear, it involves significant object overhead (`Character` objects, `Map.Entry` objects inside the `HashSet`). 
*   **Optimization:** A `boolean[9]` or even a single `short` (as a bitmask) could replace the `HashSet`. Using a `short` (where each bit $k$ represents the presence of digit $k+1$) reduces the space complexity constant and eliminates heap allocations, leading to significantly better cache locality and zero GC pressure.

### Algorithmic Consolidation
The current implementation makes three separate passes over the data. This is clear but inefficient.
*   **Refinement:** A single pass solution is possible. We can maintain three arrays of `short` bitmasks:
    *   `rows[9]`
    *   `cols[9]`
    *   `boxes[9]`
    *   Calculating the box index as `(r / 3) * 3 + (c / 3)`.
    *   As we traverse `r, c` once, we update all three bitmasks. If any bitwise `AND` operation shows a conflict, return `false` immediately. This reduces the time complexity constant significantly.

### Potential Pitfalls
*   **Char to Integer Mapping:** The current implementation treats inputs as characters. If the input alphabet were to expand beyond '1'-'9', the logic remains robust, but the memory overhead of `HashSet` would grow.
*   **Input Integrity:** The function assumes the board is exactly $9 \times 9$ and contains valid characters ('.' or '1'-'9'). It does not perform bounds checking or validation of the board's structure, which could lead to `ArrayIndexOutOfBoundsException` if the input is malformed.

---
