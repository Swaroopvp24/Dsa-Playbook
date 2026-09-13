# time-based-key-value-store

## standard_binary_search_1.java
*Style: detailed*

# Engineering Design Document: Time-Series Key-Value Store

## 1. Summary
The `TimeMap` implementation provides a versioned key-value store where multiple values can be associated with a single key, each indexed by a strictly increasing timestamp. The system utilizes an **inverted index approach** mapping a `String` key to an `ArrayList` of `TimeValue` objects. 

The core algorithmic strategy for retrieval is **Binary Search** over the temporal dimension. Since `set` operations are assumed to append entries in monotonically increasing order of timestamps (a common constraint in such systems), the internal list for each key remains sorted, enabling $O(\log N)$ retrieval complexity.

---

## 2. Complexity Analysis

### Time Complexity
*   **`set(key, value, timestamp)`**: **$O(1)$ amortized.** 
    *   The `HashMap` lookup/insertion is $O(1)$ on average. `ArrayList.add()` is $O(1)$ amortized, assuming no array resizing overhead.
*   **`get(key, timestamp)`**: **$O(\log N)$**, where $N$ is the number of entries associated with the specific key.
    *   Binary search traverses the `ArrayList` by halving the search space in each iteration, resulting in logarithmic complexity relative to the number of historical values stored for that specific key.

### Space Complexity
*   **$O(K \cdot N)$**, where $K$ is the number of unique keys and $N$ is the average number of timestamps per key.
    *   The structure maintains an object (`TimeValue`) for every insertion. This is space-optimal for a system requiring exact historical point-in-time recovery, as we store $N$ distinct entries.

---

## 3. Component Deep Dive

### Data Structure: `HashMap<String, ArrayList<TimeValue>>`
We use a `HashMap` to achieve near-instantaneous bucket selection for the key. Each bucket contains an `ArrayList`, which acts as a dense, contiguous representation of chronological data. This is cache-friendly compared to a `LinkedList` or a `TreeMap`, as the underlying array stores references to `TimeValue` objects contiguously in memory.

### Binary Search Strategy (`get` method)
The `get` operation implements a variation of binary search specifically designed to find the **floor** of a target value:
1.  **Search Space**: The range $[0, \text{size}-1]$.
2.  **State Maintenance**: We maintain `latestValidIndex` to track the "best" candidate found so far.
3.  **The Invariant**: If `values.get(middle).timestamp <= timestamp`, the current index is a candidate. We greedily move the `left` pointer (`middle + 1`) to see if a *larger* valid timestamp exists further to the right. 
4.  **Termination**: If the `middle` timestamp exceeds the input, we prune the right side of the search space (`right = middle - 1`).

### Edge Case Handling
*   **Non-existent keys**: `timeMap.get(key)` returns `null`, handled explicitly at the start of `get`.
*   **Timestamp earlier than all records**: The binary search will never satisfy `values.get(middle).timestamp <= timestamp`, `latestValidIndex` remains `-1`, and the method correctly returns `""`.
*   **Empty inputs**: If `set` is never called, `values` remains `null`. If `set` is called, the `ArrayList` will be initialized.

---

## 4. Key Insights & Nuances

### 1. Monotonicity Assumption
This implementation relies on the assumption that timestamps passed to `set` for a given key are **strictly non-decreasing**. If a caller passes timestamps out of order, the internal `ArrayList` will not be sorted, and the binary search logic will fail. If unordered input is expected, we would need to either sort the list ($O(N \log N)$ during `get`) or use a `TreeMap<Integer, String>` which maintains internal sorting at the cost of higher memory overhead (node-based pointers).

### 2. Cache Locality
Using an `ArrayList` is significantly faster than using a `TreeMap` in Java. `TreeMap` (Red-Black Tree) involves pointer chasing through nodes scattered in heap memory, leading to potential cache misses. `ArrayList` provides better locality for the binary search process.

### 3. Potential for Optimization
*   **Primitive Types**: The current `TimeValue` class uses an `int` for timestamp and an `Object` for the value. If memory pressure is high, one could use two primitive arrays (`int[] timestamps`, `String[] values`) per key to eliminate the overhead of the `TimeValue` object wrapper.
*   **Initial Capacity**: If the frequency of `set` operations per key is known, initializing `ArrayList` with a specific capacity in `computeIfAbsent` can reduce re-allocation overhead.

