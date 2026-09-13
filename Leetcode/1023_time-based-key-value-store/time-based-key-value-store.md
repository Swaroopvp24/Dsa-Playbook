# time-based-key-value-store

## standard_binary_search_1.java
*Style: detailed*

# Technical Deep-Dive: Time-Series Key-Value Store

## Summary
The `TimeMap` implementation is a specialized data structure designed for temporal data retrieval. It leverages a `HashMap` to achieve $O(1)$ average-case key lookups, where each key maps to an `ArrayList` of `TimeValue` objects. The core algorithmic technique is **Binary Search (Upper Bound variant)** on a sorted list of timestamps, allowing for $O(\log N)$ retrieval of values corresponding to the "floor" of a requested timestamp. 

This approach assumes that timestamps are provided in non-decreasing order for each key (a standard contract for time-series data ingest), which maintains the sorted property of the `ArrayList` without requiring explicit sorting operations during `set` calls.

---

## Complexity Analysis

### Time Complexity
*   **`set(key, value, timestamp)`**: **$O(1)$**
    *   `HashMap.computeIfAbsent` performs a hash lookup ($O(1)$ average). `ArrayList.add` is amortized $O(1)$. 
*   **`get(key, timestamp)`**: **$O(\log N)$**
    *   Retrieving the `List` from the `HashMap` is $O(1)$.
    *   The binary search performs $\log_2 N$ iterations, where $N$ is the number of values stored for the specific key. Each iteration performs constant-time arithmetic and list access.

### Space Complexity
*   **Total Space**: **$O(N + K)$**
    *   Where $N$ is the total number of `TimeValue` entries across all keys and $K$ is the number of unique keys. Each entry consumes object overhead (the `TimeValue` instance), and the `HashMap` structure incurs overhead proportional to the number of keys.

---

## Component Deep Dive

### 1. Storage Schema (`Map<String, List<TimeValue>>`)
By partitioning values into key-specific lists, we isolate search spaces. This effectively creates a "Bucket" architecture where the time-series search is localized to the history of a single key.

### 2. The `get` Algorithm (Modified Binary Search)
The requirement is to find `floor(timestamp)`—the largest timestamp $T$ such that $T \le \text{target}$. 
*   **Logic**: When `values.get(middle).timestamp <= timestamp`, we record `middle` as a candidate (`latestValidIndex`) and continue searching to the *right* (`left = middle + 1`) to see if a larger, yet still valid, timestamp exists.
*   **Edge-Case Handling**: 
    *   **Empty Result**: If `latestValidIndex` remains `-1`, it confirms that even the smallest timestamp for that key is greater than the requested `timestamp`.
    *   **Invalid Key**: Returns `""` immediately via `values == null` check.

---

## Key Insights & Performance Nuances

### 1. The Monotonicity Assumption
This implementation implicitly relies on the caller providing timestamps in non-decreasing order per key. 
*   **Why it matters**: If `set` operations occur out of order (e.g., `(10, "A")` then `(5, "B")`), the `ArrayList` will be unsorted. The binary search will return incorrect results because it expects a monotonic sequence. 
*   **Fix**: If out-of-order `set` calls are possible, one must either perform a `Collections.sort()` (impacting `set` complexity to $O(N \log N)$) or use a `TreeMap<Integer, String>` per key.

### 2. Memory Fragmentation
Using an `ArrayList<TimeValue>` involves wrapping primitives in objects.
*   **Overhead**: Each `TimeValue` instance adds header overhead (usually 12-16 bytes). 
*   **Optimization**: In high-throughput, latency-sensitive systems, one could replace the `ArrayList<TimeValue>` with two primitive arrays (e.g., `int[] timestamps` and `String[] values`). This would significantly reduce the garbage collector (GC) pressure and improve cache locality during the binary search.

### 3. Binary Search Boundary Safety
The expression `int middle = left + (right - left) / 2;` is used instead of `(left + right) / 2`. 
*   **Why**: While integer overflow is unlikely given typical timestamp ranges, this is a critical best practice in Java to prevent overflow if `left + right` exceeds `Integer.MAX_VALUE`.

### 4. Search Strategy
The algorithm uses the "find the last occurrence" approach. An alternative is using `Collections.binarySearch` with a custom comparator; however, that approach is often more complex to implement correctly because `Collections.binarySearch` returns the insertion point (a negative value) when a direct match is not found, requiring manual translation to the floor index. The provided manual implementation is more readable and less prone to off-by-one errors in this specific context.

---
