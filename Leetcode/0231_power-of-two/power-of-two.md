# power-of-two

## standard_recursion_solution.java
*Style: detailed*

# Deep‑Dive Reference:  `isPowerOfTwo` – Recursive Power‑of‑Two Test  

---  

## 1. Summary  

The solution determines whether a signed 32‑bit integer `n` is a power of two **using pure recursion**:

1. **Base cases**  
   * `n <= 0` → `false` (non‑positive numbers cannot be powers of two).  
   * `n == 1` → `true` (2⁰).  

2. **Parity test**  
   * If `n` is odd (`n % 2 != 0`) → `false`.  

3. **Recursive step**  
   * Return `checkPow(n / 2)`.  

The recursion repeatedly halves `n`. If the halving process reaches exactly `1` without ever encountering an odd intermediate value, `n` must be a power of two; otherwise it is not.

The public entry point `isPowerOfTwo` simply forwards the argument to the private helper `checkPow`.

---  

## 2. Complexity Analysis  

| Metric | Derivation | Result |
|--------|------------|--------|
| **Time** | Each recursive call performs a constant amount of work: one modulo (`%`), one comparison, and one division (`/`). The number of calls equals the number of times we can halve `n` before reaching `1`, i.e. `⌊log₂ n⌋ + 1`. | **O(log n)** |
| **Space (auxiliary)** | The recursion depth is the same as the number of calls because each call keeps a stack frame alive until the deeper call returns. No additional heap structures are allocated. | **O(log n)** stack space |
| **Overall (including input)** | Input `n` is a single `int`; no extra containers. | **O(log n)** auxiliary, **O(1)** total besides recursion stack |

*Why `log₂ n`?*  
If `n = 2ᵏ`, the recursion sequence is `2ᵏ → 2ᵏ⁻¹ → … → 2 → 1`, exactly `k+1 = ⌊log₂ n⌋ + 1` steps. For non‑powers of two the loop aborts earlier, never exceeding that bound.

---  

## 3. Component Deep Dive  

### 3.1 `checkPow(int n)`  

```java
private boolean checkPow(int n) {
    // 1. Guard against non‑positive values
    if (n <= 0) return false;

    // 2. Reached the only valid odd power of two (2⁰)
    if (n == 1) return true;

    // 3. Early exit for any odd number > 1
    if (n % 2 != 0) return false;

    // 4. Tail‑recursive halving
    return checkPow(n / 2);
}
```

#### 3.1.1 Guard Clause (`n <= 0`)  

* Handles `0` (by definition *not* a power of two) and all negative numbers.  
* Guarantees that subsequent modulo/division operations never encounter undefined behavior such as division overflow.  

#### 3.1.2 Base Case (`n == 1`)  

* The only odd power of two in the integer domain is `1 = 2⁰`.  
* This terminates the recursion successfully.  

#### 3.1.3 Oddness Test (`n % 2 != 0`)  

* An odd number > 1 cannot be a power of two because every power of two beyond `2⁰` is even.  
* The `%` operator on a positive `int` is cheap (single CPU instruction on modern hardware).  

#### 3.1.4 Recursive Halving (`checkPow(n / 2)`)  

* The division is **integer division**, automatically discarding any remainder (which we already guaranteed is zero).  
* The call is *tail‑recursive*: the result of the recursive call is returned directly without further processing.  
* The Java compiler/JVM **does not** guarantee tail‑call elimination, so each call pushes a stack frame. For a 32‑bit `int`, the worst‑case depth is 31 (when `n = 2³⁰`). This is well within the default stack limits, but the pattern would be unsafe for arbitrarily large inputs (e.g., `long` with values > 2⁶³).  

### 3.2 `isPowerOfTwo(int n)`  

```java
public boolean isPowerOfTwo(int n) {
    return checkPow(n);
}
```

* Thin façade separating the public API from the recursive implementation.  
* Allows future replacement of the algorithm (e.g., bit‑wise) without changing the signature.  

### 3.3 Edge‑Case Handling  

