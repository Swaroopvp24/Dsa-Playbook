# pascals-triangle

## attempt_1.java
*Style: concise*

### Pascal's Triangle Generator
This code generates Pascal's Triangle up to `numRows` using a recursive approach. Each row is constructed based on the sum of adjacent elements from the previous row.

#### Key Methods
*   `generate(int numRows)`: Initializes the result list and initiates the recursive building process starting at index 0.
*   `fun(List<List<Integer>> list, int i, int n)`: Recursive helper that builds the $i$-th row and appends it to the master list until $i = n$.

#### Non-Obvious Logic
*   **Boundary Conditions:** Rows always start and end with `1`. Internal elements are derived via `list.get(i-1).get(j-1) + list.get(i-1).get(j)`, effectively performing a rolling sum of the previous row.
*   **Recursion Flow:** The base case `i >= n` terminates the recursion. Because the function appends to the list *before* the recursive call (`fun(list, i + 1, n)`), it ensures rows are built in the correct order ($0$ to $n-1$).

---
