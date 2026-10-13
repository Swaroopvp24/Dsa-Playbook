class Solution {
    public int maxNumberOfBalloons(String text) {
        int[] charFrequency = new int[26];

        // Count the frequency of each character
        for (char ch : text.toCharArray()) {
            charFrequency[ch - 'a']++;
        }

        // Start with the maximum possible number
        int maxBalloons = Integer.MAX_VALUE;

        // Each balloon needs one 'b', one 'a', and one 'n'
        maxBalloons = Math.min(maxBalloons, charFrequency['b' - 'a']);
        maxBalloons = Math.min(maxBalloons, charFrequency['a' - 'a']);
        maxBalloons = Math.min(maxBalloons, charFrequency['n' - 'a']);

        // Each balloon needs two 'l's and two 'o's
        maxBalloons = Math.min(maxBalloons, charFrequency['l' - 'a'] / 2);
        maxBalloons = Math.min(maxBalloons, charFrequency['o' - 'a'] / 2);

        // Return the maximum number of balloons we can form
        return maxBalloons;
    }
}