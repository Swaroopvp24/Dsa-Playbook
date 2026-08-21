class Solution {
    public int characterReplacement(String s, int k) {
        // Map<Character, Integer> count = new HashMap<>();
        //Since we know only english upperCase letters are there in s we can use freq array to count frequencies
        int[] freq = new int[26];
        int maxf = 0;
        int l = 0, res = 0;
        for (int r = 0; r < s.length(); r++) {
            // count.put(s.charAt(r), count.getOrDefault(s.charAt(r), 0) + 1);
            freq[s.charAt(r) - 'A']++;
            // maxf = Math.max(count.get(s.charAt(r)), maxf);
            maxf = Math.max(freq[s.charAt(r) - 'A'], maxf);
            //the window size - the max time apperaing element count should be less than k to be valid
            while ((r - l + 1) - maxf > k) {
                // count.put(s.charAt(l), count.get(s.charAt(l)) - 1);
                freq[s.charAt(l) - 'A']--;
                l++;
            }
            res = Math.max(res, r - l + 1);
        }
        return res;
    }
}
