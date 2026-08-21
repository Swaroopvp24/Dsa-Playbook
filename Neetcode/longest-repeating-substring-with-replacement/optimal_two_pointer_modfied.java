class Solution {
    public int characterReplacement(String s, int k) {
        // Since s contains only uppercase English letters,
        // we can use a frequency array instead of a HashMap.
        int[] characterFrequency = new int[26];

        int mostFrequentCount = 0;
        int left = 0;
        int maxLength = 0;

        for (int right = 0; right < s.length(); right++) {

            characterFrequency[s.charAt(right) - 'A']++;

            mostFrequentCount = Math.max(
                characterFrequency[s.charAt(right) - 'A'],
                mostFrequentCount
            );

            // Window is invalid if the number of characters
            // we need to replace is greater than k.
            while ((right - left + 1) - mostFrequentCount > k) {
                characterFrequency[s.charAt(left) - 'A']--;
                left++;
            }

            maxLength = Math.max(
                maxLength,
                right - left + 1
            );
        }

        return maxLength;
    }
}