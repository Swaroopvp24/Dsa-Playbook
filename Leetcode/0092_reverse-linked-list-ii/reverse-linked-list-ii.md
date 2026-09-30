# reverse-linked-list-ii

## linkedlist_solution.java
*Style: detailed*

# Engineering Deep-Dive: In-Place Sublist Reversal

## Summary
The `reverseBetween` implementation employs an **in-place pointer manipulation** technique to reverse a specific segment $[left, right]$ of a singly-linked list. By utilizing a "dummy head" pattern, the algorithm eliminates conditional branching for edge cases involving the list head (where $left = 1$). The process is decomposed into two phases: **Locate Phase** (identifying the segment boundaries) and **Reversal Phase** (iterative link redirection).

## Complexity Analysis

### Time Complexity: $O(N)$
*   **Locate Phase:** We perform a single traversal up to index `right`. In the worst case, $right = N$, resulting in $O(N)$ operations.
*   **Reversal Phase:** We iterate over the segment of length $K = (right - left + 1)$. This is at most $O(N)$.
*   **Total:** $O(N) + O(K) \approx O(N)$, where $N$ is the total number of nodes in the list.

### Space Complexity: $O(1)$
*   The algorithm operates strictly **in-place**. We only maintain a constant number of pointers (`dummy`, `leftPrev`, `leftNode`, `rightNode`, `previous`, `currentNode`, `nextNode`), regardless of the input size $N$. No additional data structures (like stacks or recursion buffers) are utilized.

---

## Component Deep Dive

### 1. The Sentinel (Dummy) Node
The use of `dummy` is critical. It acts as a stable reference point (`dummy.next`) that persists even if the reversal begins at the head of the list ($left=1$). This avoids the "special case" logic typically required to handle head modifications, ensuring `leftPrev` is always a valid node object.

### 2. The Locate Phase
*   **Logic:** The algorithm iterates up to `right` to capture the three critical nodes:
    *   `leftPrev`: The anchor that connects to the new head of the sublist.
    *   `leftNode`: The node that will become the *tail* of the reversed sublist.
    *   `rightNode`: The node that will become the *head* of the reversed sublist.
*   **Edge Case:** If $left=1$, `leftPrev` correctly defaults to the `dummy` node.

### 3. The Reversal Phase (Pointer Reversal)
*   **Technique:** The implementation uses a modified iterative reversal. 
*   **Initialization Trick:** By initializing `previous` to `nodeAfter` (the node at `right + 1`) rather than `null`, the algorithm performs a "pre-wiring." As the loop proceeds, each node in the segment is redirected to the `previous` node. When the loop finishes, `rightNode.next` is already pointing to `nodeAfter`, satisfying the sublist-to-tail connection requirement automatically.

---

## Key Insights & Nuances

### Subtle Efficiency Gain
The decision to initialize `previous = nodeAfter` is a performance and logic optimization. In a standard reversal, you have to manually relink the head of the reversed segment to the remainder of the list. Here, the initialization essentially completes the tail-linkage concurrently with the reversal, reducing the number of post-reversal operations.

### Failure Modes & Safety
*   **Bounds Violation:** The provided code assumes $1 \le left \le right \le N$. If $right$ exceeds the list length, `rightNode.next` would be `null` and `rightNode` would become `null`, causing a `NullPointerException`. In a production environment, an additional check for `current == null` during the locate phase is advisable.
*   **Single-Pass Optimization:** While the current implementation traverses the list to find the nodes and then traverses the sublist again, this is strictly $O(N)$. For very large lists, one could optimize by merging the Locate and Reversal phases into a single pass using a "sliding window" pointer approach, though this significantly increases the cognitive complexity and potential for off-by-one errors. 

### Implementation Trap
Avoid modifying `leftNode.next` before the entire reversal is finished; otherwise, you lose the reference to the rest of the sublist. The current implementation correctly handles this by tracking `nextNode` in each iteration of the `while` loop, preserving the path forward.

