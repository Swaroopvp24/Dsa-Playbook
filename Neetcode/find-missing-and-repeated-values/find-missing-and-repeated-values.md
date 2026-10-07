# find-missing-and-repeated-values

## attempt_1.java
*Style: detailed*

## 📚 Deep‑Dive Reference – “Find Missing and Repeated Values” (Java)

> **File**: `Solution.java`  
> **Method**: `int[] findMissingAndRepeatedValues(int[][] grid)`  

---

### 1️⃣ Summary – What the code does & the core algorithmic idea  

The routine receives an **n × n** matrix `grid` that should contain **every integer from 1 to n² exactly once** – i.e. a permutation of the set `{1,…,n²}`.  
Unfortunately one value appears **twice** (the *repeated* number) and consequently one value is **absent** (the *missing* number).  

The algorithm solves the problem in two passes:

1. **Counting Pass** – build a frequency table `frequency[1…n²]` that records how many times each value occurs while scanning the matrix once.  
2. **Scanning Pass** – walk the frequency table once to locate the entry with count `2` (the repeated value) and the entry with count `0` (the missing value).  

Both passes are linear with respect to the total number of cells, `n²`.  
The technique is a classic **count‑array / bucket‑sort** approach, trading **O(n²)** auxiliary space for a **single‑scan** detection of the anomaly.

---

### 2️⃣ Complexity Analysis  

| Aspect | Derivation | Result |
|--------|------------|--------|
| **Time** | • Outer loop iterates over every matrix cell → `n·n = n²` operations. <br>• Second loop iterates over the `totalNumbers = n²` entries of the frequency array. | **O(n²)** overall. |
| **Space** | • Frequency array size = `totalNumbers + 1 = n² + 1`. <br>• No other data structures grow with `n`. | **O(n²)** auxiliary space. |
| **Why it isn’t sub‑quadratic** | The input itself contains `n²` numbers; any algorithm must at least look at each element once, giving a lower bound of Ω(n²). The solution meets this lower bound. |
| **Potential overflow** | The code never accumulates sums, only increments counters that stay ≤ 2 (by problem constraints). No risk of integer overflow. |

---

### 3️⃣ Component Deep Dive  

#### 3.1 Variables & Data Structures  

| Symbol | Meaning | Type / Size | Comments |
|--------|----------|------------|----------|
| `n` | Matrix dimension (`grid.length`) | `int` | Assumes a square matrix (`grid[i].length == n`). |
| `totalNumbers` | `n * n` – total distinct values expected | `int` | Upper bound of values (also max index for `frequency`). |
| `frequency` | Count of each integer `1 … n²` | `int[ totalNumbers + 1 ]` | Index `0` is unused to keep the mapping 1‑based, simplifying the later scan. |
| `repeatedNumber`, `missingNumber` | Result placeholders | `int` | Initialized to `0`; will be overwritten exactly once if the input obeys the “one duplicate, one missing” guarantee. |
| `grid[row][col]` | Current cell value | `int` | Expected to be within `[1, totalNumbers]`. No validation is performed. |

#### 3.2 First Pass – Populating `frequency`

```java
for (int row = 0; row < n; row++) {
    for (int col = 0; col < n; col++) {
        frequency[grid[row][col]]++;
    }
}
```

* **What it does**: Directly indexes the frequency array with the cell value and increments the counter.  
* **Edge‑case handling**:  
  * **Out‑of‑range value** (e.g., `0` or `> n²`) → `ArrayIndexOutOfBoundsException`. The method **assumes** the caller respects the contract.  
  * **Duplicate beyond two occurrences** – the algorithm still works; the repeated number will be the one whose counter ends up `>1`. However the later detection logic only checks `== 2`, so a triple duplicate would be silently ignored (bug).  

#### 3.3 Second Pass – Extracting the anomalies

```java
for (int number = 1; number <= totalNumbers; number++) {
    if (frequency[number] == 2) {
        repeatedNumber = number;
    } else if (frequency[number] == 0) {
        missingNumber = number;
    }
}
```

* **Why `if … else if`?**  
  * The two conditions are mutually exclusive (a number cannot be both missing and repeated).  
  * The loop continues even after both values are found, overwriting them only if the invariant is broken (e.g., more than one duplicate/missing).  
* **Guarantee reliance**: The problem statement guarantees **exactly one** repeated and **exactly one** missing element, ensuring the final values are correct.  

#### 3.4 Return value  

```java
return new int[] { repeatedNumber, missingNumber };
```

