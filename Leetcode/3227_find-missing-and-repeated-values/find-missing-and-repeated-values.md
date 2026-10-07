# find-missing-and-repeated-values

## attempt_1.java
*Style: detailed*

# 🧩 Deep‑Dive Reference: `findMissingAndRepeatedValues` (Java)

> **Purpose** – Given an *n × n* grid that should contain every integer from `1 … n²` exactly once, locate the **single repeated** value and the **single missing** value.  
> The method returns an `int[2]` where `index 0` = repeated, `index 1` = missing.

---

## 1️⃣ Summary – High‑Level Approach  

| Step | What it does | Why it works |
|------|--------------|--------------|
| **1. Compute `n` & `totalNumbers`** | `n = grid.length`, `totalNumbers = n * n` | The grid is guaranteed square; the full domain of expected values is `[1, n²]`. |
| **2. Frequency array** | Allocate `int[] frequency = new int[totalNumbers + 1]` (1‑based indexing). | Direct‑address table gives O(1) increment per element, no hashing, no collisions. |
| **3. Scan the grid** | Nested loops over rows and columns, increment `frequency[value]`. | Guarantees we count each occurrence exactly once. |
| **4. Identify anomalies** | Linear scan `1 … totalNumbers`. <br> - `frequency == 2` → repeated.<br> - `frequency == 0` → missing. | Because only one number is duplicated and one is omitted, the scan will set both variables exactly once. |
| **5. Return result** | `new int[]{repeatedNumber, missingNumber}`. | API contract satisfied. |

The algorithm is essentially a **count‑sort** (or “frequency‑count”) technique adapted to the specific “one duplicate / one missing” constraint.

---

## 2️⃣ Complexity Analysis  

| Metric | Derivation | Result |
|--------|------------|--------|
| **Time** | - **Frequency build**: `n²` cells → `O(n²)`.<br>- **Post‑scan** over the domain `1 … n²` → `O(n²)`.<br>- Constant‑time work elsewhere. | **Overall** `O(n²)` (linear in the number of cells). |
| **Space** | - Frequency array size `totalNumbers + 1 = n² + 1` → `O(n²)`.<br>- Only a handful of scalar variables (`repeatedNumber`, `missingNumber`). | **Overall** `O(n²)` auxiliary space. |

> **Why not O(1) space?**  
> The problem demands locating *both* the duplicate and the missing value among a known dense range. Without additional constraints (e.g., read‑only, in‑place modification) a direct‑address table of size `n²` is the simplest deterministic way to achieve linear time. Alternative O(1)‑space tricks (xor‑sum, sum‑of‑squares) exist but involve 64‑bit arithmetic and risk overflow for large `n` (since `n²` can be up to ~10⁶ for typical LeetCode limits).  

---

## 3️⃣ Component Deep Dive  

### 3.1 `int n = grid.length;`
*Assumption*: `grid` is a well‑formed square matrix (all rows same length). No validation is performed, which is acceptable under LeetCode‑style contracts but would be a point of defensive programming in production.

### 3.2 `int totalNumbers = n * n;`
*Potential overflow*: If `n` approaches `√Integer.MAX_VALUE` (~46 340), `n * n` would overflow 32‑bit `int`. The method would then allocate a negative‑sized array → `NegativeArraySizeException`. In practice test constraints keep `n` ≤ 10⁴, so safe, but a production version would use `long` for the product and sanity‑check before casting.

### 3.3 Frequency Array Allocation  

```java
int[] frequency = new int[totalNumbers + 1];
```

- **Index 0** is unused; we deliberately keep a 1‑based mapping to avoid off‑by‑one gymnastics when checking `frequency[number]`.
- **Memory layout**: contiguous `int[]` → 4 bytes per entry, total ≈ `4·(n²+1)` bytes. For `n = 10⁴` → ~400 MiB, still within many JVM heap limits but noteworthy.

### 3.4 Grid Traversal  

```java
for (int row = 0; row < n; row++) {
    for (int col = 0; col < n; col++) {
        frequency[grid[row][col]]++;
    }
}
```

- **Safety**: No bounds check on `grid[row][col]`. If a cell contains a value outside `[1, n²]`, an `ArrayIndexOutOfBoundsException` will be thrown. The problem statement guarantees valid input, but a defensive version would verify `v >= 1 && v <= totalNumbers` before incrementing.
- **Cache behavior**: Row‑major iteration matches Java’s internal row layout, giving optimal locality.