---

## linkedlist_optimal_solution.java
*Style: detailed*

# Engineering Reference: In-Place Linked List Sub-Section Reversal

## 1. Summary
The implementation employs a **single-pass, pointer-manipulation strategy** to reverse a sub-segment of a singly linked list in-place. Instead of partitioning the list and reconstructing it, the algorithm maintains a fixed anchor point (`beforeReversal`) and iteratively "bubbles" subsequent nodes of the segment to the position immediately following the anchor. This effectively performs a series of localized head-insertions, converting the target sub-list into a reversed sequence without auxiliary memory allocation.

## 2. Complexity Analysis

### Time Complexity: $O(N)$
*   **Traversal:** The algorithm performs a linear traversal to reach the `left` index ($O(L)$) and performs $R-L$ iterations of pointer swaps.
*   **Efficiency:** Each node within the target range is visited a constant number of times. Total operations remain proportional to the number of nodes in the list, resulting in a strict $O(N)$ bound.

### Space Complexity: $O(1)$
*   **Constant Overhead:** The solution utilizes a fixed number of pointers (`dummy`, `beforeReversal`, `start`, `nodeToMove`), regardless of the input list size. 
*   **In-Place Transformation:** No recursive stack space or additional data structures (like arrays or lists) are used. The input list is modified strictly by updating node references.

## 3. Component Deep Dive

### A. The Dummy Node Strategy
The `dummy` node is critical for **boundary abstraction**. By initializing `dummy.next = head`, we ensure that the logic for the "node before the reversal range" remains consistent even when `left == 1`. This eliminates the need for conditional logic (e.g., checking if the list head itself is being replaced) and maintains structural uniformity in pointer assignments.

### B. The "Bubble-Up" Mechanism
The core logic relies on these three operations within the loop:
1.  **Isolation:** `start.next = nodeToMove.next;` disconnects the node to be moved from its current predecessor, effectively bypassing it to link the `start` node to the subsequent element.
2.  **Insertion:** `nodeToMove.next = beforeReversal.next;` connects the floating node into the list at the head of the segment.
3.  **Anchoring:** `beforeReversal.next = nodeToMove;` updates the anchor point to point to the newly shifted node.

### C. Variable State Tracking
*   **`beforeReversal`**: Remains static throughout the transformation loop. It acts as the "gatekeeper" of the reversed segment.
*   **`start`**: Also remains static after the initial setup. Because the sub-list is being reversed by pulling elements from *after* `start` to *before* `start`, `start` effectively becomes the **tail** of the reversed sub-segment.

## 4. Key Insights

### Sub-segment Invariants
*   At every iteration of the `for` loop, the node currently referenced by `beforeReversal.next` is the most recently moved node, and `start.next` is always the node that will be moved in the *next* iteration.
*   The termination condition `i < right - left` accurately reflects that for a segment of size $K$, we require $K-1$ adjustments to finalize the reversal.

### Potential Pitfalls & Nuances
1.  **Pointer Leaks:** Note the `nodeToMove = start.next;` statement at the end of the loop. If `start.next` happens to be null (if the loop was calculated incorrectly), this will throw a `NullPointerException`. The current loop bounds correctly prevent this.
2.  **Range Validity:** The code assumes $1 \le left \le right \le \text{length}$. In a production environment, you should add validation guards for inputs where `left > right` or where indices exceed list bounds to prevent unexpected behavior.
3.  **Memory Model:** Since this code performs in-place mutation, it is **not thread-safe** if another thread is traversing the list. In a concurrent system, this operation would require external synchronization or a read-write lock on the list head.
4.  **Reference Stability:** The `start` node maintains its memory address throughout the reversal process; it essentially "falls back" to the end of the reversed sub-segment as nodes are pulled in front of it. This is a common pattern in list manipulation that avoids the complexity of manual pointer tracking for multiple moving variables.

---
