class Solution {
    public boolean checkPow(int n) {
        if (n <= 0)
            return false;

        if (n == 1)
            return true;

        if (n % 2 != 0)
            return false;

        return checkPow(n / 2);
        // Keep dividing by 2. If I can keep doing it evenly until I reach 1 → power of 2. If I encounter an odd number before 1 → not a power of 2.
    }
    public boolean isPowerOfTwo(int n) {
        return checkPow(n);
    }
}