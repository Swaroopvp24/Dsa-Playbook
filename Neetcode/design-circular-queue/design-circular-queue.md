# design-circular-queue

## linkedlist_solution(single linked list).java
*Style: detailed*

# 📘 Deep‑Dive Reference – `MyCircularQueue` (Linked‑List Implementation)

---

## 1. Summary – What the code does & the algorithmic technique  

The class implements a **fixed‑capacity circular queue** using a **singly‑linked list** (`ListNode`).  
* `enQueue` appends a node at the **tail** (`rear`).  
* `deQueue` removes a node from the **head** (`front`).  

The “circular” aspect is **logical only** – the data structure does **not** link the tail back to the head.  
Instead, the queue enforces capacity via a `size` counter and rejects operations when `size == capacity`.  

Thus the algorithmic technique is a **FIFO (first‑in‑first‑out) queue** realized with **O(1) head/tail pointer updates** and a **bounded size** check.

---

## 2. Complexity Analysis  

| Operation | Time Complexity | Reasoning |
|-----------|----------------|-----------|
| `enQueue` | **O(1)** | Constant‑time allocation of a `ListNode` and two pointer assignments (`rear.next`, `rear`). Capacity check and `size++` are O(1). |
| `deQueue` | **O(1)** | Only moves `front` to `front.next`, decrements `size`, and possibly nulls `rear`. No traversal. |
| `Front` / `Rear` | **O(1)** | Direct field read, guarded by `isEmpty`. |
| `isFull` / `isEmpty` | **O(1)** | Simple integer comparison. |
| Memory usage (steady state) | **O(k)** where `k = capacity` | At most `capacity` `ListNode` objects exist simultaneously (each node stores an `int` and a reference). The overhead of the two pointers (`front`, `rear`) and the counters is O(1). |

**Why no hidden cost?**  
- No resizing, no array copying, no iteration over the linked list.  
- The only allocation is the newly enqueued node; de‑allocation is delegated to the GC when a node becomes unreachable after `deQueue`.  

---

## 3. Component Deep Dive  

### 3.1 Data Structures  

| Field | Type | Role |
|-------|------|------|
| `size` | `int` | Current number of elements (0 … capacity). |
| `capacity` | `final int` | Upper bound; immutable after construction. |
| `front` | `ListNode` | Reference to the node that will be dequeued next (head). |
| `rear` | `ListNode` | Reference to the most recently enqueued node (tail). |
| `ListNode` | inner class (`int value`, `ListNode next`) | Minimal singly‑linked node; no sentinel. |

*Why singly linked?*  
Only head and tail need to be touched; a doubly‑linked list would add unnecessary memory per node.

### 3.2 Core Methods  

#### `MyCircularQueue(int k)`  

```java
public MyCircularQueue(int k) {
    capacity = k;
    size = 0;
    front = null;
    rear = null;
}
```

- Sets immutable capacity, zeroes size, and clears both pointers.  
- No sentinel node → `front == null` ⇔ empty queue.

#### `boolean enQueue(int value)`

```java
if (isFull()) return false;
ListNode newNode = new ListNode(value);
if (isEmpty()) { front = rear = newNode; }
else { rear.next = newNode; rear = newNode; }
size++;
return true;
```

- **Full‑check** (`size == capacity`) prevents overflow.  
- **First insertion** sets both `front` and `rear`.  
- **Subsequent insertion** updates only the tail's `next` and the `rear` reference.  
- **Complexity**: O(1).  
- **Edge Cases** handled:
  - Inserting into an empty queue (both pointers become the new node).  
  - Inserting when `size == capacity` (rejected early).  

#### `boolean deQueue()`

```java
if (isEmpty()) return false;
front = front.next;
size--;
if (size == 0) rear = null;
return true;
```

- **Empty‑check** prevents underflow.  
- Moves `front` forward; the old head becomes unreachable → GC.  
- When the last element is removed (`size` drops to 0), **both** pointers are cleared to keep the “empty” invariant (`front == null && rear == null`).  
- **Complexity**: O(1).  

