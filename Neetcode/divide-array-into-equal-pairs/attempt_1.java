class Solution {
    public boolean divideArray(int[] nums) {
        // Store the frequency of each number
        int[] frequency = new int[501];

        for (int number : nums) {
            frequency[number]++;
        }

        // Every number must occur an even number of times
        for (int number = 1; number <= 500; number++) {
            if (frequency[number] % 2 != 0) {
                return false;
            }
        }

        return true;
    }
}
