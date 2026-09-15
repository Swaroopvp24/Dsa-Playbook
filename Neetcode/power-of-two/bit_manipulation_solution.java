class Solution {
    public boolean isPowerOfTwo(int n) {
        if(n<=0) return false;
        // Every power of 2 has exactly ONE '1' in its binary representation.
        // Example: 8 = 1000

        // N - 1 changes the rightmost '1' to '0'
        // and all the zeros after it to '1'.
        // Example: 101000 -> 100111

        // For a power of 2, that rightmost '1' is the ONLY '1'.
        // Example: 1000 -> 0111 after subtracting 1

        // Therefore, N and N - 1 have no common '1' bits.
        // Example: 1000 & 0111 = 0000

        // For a non-power of 2, there is more than one '1' bit,
        // so N & (N - 1) will not be 0.
        // Example: 10 = 1010, and 10 & 9 = 1010 & 1001 = 1000
        return (n & (n - 1)) == 0;
    }
}