### 4. Subtle Bug: `null` return
The `get` method returns an empty string `""` for missing data. If the application environment allows `""` as a valid user-defined value, there is an ambiguity between "value not found" and "value is empty." In a production system, returning `Optional<String>` or throwing a custom `NotFoundException` might be more robust.

---

## standard_binary_search_2.java
*Style: detailed*

# Technical Reference: `TimeMap` Implementation

## Summary
The `TimeMap` implements a versioned key-value store supporting non-strictly increasing timestamp queries. It utilizes a **hash-map backed adjacency list** structure, where each key maps to an `ArrayList` of `Pair<Timestamp, Value>`. 

The core algorithmic strategy relies on the **monotonicity of timestamps** (assuming `set` operations for a single key are called with non-decreasing timestamps). By maintaining a sorted list of pairs per key, the `get` operation is reduced to an **Upper Bound Binary Search** (or "floor" search), allowing for logarithmic retrieval of versioned state.

---

## Complexity Analysis

### Time Complexity
*   **`set(key, value, timestamp)`**: **O(1)** amortized. 
    *   The `HashMap` provides O(1) access. Appending to an `ArrayList` is O(1) amortized, provided no internal array resizing occurs.
*   **`get(key, timestamp)`**: **O(log N)**, where $N$ is the number of historical values associated with the specific key.
    *   The algorithm performs a binary search over the `ArrayList`. Since the search space is halved every iteration, the complexity is logarithmic relative to the history length of that key.

### Space Complexity
*   **O(K + V)**, where $K$ is the number of unique keys and $V$ is the total number of `set` operations performed across all keys. 
    *   Each unique key consumes memory for the map entry and its associated `ArrayList`. Each `set` invocation creates one `Pair` object and stores it in the list.

---

## Component Deep Dive

### 1. Storage Schema (`Map<String, List<Pair<...>>>`)
*   **Design Choice**: Using `ArrayList` is optimal here because we require index-based access for binary search. A `LinkedList` would degrade `get()` to $O(N)$.
*   **Memory Overhead**: `Pair` objects are heap-allocated. For massive datasets, consider primitive arrays (e.g., `int[]` for timestamps and `String[]` for values) to improve cache locality and reduce object header overhead.

### 2. Search Logic (`get` method)
*   **The Binary Search Pattern**: The algorithm uses a "Floor Search." 
    *   When `currentTimestamp <= timestamp`, we record the value and perform `left = middle + 1`. This is the critical step: we greedily move to the right half of the search space to find the *largest* valid timestamp, effectively performing a right-biased binary search.
*   **Boundary Conditions**:
    *   **Key Miss**: If the key does not exist, `getOrDefault` returns an empty list, and the `while` loop condition `left <= right` is immediately false, returning the default `""`. This elegantly avoids null pointer exceptions.
    *   **Timestamp Miss**: If all stored timestamps are greater than the query timestamp, `result` remains an empty string, which is the expected behavior.

---

## Key Insights

### The "Non-Decreasing" Assumption
This implementation implicitly assumes that for a given key, `set` operations occur with monotonically increasing timestamps. 
*   **If timestamps arrive out of order**: The binary search will return incorrect results because the `ArrayList` will not be sorted. To support unsorted inputs, one would need to call `Collections.sort()` after every `set`, degrading performance to $O(N \log N)$ or $O(N)$ (if using Timsort on nearly sorted data).

### Potential Optimizations
1.  **Memory Pooling**: If the `TimeMap` experiences high churn, object pooling for `Pair` objects can reduce GC pressure.
2.  **Primitive Collections**: Use libraries like *fastutil* or *Trove* for `IntObjectMap` structures to avoid boxing `Integer` timestamps into `Pair` objects, significantly reducing the memory footprint.
3.  **Concurrency**: The current implementation is **not thread-safe**. In a multi-threaded environment, replacing `HashMap` with `ConcurrentHashMap` and the internal `ArrayList` with a thread-safe variant or using `ReentrantReadWriteLock` would be necessary. 

### Subtle Pitfalls
*   **`computeIfAbsent`**: This is efficient, but note that it performs an atomic check-and-insert on the map. This is safer than manual `containsKey` checks, which are susceptible to race conditions in concurrent contexts (even though this specific implementation is not thread-safe).
*   **Array Resizing**: The `ArrayList` uses an internal array that grows exponentially. During extreme high-volume `set` operations, the reallocation and copying cost may cause transient latency spikes. If the number of updates per key is known in advance, initializing the `ArrayList` with a specific capacity can mitigate this.

---
