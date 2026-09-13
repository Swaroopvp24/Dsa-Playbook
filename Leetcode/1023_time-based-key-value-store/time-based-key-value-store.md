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

## standard_binary_search_2.java
*Style: detailed*

# Deep-Dive: Time-Based Key-Value Store Implementation

## 1. Summary
The `TimeMap` implementation leverages a **multilevel indexing strategy** combining a hash-based lookup for O(1) key access with a sorted dynamic array for O(log N) temporal retrieval. 

The core algorithmic technique relies on the assumption that `set` operations occur with monotonically increasing timestamps (standard for this specific problem definition). Because the `ArrayList` maintains insertion order, the internal data structure remains sorted by `timestamp` naturally, enabling the application of **Binary Search** to perform "floor" lookups for the target `timestamp` in logarithmic time.

---

## 2. Complexity Analysis

### Time Complexity
*   **`set(String key, String value, int timestamp)`**: **O(1)** (amortized). 
    *   The `HashMap` lookup and `ArrayList.add()` operation are O(1) on average.
*   **`get(String key, int timestamp)`**: **O(log N)**.
    *   Where *N* is the number of historical entries associated with a specific key. The binary search mechanism halves the search space in each iteration, leading to logarithmic complexity.

### Space Complexity
*   **O(N + K)**:
    *   Where *N* is the total number of entries across all keys, and *K* is the number of unique keys. Each `Pair` object adds constant overhead for storage, and the `HashMap` stores references to the lists.

---

## 3. Component Deep Dive

### `HashMap<String, List<Pair>>` Structure
This acts as an **Inverted Index**. By partitioning data by `key` first, we limit the search scope during `get()` calls to only the relevant temporal history. Using `computeIfAbsent` ensures minimal boilerplate for lazy initialization of the backing `ArrayList`.

### The Binary Search Algorithm (`get` method)
The implementation employs a **"Lower Bound" variant** of binary search adapted for a "Floor" query:
1.  **State Tracking**: The `result` variable acts as a transient buffer. It is updated whenever `currentTimestamp <= timestamp`.
2.  **Greedy Advancement**: When `currentTimestamp <= timestamp` is true, we store the value and move `left = middle + 1`. This is critical: we know the current `middle` is valid, but there might be a *greater* valid timestamp further to the right. 
3.  **Boundary Conditions**: The `while (left <= right)` loop handles the empty list case gracefully because `timestampedValues.size() - 1` will result in `right` being -1, skipping the loop entirely and returning the initialized `result` ("").

### Custom `Pair<K, V>`
While Java 14+ introduces `record` types, the explicit `Pair` class used here maintains compatibility and encapsulates the historical state. By using `final` fields, the data structure ensures **immutability** of historical records, preventing accidental corruption of past timestamps.

---

## 4. Key Insights & Nuances

### Monotonicity Assumption
This solution implicitly relies on the input providing timestamps in non-decreasing order. If `set()` were called with out-of-order timestamps, the binary search would fail to find the correct "floor" value. 
*   **Optimization Note**: If the requirement shifts to support out-of-order timestamps, you would need to perform `Collections.sort()` or use a `TreeMap<Integer, String>` for each key. However, this would degrade `set()` to **O(N log N)** or **O(log N)** respectively.

### Binary Search Performance
*   **Cache Locality**: Because the data is stored in an `ArrayList` (a contiguous block of memory references), iterating via `middle` index is cache-friendly compared to a `LinkedList` or a `TreeMap` structure, which would involve pointer chasing.
*   **Avoiding Integer Overflow**: The use of `int middle = left + (right - left) / 2;` is a standard best practice to prevent integer overflow that occurs with `(left + right) / 2` when indices are large.

### Memory Overhead
*   Each `Pair` object is an instance on the heap. If the frequency of `set()` operations is extremely high, memory consumption will grow linearly.
*   **Potential Refinement**: For high-scale production systems, consider using primitive collections (e.g., fastutil's `Int2ObjectOpenHashMap`) to reduce object boxing overhead if memory pressure becomes the primary bottleneck.

---