### 3.5 Duplicate / Missing Detection  

```java
for (int number = 1; number <= totalNumbers; number++) {
    if (frequency[number] == 2) {
        repeatedNumber = number;
    } else if (frequency[number] == 0) {
        missingNumber = number;
    }
}
```

- **Single‑pass guarantee**: Because there is exactly one duplicate and one missing, the loop will set each variable exactly once.  
- **No early break**: The loop continues scanning to the end even after both values are found. For large `n` this is negligible (`O(n²)` anyway) but could be trimmed to `if (repeatedNumber != 0 && missingNumber != 0) break;` to cut average runtime roughly in half when the anomalies appear early.

### 3.6 Return Value  

```java
return new int[] { repeatedNumber, missingNumber };
```

- Returns a freshly allocated array; callers should treat it as immutable. No reference aliasing issues.

---

## 4️⃣ Key Insights & Gotchas  

| Aspect | Detail |
|--------|--------|
| **Direct‑address vs. mathematical tricks** | The frequency table is the most straightforward solution. Alternative O(1)‑space methods (e.g., using Σx and Σx²) are prone to overflow and require careful handling of signed 64‑bit arithmetic. |
| **Overflow risk** | `int totalNumbers = n * n;` can overflow for `n > √2³¹‑1`. If you anticipate larger grids, compute with `long` and validate before array allocation. |
| **Input validation** | The code trusts the grid to contain only values in `[1, n²]`. Out‑of‑range values cause immediate runtime exceptions. In a library context, wrap the increment in a guard or throw a custom `IllegalArgumentException`. |
| **Memory footprint** | `O(n²)` extra memory may be prohibitive for very large `n`. A streaming approach (e.g., using a BitSet to track visited numbers and a running sum) can reduce to `O(n² / 32)` bits, still linear but a factor of 32 lower. |
| **Early exit optimisation** | Adding a break when both anomalies are found reduces the worst‑case constant factor. |
| **Thread‑safety** | The method is stateless apart from local arrays → inherently thread‑safe. However, the returned array is mutable; callers must not share it across threads without synchronization. |
| **Potential subtle bug** – **duplicate of the *same* number multiple times** | The algorithm assumes exactly one duplicated entry (frequency 2). If the input mistakenly contains a number three times and another missing, the loop will still capture the first `frequency == 2` (which may be the same value) and the missing value, silently ignoring the extra duplication. Validation could be added: `if (frequency[number] > 2) throw ...`. |
| **Negative / zero values** | If a cell contains `0` or a negative number, `frequency[grid[row][col]]` will throw `ArrayIndexOutOfBoundsException`. Again, defensive checks are advisable for non‑trusted input. |
| **Performance micro‑optimizations** | - Use a single `for (int i = 0, len = n * n; i < len; ++i)` loop with a flattened index to avoid the double‑loop overhead (though JVM JIT usually eliminates it).<br>- Reuse a pre‑allocated `int[]` buffer across multiple calls if the method is part of a high‑throughput service, to cut GC pressure. |
| **Testing edge cases** | 1. `n = 1` (grid `[ [1] ]` → both repeated and missing stay `0` – but problem guarantees a duplicate/missing, so such input is invalid).<br>2. Duplicate is the smallest value (`1`) or largest (`n²`).<br>3. Missing value is the smallest or largest. Ensure the algorithm correctly picks them up. |
| **Alternative data structures** | - **BitSet** for visited flags + a variable to remember the duplicate when a set bit is already true.<br>- **Int2IntOpenHashMap** (fastutil) for sparse grids where numbers may be outside the dense range, albeit with higher constant factors. |

---

## 5️⃣ TL;DR – Checklist for Production‑Ready Version  

1. **Validate `n`** – guard against overflow in `n * n`.
2. **Bounds‑check each cell** before using it as an index.
3. **Early‑exit** when both `repeatedNumber` and `missingNumber` are discovered.
4. **Consider memory‑saving alternatives** (BitSet, streaming sum) if `n` can be large.
5. **Add sanity checks** for `frequency > 2` or `frequency < 0` to detect malformed input.
6. **Document contract** clearly: exactly one duplicated and one missing value expected.

---

*Prepared by*: **Senior Staff Engineer** – Deep‑dive reference for algorithmic clarity, performance, and robustness.

---
