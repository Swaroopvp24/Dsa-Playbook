class Solution {
    public int sub(char[] s1, char[] s2, int i, int j, int[][] dp) {
        if (i < 0 || j < 0) {
            // System.out.println(ds);
            return 0;
        }
        if (dp[i][j] != -1) {
            return dp[i][j];
        }
        if (s1[i] == s2[j]) {
            return dp[i][j] = 1 + sub(s1, s2, i - 1, j - 1, dp);
        } else {
            return dp[i][j] = 0 + Math.max(sub(s1, s2, i, j - 1, dp), sub(s1, s2, i - 1, j, dp));
        }
    }

    public int longestCommonSubsequence(String st1, String st2) {
        int[][] dp = new int[st1.length() + 1][st2.length() + 1];

        int n1 = st1.length() + 1;
        int n2 = st2.length() + 1;

        // First column = 0
        for (int i = 0; i < n1; i++) {
            dp[i][0] = 0;
        }

        // First row = 0
        for (int j = 0; j < n2; j++) {
            dp[0][j] = 0;
        }
        char[] s1 = st1.toCharArray();
        char[] s2 = st2.toCharArray();

        int[] prev = new int[n2];
        int[] cur = new int[n2];

        for (int i = 1; i < n1; i++) {
            for (int j = 1; j < n2; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    cur[j] = 1 + prev[j - 1];
                } else {
                    cur[j] = Math.max(prev[j], cur[j - 1]);
                }
            }

            prev = cur;
            cur = new int[n2]; // Reset current row
        }

        return prev[n2 - 1];
        // return dp[n1-1][n2-1];
    }
}