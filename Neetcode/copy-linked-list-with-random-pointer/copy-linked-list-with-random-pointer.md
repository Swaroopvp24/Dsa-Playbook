# copy-linked-list-with-random-pointer

## linkedlist_solution(hashMap).java
*Style: detailed*

# Technical Reference: Deep Copy of Linked List with Random Pointers

## Summary
The solution employs a **two-pass Hash-Map-based mapping strategy** to perform a deep copy of a singly linked list where nodes contain an additional "random" pointer. This technique decouples the node instantiation process from the structural linkage process, effectively resolving the circular dependency problem inherent in graph-like linked structures. By using the original node reference as the key in a `HashMap`, we achieve $O(1)$ lookup for the corresponding cloned node during the pointer assignment phase.

## Complexity Analysis

### Time Complexity: $O(N)$
*   **First Pass:** $N$ iterations to instantiate each node and populate the `HashMap`. Each `Map.put()` operation is $O(1)$ on average.
*   **Second Pass:** $N$ iterations to establish `next` and `random` links. Each `Map.get()` operation is $O(1)$ on average.
*   **Total:** $O(N + N) = O(N)$, where $N$ is the number of nodes in the list.

### Space Complexity: $O(N)$
*   The `HashMap` stores exactly $N$ entries, where each entry consists of an object reference mapping (original `Node` to cloned `Node`).
*   Additional space for the cloned list is proportional to $N$, but in strict terms of auxiliary space, the Map is the primary consumer.

## Component Deep Dive

### 1. The Mapping Strategy (`HashMap<Node, Node>`)
The core of the algorithm is the identity-based mapping. In Java, `HashMap` uses the object's `hashCode()` and `equals()` methods. Since `Node` does not override these, the `HashMap` relies on default reference equality (the memory address). This is exactly what is required to ensure that each unique node in the original structure is mapped to one—and only one—corresponding cloned node.

### 2. Phase 1: Decoupled Instantiation
By iterating through the original list first, we ensure that every target node exists in the `HashMap` before we attempt to link them. This prevents `NullPointerException` errors during the second pass when a `random` pointer might refer to a node that hasn't been instantiated yet.

### 3. Phase 2: Pointer Reconstruction
We traverse the original list a second time. For every `current` node:
*   `copiedNode.next = originalToCopy.get(current.next)`: If `current.next` is `null`, `get()` returns `null`, correctly terminating the list.
*   `copiedNode.random = originalToCopy.get(current.random)`: Similarly, if `current.random` is `null`, the copy’s random pointer remains `null`.

## Key Insights

*   **Handling Nulls:** The `HashMap.get()` method gracefully returns `null` if the key is `null`. This is critical, as it eliminates the need for explicit conditional checks (e.g., `if (current.random != null)`) when assigning pointers.
*   **Space-Time Tradeoff:** The `HashMap` approach is highly readable and idiomatic. However, it is possible to achieve $O(1)$ auxiliary space by interleaving the cloned nodes into the original list (i.e., `A -> A' -> B -> B'`). While that approach reduces memory overhead, it complicates the logic and is non-thread-safe if the list is being accessed concurrently.
*   **Garbage Collection Considerations:** Because the `HashMap` holds strong references to all cloned nodes, they will not be garbage collected until the `HashMap` itself goes out of scope. In memory-constrained systems, ensuring the `originalToCopy` map is cleared or falls out of scope immediately after the method returns is important.
*   **Edge Case Handling:**
    *   **Empty List:** If `head` is `null`, the `while` loops are skipped, and `originalToCopy.get(null)` returns `null`, correctly returning an empty structure.
    *   **Single Node:** If a node points to itself (`random` points to `self`), the logic holds because `originalToCopy.get(current)` will retrieve the same cloned object for the random pointer.

---
