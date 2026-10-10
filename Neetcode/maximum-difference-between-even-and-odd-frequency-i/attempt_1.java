class Solution {
    public int maxDifference(String s) {
        int[] frequency = new int[26];

        // Count the frequency of each character.
        for (char c : s.toCharArray()) {
            frequency[c - 'a']++;
        }

        int minEvenFrequency = Integer.MAX_VALUE;
        int maxOddFrequency = 0;

        // Find the smallest even and largest odd frequencies.
        for (int count : frequency) {
            if (count > 0 && count % 2 == 0) {
                minEvenFrequency = Math.min(minEvenFrequency, count);
            }

            if (count % 2 != 0) {
                maxOddFrequency = Math.max(maxOddFrequency, count);
            }
        }

        // Return the difference between the largest odd and smallest even frequencies.
        return maxOddFrequency - minEvenFrequency;
    }
}