#### `int Front()` / `int Rear()`

```java
return isEmpty() ? -1 : front.value;   // Rear analogous
```

- Returns sentinel `-1` on empty (as required by LeetCode problem).  
- No side‑effects.

#### `boolean isFull()` / `boolean isEmpty()`

```java
return size == capacity;   // Full
return size == 0;          // Empty
```

- Direct integer comparisons; O(1).

### 3.3 Edge‑Case & Defensive Considerations  

| Situation | Current Handling | Potential Pitfall if omitted |
|-----------|------------------|------------------------------|
| `enQueue` on full queue | Early `return false` | Would overflow `size` and break `isFull`. |
| `deQueue` on empty queue | Early `return false` | `front.next` would NPE. |
| Removing the last element | `if (size == 0) rear = null;` | `rear` would still point to a stale node, causing `Rear()` to return a value that is no longer in the queue. |
| `Front` / `Rear` on empty | Return `-1` | Caller expects sentinel; returning `0` (default int) would be ambiguous. |
| `capacity <= 0` | Not validated | Queue would accept any `enQueue` because `size == capacity` is true only when both are 0; the constructor should guard against non‑positive capacity for production use. |

---

## 4. Key Insights & Gotchas  

### 4.1 “Circular” is Misnomer  
- The implementation **does not** link `rear.next` back to `front`.  
- The “circular” property is enforced purely by the `size` check.  
- This is fine for the LeetCode contract but **not** a true circular buffer; a genuine ring buffer would reuse the underlying array slots, eliminating per‑node allocation.

### 4.2 Memory‑Allocation Cost  
- Each `enQueue` incurs a **heap allocation** for a `ListNode`. In high‑throughput scenarios this can lead to GC pressure.  
- An array‑based circular buffer would be **more cache‑friendly** and avoid allocations.

### 4.3 Thread‑Safety  
- No synchronization; concurrent calls will corrupt `size`, `front`, `rear`.  
- To make it thread‑safe, wrap each public method in a `synchronized` block or use `java.util.concurrent.locks`.  

### 4.4 Capacity Validation (Robustness)  
- Adding a guard:

```java
if (k <= 0) throw new IllegalArgumentException("capacity must be > 0");
```

prevents misuse and clarifies contract.

### 4.5 Potential Subtle Bug – Leaking Nodes on `deQueue`  
- The implementation discards the old head by moving `front`.  
- The node object becomes eligible for GC **only** after the method returns, which is fine.  
- However, if external code retains a reference to the removed node (e.g., via reflection), the queue would still think it’s gone, but the node would linger—this is a **memory leak** edge case only in hostile environments.

### 4.6 Extensibility – Adding a `peekRear()`  
- Already provided via `Rear()`. If we wanted to support a **pop‑rear** operation, we’d need a **doubly‑linked list** or maintain a **prev‑pointer** to the node before `rear`, otherwise O(n) traversal would be required.

### 4.7 Complexity Trade‑offs vs. Array Implementation  

| Metric | Linked‑List version | Array (ring buffer) version |
|--------|--------------------|-----------------------------|
| Enqueue/Dequeue time | O(1) (pointer updates) | O(1) (index arithmetic) |
| Memory overhead per element | `Object header` + `int` + `reference` | Only `int` (or generic) |
| Cache locality | Poor (nodes scattered) | Excellent (contiguous) |
| GC pressure | High (alloc/free per op) | Low (pre‑allocated) |
| Code simplicity | Very simple, no modulo arithmetic | Slightly more arithmetic, but still trivial |

Choosing linked‑list is acceptable for interview‑style correctness, but **production‑grade circular queues** almost always use an array.

---

## 5. Reference Summary (One‑Liner)

> The `MyCircularQueue` class implements a bounded FIFO queue with O(1) en‑ and de‑queue operations via a singly‑linked list, using a `size` counter to enforce capacity rather than true circular linking; it is memory‑intensive, non‑thread‑safe, and best suited for correctness‑focused scenarios rather than high‑performance production use.  

---
