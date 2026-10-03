class Solution {
    public int findLucky(int[] arr) {
        // Store the frequency of each number.
        int[] frequency = new int[501];

        for (int number : arr) {
            frequency[number]++;
        }

        // Check from the largest possible number to find
        // the largest lucky number.
        for (int number = 500; number >= 1; number--) {
            // A lucky number appears exactly as many times as its value.
            if (frequency[number] == number) {
                return number;
            }
        }

        // No lucky number was found.
        return -1;
    }
}
