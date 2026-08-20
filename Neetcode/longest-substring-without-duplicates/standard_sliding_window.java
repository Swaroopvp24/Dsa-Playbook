class Solution {
    public int lengthOfLongestSubstring(String s) {
        Set<Character> seen = new HashSet<>();
        int l = 0;
        int count = 0;
        for (int r = 0; r < s.length(); r++) {
            while (seen.contains(s.charAt(r))) {
                seen.remove(s.charAt(l++));
            }
            seen.add(s.charAt(r));
            // count = Math.max(count,seen.size()); or
            count = Math.max(count, r - l + 1);
        }
        return count;
    }
}