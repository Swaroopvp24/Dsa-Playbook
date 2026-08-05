class NumMatrix {
    private int[][] prefix;

    public NumMatrix(int[][] mat) {
        this.prefix = new int[mat.length + 1][mat[0].length + 1];

        // for (int i = 0; i < prefix.length; i++) {
        //     prefix[i][0] = 0;
        // }

        // for (int i = 0; i < prefix.length; i++) {
        //     prefix[0][i] = 0;
        // }
        //In java default values is 0

        for (int i = 1; i < prefix.length; i++) {
            for (int j = 1; j < prefix[0].length; j++) {
                prefix[i][j] =
                    prefix[i - 1][j] + prefix[i][j - 1] - prefix[i - 1][j - 1] + mat[i - 1][j - 1];
            }
        }
    }

    public int sumRegion(int row1, int col1, int row2, int col2) {
        row1++;
        col1++;
        row2++;
        col2++;
        return prefix[row2][col2] - prefix[row1 - 1][col2] - prefix[row2][col1 - 1]
            + prefix[row1 - 1][col1 - 1];
    }
}

/**
 * Your Numprefix object will be instantiated and called as such:
 * Numprefix obj = new Numprefix(prefix);
 * int param_1 = obj.sumRegion(row1,col1,row2,col2);
 */