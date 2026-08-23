class Solution {
    public String minWindow(String s, String t) {
        if (t.length() > s.length())
            return "";
        int[] need = new int[128];
        int[] have = new int[128];
        int st = 0, len = Integer.MAX_VALUE;

        int req = 0, formed = 0;

        for (char c : t.toCharArray()) {
            if (need[c] == 0)
                req++;

            need[c]++;
        }

        int l = 0;
        for (int r = 0; r < s.length(); r++) {
            char c = s.charAt(r);
            have[c]++;
            if (need[c] == have[c])
                formed++;
            while (req == formed) {
                if (r - l + 1 < len) {
                    len = r - l + 1;
                    st = l;
                }
                have[s.charAt(l)]--;
                if (need[s.charAt(l)] > have[s.charAt(l)]) {
                    formed--;
                }
                l++;
            }
        }
        if (len == Integer.MAX_VALUE)
            return "";
        return s.substring(st, st + len);
    }
}