| Input | Expected Output | Reason |
|-------|-----------------|--------|
| `-8`  | `false` | Guard clause rejects negatives. |
| `0`   | `false` | Guard clause rejects zero. |
| `1`   | `true`  | Base case. |
| `2`   | `true`  | `2 → 1`. |
| `3`   | `false` | `3 % 2 != 0`. |
| `INT_MAX` (2,147,483,647) | `false` | Odd → fails early. |
| `INT_MIN` (−2,147,483,648) | `false` | Guard clause (`n <= 0`). |

*Note*: `INT_MIN` cannot be represented as a positive power of two because its absolute value exceeds the maximum positive `int`. The guard clause safely filters it out.

---  

## 4. Key Insights & Gotchas  

### 4.1 Tail‑Recursion vs. Iteration  

* The algorithm is tail‑recursive, but the JVM does **not** perform guaranteed tail‑call optimization.  
* For `int` inputs the recursion depth ≤ 31, making stack overflow impossible under normal JVM defaults.  
* If the same pattern were applied to `long` (`64`‑bit) or a `BigInteger` implementation, the depth could reach 63 or more, increasing the risk of `StackOverflowError`. An iterative loop (`while (n % 2 == 0) n >>= 1;`) would be safer for such ranges.

### 4.2 Modulo vs. Bitwise Test  

* A classic O(1) alternative: `return n > 0 && (n & (n - 1)) == 0;`.  
* The recursive solution trades constant‑time bit‑wise arithmetic for **logarithmic** recursion depth, which is still negligible for `int`.  
* The recursive method may be pedagogically clearer (explicit “divide by 2 until 1”) but is less performant due to function‑call overhead and extra stack usage.

### 4.3 Integer Division Guarantees  

* After confirming `n % 2 == 0`, `n / 2` is guaranteed to be exact (no truncation loss).  
* No need for `Math.floorDiv` or special handling of negative numbers because the guard already eliminates them.

### 4.4 Potential Subtle Bug – Signed Right Shift  

* A tempting micro‑optimisation is to replace `n / 2` with `n >> 1`.  
* **Caution**: `>>` preserves the sign bit, so for negative `n` it would produce a different sequence (e.g., `-2 >> 1 == -1`). The guard clause prevents this, but if the guard were removed or altered, using `>>` could introduce a bug. Prefer `>>>` (unsigned right shift) only after confirming `n > 0`.

### 4.5 Recursive Call Overhead  

* Each call incurs:  
  * Allocation of a new stack frame (≈ 32–64 bytes).  
  * Preservation of the return address and local variables.  
* For the worst‑case (`n = 2³⁰`), total overhead ≈ 2 KB – trivial but measurable compared to a single bit‑wise operation.

### 4.6 Thread‑Safety & Reentrancy  

* The method is **purely functional**: no mutable static state, no side effects.  
* Safe for concurrent execution without synchronization.

---  

## 5. Suggested Refactor (Optional)  

If performance or stack‑size guarantees are a concern, replace recursion with an iterative loop or the bit‑wise trick:

```java
public boolean isPowerOfTwo(int n) {
    // O(1) bit‑wise version – widely used in production code
    return n > 0 && (n & (n - 1)) == 0;
}
```

Or keep the recursive spirit but eliminate stack growth:

```java
public boolean isPowerOfTwoIterative(int n) {
    if (n <= 0) return false;
    while ((n & 1) == 0) {   // while even
        n >>= 1;              // divide by 2
    }
    return n == 1;
}
```

Both alternatives preserve the original semantics while guaranteeing **O(1)** time and **O(1)** space.

---  

## 6. Take‑away Checklist  

- ✅ Guard against `n <= 0`.  
- ✅ Stop recursion at `n == 1`.  
- ✅ Early reject any odd `n > 1`.  
- ✅ Recursive halving is tail‑recursive but not optimized by the JVM; depth ≤ 31 for `int`.  
- ✅ No hidden overflow or sign‑extension issues because of the guard.  
- ✅ For larger numeric types, prefer an iterative or bit‑wise solution.  

---  

*Prepared by the Senior Staff Engineer – 2026‑09‑15*

---
