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
        int[][] dp = new int[st1.length()][st2.length()];
        char[] s1 = st1.toCharArray();
        char[] s2 = st2.toCharArray();
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        ;
        // System.out.println(sub(s1, s2, s1.length - 1, s2.length - 1, dp));
        return sub(s1, s2, s1.length - 1, s2.length - 1, dp);
    }
}