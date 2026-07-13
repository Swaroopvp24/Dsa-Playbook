# sequential-digits

## attempt_1_icopied.java
*Style: detailed*

# Technical Reference: Sequential Digits Generation

## Summary
The solution employs a **sliding window / exhaustive generation approach** to identify integers whose digits are in strictly increasing consecutive order. Since the range of possible integers is constrained by the base-10 system (digits 1–9), the total search space is extremely small. The algorithm iterates through all possible lengths ($L \in [2, 9]$) and all possible starting digits ($S \in [1, 9-L+1]$), constructing the sequence mathematically rather than searching through the range `[low, high]`, which would be computationally inefficient for large inputs.

---

## Complexity Analysis

### Time Complexity: $O(1)$
*   **Analysis:** Although the code contains nested loops, the constraints are fixed. The outer loop runs at most 8 times (lengths 2 through 9), the middle loop at most 9 times, and the inner loop at most 9 times.
*   **Formal Bound:** The number of sequential digit numbers is exactly $\sum_{i=2}^{9} (10-i) = 44$. The algorithm performs a constant number of operations, making it $O(1)$ relative to the input magnitude.

### Space Complexity: $O(1)$
*   **Analysis:** We store the results in a `List<Integer>`. Given that there are only 44 such numbers in existence, the space required to store the output is constant. The auxiliary space used for the `num` variable is also constant.

---

## Component Deep Dive

### 1. Sequential Generation Logic
The core mechanism `num = num * 10 + (start + i)` is a base-10 left-shift followed by an addition. This effectively builds the number digit-by-digit from left to right.
*   **Example (Start=2, Length=3):**
    *   `i=0`: `num = 0 * 10 + 2 = 2`
    *   `i=1`: `num = 2 * 10 + 3 = 23`
    *   `i=2`: `num = 23 * 10 + 4 = 234`

### 2. Constraint Handling
*   **Length Constraint:** The algorithm caps length at 9 because a strictly increasing sequence of digits cannot exceed 9 digits (123,456,789).
*   **Start Constraint:** The inner loop condition `start <= 10 - length` acts as a guardrail. If `length` is 9, `start` must be $\le 1$, ensuring the number doesn't attempt to append a digit `> 9`.
*   **Input Range:** By checking `num >= low && num <= high`, we effectively filter the pre-generated set of 44 sequential numbers. 

---

## Key Insights

### 1. Efficiency vs. Exhaustive Search
A naïve approach would be to iterate from `low` to `high` and check if each number is "sequential." If `high` is $10^9$, that approach would result in $O(N)$ complexity (where $N = high - low$). By constructing the numbers directly, we bypass the need to iterate through invalid candidates.

### 2. Implicit Ordering
Because the outer loop iterates by `length` and the middle loop iterates by `start`, the resulting list is naturally **sorted in ascending order**. This is a significant optimization if the problem requirements were expanded to require sorted output (which is standard for this LeetCode problem).

### 3. Edge Cases & Robustness
*   **Single-digit numbers:** The loop starts at `length = 2`. If the problem requirements defined single digits (1-9) as sequential, the logic would fail. The current implementation assumes a minimum length of 2.
*   **Integer Overflow:** The maximum possible sequential number is `123,456,789`, which is well within the `Integer.MAX_VALUE` ($2,147,483,647$). No `long` type promotion is required for the `num` variable.
*   **Empty Range:** If `low` and `high` are such that no sequential numbers fall in between, the code gracefully returns an empty list without auxiliary overhead.

### 4. Potential Optimization (Pre-calculation)
Since the set of sequential numbers is static and small (44 total), this solution could be further optimized in a production environment by using a `static final` array or list to pre-calculate these values once at class-loading time, reducing the runtime complexity to $O(K)$ where $K$ is the number of sequential digits within the specific input bounds.

---
