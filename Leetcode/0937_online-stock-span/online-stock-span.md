# online-stock-span

## stack_solution(Monotonic_stack).java
*Style: detailed*

# Technical Deep-Dive: Monotonic Stack for Stock Spanning

## Summary
The `StockSpanner` implements an algorithm to calculate the "span" of a stock's price—defined as the number of consecutive days (including the current day) prior to and including the current day where the price was less than or equal to the current price.

The solution utilizes a **Monotonic Decreasing Stack**. By maintaining a stack of price-span pairs, we reduce the problem of finding the span from a $O(N)$ linear scan to an amortized $O(1)$ operation. The core algorithmic technique is **compressive accumulation**: instead of storing every historical price, we "roll up" the spans of preceding smaller days into the current entry, effectively skipping redundant comparisons.

---

## Complexity Analysis

### Time Complexity: Amortized $O(1)$
While the `next` method contains a `while` loop that can execute multiple times, the overall complexity is $O(1)$ per call.
*   **Proof:** Each price-span pair is pushed onto the stack exactly once. Each pair is popped from the stack at most once. Over a sequence of $N$ calls, there are at most $N$ total pushes and $N$ total pops. 
*   **Result:** The total time complexity for $N$ calls is $O(N)$, resulting in an amortized cost of $O(1)$ per call.

### Space Complexity: $O(N)$
*   **Explanation:** In the worst-case scenario (e.g., strictly decreasing stock prices), every input price is pushed onto the stack without any pops. This results in the stack size growing linearly with the number of calls $N$. 

---

## Component Deep Dive

### 1. The Monotonic Stack Structure
The stack stores `int[]` arrays of size 2: `[price, span]`.
*   **Price:** Used as the comparison key to maintain the monotonic property.
*   **Span:** The cumulative count of days aggregated by that price entry.

### 2. The `next(int price)` Mechanism
*   **The Aggregation Logic:** When a new price arrives, we inspect the stack top. If `currentPrice >= stack.peek()[0]`, it means the previous day (and its associated span) is strictly less than or equal to the current day. 
*   **The Skip Optimization:** Instead of just recording the current day, we add the `previousDay[1]` (its span) to our current `span`. This effectively "flattens" the history; the current stack entry now represents a contiguous block of days that were all $\le$ the current price.
*   **Terminal Condition:** The `while` loop terminates when we either hit an empty stack or a price strictly greater than our current price. This new state maintains the **monotonic decreasing property** of the stack.

### 3. Edge Case Handling
*   **Strictly Increasing Prices:** The stack will effectively be cleared every time, as the `while` loop will pop every previous element. The current price will then represent the total span of all prior days plus itself.
*   **Strictly Decreasing Prices:** The `while` loop condition is never met; the stack grows by 1 per call, storing the new price with a span of 1.
*   **Equal Prices:** The `price >= stack.peek()[0]` condition ensures that equal prices are also aggregated, satisfying the problem requirement of "less than or equal to."

---

## Key Insights

*   **Stack Invariant:** After every `next()` call, the stack is guaranteed to be sorted by price in descending order (bottom to top is large to small). If we encounter a price $P$, all prices $p < P$ are consumed and merged into $P$'s span.
*   **Memory Efficiency:** Using `ArrayDeque` is critical here. Unlike `java.util.Stack`, `ArrayDeque` is not synchronized and provides better performance for stack operations as it avoids the overhead of `Vector` methods.
*   **Subtle Bug Warning:** Be careful with the comparison operator. If the requirement were strictly "less than," the condition would change to `price > stack.peek()[0]`. The current use of `>=` is intentional for the "less than or equal to" constraint.
*   **Potential Optimization:** For high-frequency systems where GC overhead is a concern, consider using two primitive arrays (`int[] prices`, `int[] spans`) and a manual `top` pointer instead of an object-based `Deque<int[]>`. This would eliminate the `new int[]` allocation per `next()` call, significantly reducing heap pressure.

---