A fresh two‑element array is allocated (O(1) extra memory) and returned in the order **[repeated, missing]**, matching typical interview expectations.

---

### 4️⃣ Key Insights – Subtleties, Optimisations & Pitfalls  

| Area | Insight / Nuance |
|------|------------------|
| **Space‑vs‑Time Trade‑off** | The counting array gives a **single‑pass detection** but costs O(n²) extra memory. For very large `n` (e.g., `n = 10⁴` → 100 M entries → ~400 MB), this may be prohibitive. |
| **In‑place alternatives** | • **Sign‑flipping**: iterate the matrix, treat each value `v` as an index `v‑1` into the flattened array, and negate the element at that index. A second pass identifies the positive entry (missing) and the index that gets flipped twice (repeated). <br>• **Arithmetic method**: compute `sum(grid) - expectedSum` (gives `repeated - missing`) and `sumSq(grid) - expectedSumSq` (gives `repeated² - missing²`). Solve the two equations for the two unknowns – O(1) extra space, O(n²) time, but prone to overflow for large `n`. |
| **Robustness to malformed input** | The current implementation will **crash** on out‑of‑range values. Defensive programming could add: <br>```java if (v < 1 || v > totalNumbers) throw new IllegalArgumentException(...); ``` |
| **Multiple anomalies** | If the grid contains **more than one duplicate** or **more than one missing** number, the loop will return the *last* pair it encounters, which is **undefined behaviour**. A production‑grade version should detect `frequency[number] > 2` or count how many `0`s appear, and raise an error. |
| **Cache friendliness** | The frequency array fits into a contiguous block of memory; the double‑nested loop accesses `grid` row‑major order, which is cache‑optimal for Java’s 2‑D array layout (arrays of arrays). No further micro‑optimisations are necessary unless `n` is extremely large. |
| **Potential for early exit** | After the first pass, we could keep a running count of how many entries have been seen twice or zero times, and break the second loop as soon as both are found. This would reduce the *average* runtime when `n` is huge, though worst‑case remains O(n²). |
| **Thread‑safety** | The method is **pure** – it does not modify the input matrix and uses only local variables, making it safe for concurrent invocations. |
| **Testing edge cases** | • `n = 1` (grid = `[[1]]`) – there is no duplicate/missing; the algorithm returns `{0,0}` – arguably an invalid result. <br>• Minimum valid `n` with a duplicate is `n = 2`. <br>• All values present but shuffled – still returns `{0,0}` because no count hits `2` or `0`. Test harness should enforce preconditions. |
| **Complexity sanity check** | The algorithm’s runtime is bounded by the input size (`n²`). Any algorithm that *does not* look at every cell cannot guarantee detection of a misplaced value, confirming that O(n²) is optimal in the comparison‑based model. |

---

### 5️⃣ TL;DR – Cheat Sheet  

| Step | Action | Cost |
|------|--------|------|
| 1 | Allocate `int[ n² + 1 ]` frequency array. | **O(n²) space** |
| 2 | Iterate matrix once, increment `frequency[value]`. | **O(n²) time** |
| 3 | Scan `frequency[1…n²]` to locate `count == 2` (repeated) and `count == 0` (missing). | **O(n²) time** (but only `n²` checks) |
| 4 | Return `[repeated, missing]`. | **O(1) extra** |

**Overall:** `Time = Θ(n²)`, `Space = Θ(n²)`.

---

### 6️⃣ Suggested Refactor (optional)  

If memory is a concern, replace the frequency array with the **XOR‑based** or **sum‑of‑squares** approach:

```java
public int[] findMissingAndRepeatedValues(int[][] grid) {
    int n = grid.length;
    long total = (long) n * n;
    long expectedSum = total * (total + 1) / 2;
    long expectedSqSum = total * (total + 1) * (2 * total + 1) / 6;

    long actualSum = 0, actualSqSum = 0;
    for (int[] row : grid) {
        for (int v : row) {
            actualSum += v;
            actualSqSum += (long) v * v;
        }
    }

    long diff = actualSum - expectedSum;          // repeated - missing
    long sqDiff = actualSqSum - expectedSqSum;    // repeated² - missing²

    long sum = sqDiff / diff;                     // repeated + missing
    long repeated = (diff + sum) / 2;
    long missing  = sum - repeated;

    return new int[]{(int) repeated, (int) missing};
}
```

* **Pros:** O(1) extra space, still O(n²) time.  
* **Cons:** Requires `long` to avoid overflow; still assumes exactly one duplicate/missing.

--- 

**End of document**.

---
