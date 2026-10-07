class Solution {
    public int[] findMissingAndRepeatedValues(int[][] grid) {
        int n = grid.length;
        int totalNumbers = n * n;

        // Store the frequency of each number.
        int[] frequency = new int[totalNumbers + 1];

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                frequency[grid[row][col]]++;
            }
        }

        int repeatedNumber = 0;
        int missingNumber = 0;

        // Find the number appearing twice and the number not appearing.
        for (int number = 1; number <= totalNumbers; number++) {
            if (frequency[number] == 2) {
                repeatedNumber = number;
            } else if (frequency[number] == 0) {
                missingNumber = number;
            }
        }

        return new int[] { repeatedNumber, missingNumber };
    }
}
