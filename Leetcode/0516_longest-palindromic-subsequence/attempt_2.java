class Solution {
    public String rev(String s) {
        return new StringBuilder(s).reverse().toString();
    }

    public int longestPalindromeSubseq(String st1) {
        String st2 = rev(st1);

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
        // for (int[] row : dp) {
        //     Arrays.fill(row, -1);
        // }
        // System.out.println(sub(s1, s2, s1.length - 1, s2.length - 1, dp));
        dp[0][0] = 0;
        for (int i = 1; i < n1; i++) {
            for (int j = 1; j < n2; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 0 + Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[n1 - 1][n2 - 1];
    }
}