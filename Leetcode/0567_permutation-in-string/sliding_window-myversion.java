class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int[] s1f = new int[26];
        int[] freq = new int[26];
        int k = s1.length();

        for (char c : s1.toCharArray()) {
            s1f[c - 'a']++;
        }

        int l = 0;

        for (int r = 0; r < s2.length(); r++) {
            while (r - l >= k) {
                freq[s2.charAt(l) - 'a']--;
                l++;
            }

            freq[s2.charAt(r) - 'a']++;

            if (Arrays.equals(s1f, freq)) {
                return true;
            }
        }

        return false;
    }
}
