class Solution {
    public boolean isValidSudoku(char[][] b) {
        for (int i = 0; i < 9; i++) {
            Set<Character> seen = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (b[i][j] == '.')
                    continue;
                if (seen.contains(b[i][j]))
                    return false;

                seen.add(b[i][j]);
            }
        }

        for (int i = 0; i < 9; i++) {
            Set<Character> seen = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (b[j][i] == '.')
                    continue;
                if (seen.contains(b[j][i]))
                    return false;

                seen.add(b[j][i]);
            }
        }

        for (int sq = 0; sq < 9; sq++) {
            Set<Character> seen = new HashSet<>();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    int r = (sq / 3) * 3 + i;
                    int c = (sq % 3) * 3 + j;
                    if (b[r][c] == '.')
                        continue;
                    if (seen.contains(b[r][c]))
                        return false;

                    seen.add(b[r][c]);
                }
            }
        }
        return true;
    }
}
