# online-stock-span

## stack_solution(Monotonic_stack).java
*Style: detailed*

# Technical Deep-Dive: Monotonic Stack for Stock Span

## Summary
The `StockSpanner` implements a **Monotonic Decreasing Stack** algorithm to solve the "Online Stock Span" problem. Instead of performing a naive $O(N)$ linear scan backwards for every new price, the algorithm maintains a stack of prices in strictly decreasing order. By storing the cumulative span (the number of consecutive days a price was lower or equal) alongside the price, the algorithm effectively collapses segments of the history. When a new price arrives, we greedily pop all elements that are less than or equal to the current price, inheriting their spans to form a new, aggregate span.

---

## Complexity Analysis

### Time Complexity: $O(1)$ Amortized
*   **Analysis:** While a single call to `next()` can involve multiple pops in the `while` loop (potentially $O(N)$ in the worst case), each price element is pushed onto the stack exactly once and popped at most once. 
*   **Amortization:** Across a sequence of $N$ calls, the total number of operations is bounded by $2N$. Therefore, the amortized time complexity per call is $O(1)$.

### Space Complexity: $O(N)$
*   **Analysis:** In the worst-case scenario (a strictly decreasing sequence of prices, e.g., $100, 90, 80, \dots$), no elements are popped. The stack will grow linearly with the number of calls, storing $N$ elements where each entry is a two-element integer array.

---

## Component Deep Dive

### 1. Data Structure: `Deque<int[]>`
The implementation uses `ArrayDeque`, which is preferred over `Stack` in Java due to its better cache locality and performance (it is not synchronized, avoiding unnecessary overhead). Storing an `int[]` of size 2 (`[price, span]`) keeps related metadata together, reducing the complexity of pointer management in the stack operations.

### 2. The `next(int price)` Logic
*   **Aggregation:** The `span` variable initializes to 1 (the current day itself). 
*   **The While-Loop:** This is the heart of the algorithm. By popping `previousDay`, we retrieve the span representing a contiguous range of days where the stock price was below the popped price. Because the current `price` is $\ge$ the popped `price`, the current `price` also spans across all those previous days.
*   **Invariant Maintenance:** After the loop, the stack remains strictly monotonic (the new price is pushed onto the stack, and it is strictly greater than the new `peek()` if the loop completed).

### 3. Edge-Case Handling
*   **Strictly Increasing Input:** The loop condition `price >= stack.peek()[0]` will trigger for every call, popping the previous element and accumulating spans. The stack size stays small (often size 1).
*   **Strictly Decreasing Input:** The loop condition never evaluates to true. The stack grows linearly, and every `span` remains 1.
*   **Empty Stack:** Handled implicitly by `!stack.isEmpty()`, preventing `NoSuchElementException`.

---

## Key Insights

### The "Stack-as-History" Pattern
This is a classic application of the **Monotonic Stack** pattern used to solve "Nearest Greater Element" problems. By storing the `span`—which acts as a "skip pointer"—we avoid redundant comparisons. The stack effectively acts as a compressed representation of the historical timeline.

### Performance Nuances
*   **Object Allocation:** The implementation creates a `new int[]` on every `next()` call. In high-frequency, low-latency environments, this could trigger frequent GC activity. 
    *   *Optimization:* If performance is hyper-critical, consider using two separate `int[]` arrays (a "parallel array" approach) or a custom linked-list node structure to avoid `new` allocations for every single call.
*   **Cache Locality:** `ArrayDeque` is backed by an array, which is cache-friendly compared to `LinkedList`. However, the constant allocation of `int[]` objects spreads data across the heap. If the frequency of calls is massive, object pooling for the `int[]` arrays might yield a slight performance gain.

### Subtle Logic Trap
Do not attempt to pre-calculate the span by looking at the index of the last greater element. While that is a valid alternative approach (finding the *previous greater element index*), it requires storing indices and calculating `current_index - prev_greater_index`. The current "aggregation" method is more elegant because it performs the work incrementally and requires no external counter or global index state.

---
